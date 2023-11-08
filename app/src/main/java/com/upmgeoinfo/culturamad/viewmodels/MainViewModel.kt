package com.upmgeoinfo.culturamad.viewmodels

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.upmgeoinfo.culturamad.services.authentication.AuthenticationRepository
import com.upmgeoinfo.culturamad.services.firestoredb.FirestoredbRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.services.room.CulturalEventRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.MainState
import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository
import com.upmgeoinfo.culturamad.viewmodels.auth.model.LoginUiState
import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class MainViewModel(
    private val apiEventsRepository: ApiEventsRepository,//API REST resource consumption
    private val culturalEventRepository: CulturalEventRepository,//Local database (dbLo)
    private val firestoredbRepository: FirestoredbRepository,//Firebase Firestore data base (dbFi)
    private val authenticationRepository: AuthenticationRepository,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    val context: Context? = null
    //TODO: other repos will be added here, ie. Firebase Auth and Firebase FireStore
): ViewModel() {
    val currentUser: FirebaseUser?
        get() = authenticationRepository.currentUser
    val hasUser: Boolean
        get() = authenticationRepository.hasUser()
    var loginUiState by mutableStateOf(LoginUiState())
        private set
    var state by mutableStateOf(MainState())
        private set

/****************General-MainState Block************************/
    init {
        viewModelScope.launch {
            signupAnonymously()
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList(),
                reviews = firestoredbRepository.getAllReviews{}.toMutableList()
            )
            refreshDeviceLocation()
            refreshCurrentUserMail()
        }
    }

    suspend fun signupAnonymously(){
        viewModelScope.launch {
            if (!hasUser){
                authenticationRepository.signupAnonymously {  }
            }
        }
    }

    suspend fun setupStateData(
       onSuccess: (Boolean) -> Unit
    )= viewModelScope.launch{
        //fetch data from API EndPoint and Firestore Database (this is loaded at init block)
        val newStateList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
        var itemsEndPoint: List<CulturalEvent> = emptyList()
        var successOnEndPoint = false
        //fetching event from end point
        itemsEndPoint = apiEventsRepository.getEventsWithLocation { successful -> successOnEndPoint = successful }
        //First, the dbLo tasks
        //do we have data at dbLo?
        if (state.items.isEmpty()){//No
            //Do we have data from EndPoint
            if (!successOnEndPoint) onSuccess.invoke(false)//No data available. This  must trigger actions at SplashScreen
            else{//Yes
                //Insert operation into dbLo
                itemsEndPoint.forEach { culturalEventRepository.insertCulturalEvent(it) }
            }
            //As we have data from EndPoint we will use it to provide state
            //dbFi task
            itemsEndPoint.forEach {endPointItem ->
                //calculating event's averageRate
                endPointItem.averageRate = calculateAverageRate(endPointItem.id!!.toString())
                //getting current user EventReview
                if (hasUser){
                    val storedReview = getUserReview(endPointItem.id!!)
                    with(endPointItem){
                        review = storedReview.review
                        rate= storedReview.rate
                        favorite= storedReview.favorite
                    }
                }
                //Now this item is ready for state update
                newStateList.add(endPointItem)
            }
            //Once the list is thoroughly examined, we are ready to update state
            state = state.copy(items = newStateList)
            onSuccess.invoke(true)

        }else{//Yes
            //Do we have data from EndPoint
            if (successOnEndPoint){
                //Upsert operation into dbLo
                itemsEndPoint.forEach {endPointItem ->
                    if (state.items.find { it.id == endPointItem.id } == null){
                        culturalEventRepository.updateCulturalEvent(endPointItem)
                    }else{
                        culturalEventRepository.insertCulturalEvent(endPointItem)
                    }
                }
                state.items.forEach { stateItem ->
                    if (itemsEndPoint.find { it.id == stateItem.id } == null){
                        culturalEventRepository.deleteCulturalEvent(stateItem.id!!)
                    }
                }
                //As we have data from EndPoint we will use it to provide state
                //dbFi task
                itemsEndPoint.forEach {endPointItem ->
                    //calculating event's averageRate
                    endPointItem.averageRate = calculateAverageRate(endPointItem.id!!.toString())
                    //getting current user EventReview
                    if (hasUser){
                        val storedReview = getUserReview(endPointItem.id!!)
                        with(endPointItem){
                            review = storedReview.review
                            rate= storedReview.rate
                            favorite= storedReview.favorite
                        }
                    }
                    //Now this item is ready for state update
                    newStateList.add(endPointItem)
                }
                //Once the list is thoroughly examined, we are ready to update state
                state = state.copy(items = newStateList)
                onSuccess.invoke(true)
            }else{//No: local data only
                state.items.forEach {localItem ->
                    //idem, but using local data
                    localItem.averageRate = calculateAverageRate(localItem.id!!.toString())
                    if (hasUser){
                        val storedReview = getUserReview(localItem.id)
                        with(localItem){
                            review = storedReview.review
                            rate = storedReview.rate
                            favorite = storedReview.favorite
                        }
                        newStateList.add(localItem)
                    }
                }
                state = state.copy(items = newStateList)
                onSuccess.invoke(true)
            }
        }
    }

    fun setupStateDataAfterUserChange(){
        val newStateList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
        if (hasUser){
            state.items.forEach { localItem ->
                val storedReview = getUserReview(localItem.id!!)
                with(localItem){
                    review = storedReview.review
                    rate = storedReview.rate
                    favorite = storedReview.favorite
                }
                newStateList.add(localItem)
            }
            state = state.copy(items = newStateList)
        }else{
            state.items.forEach { localItem ->
                with(localItem){
                    favorite = false
                    rate = 0.0f
                    review = ""
                }
                newStateList.add(localItem)
            }
            state = state.copy(items = newStateList)
        }
    }

    /**
     * gets a user review for a given event from the review list stored at the state
     */
    private fun getUserReview(eventID: Int): EventReview{
        return state.reviews.find {
            it.eventID == eventID.toString() && it.userID == loginUiState.currentUserMail
        } ?: EventReview()
    }

    fun getEventReviews(eventID: Int): List<EventReview>{
        val reviewsList: MutableList<EventReview> = emptyList<EventReview>().toMutableList()

        state.reviews.forEach { storedReview ->
            if (storedReview.eventID == eventID.toString())
                reviewsList.add(storedReview)
        }

        return reviewsList.toList()
    }
    private fun calculateAverageRate(
        eventId: String
    ): Float{
        var averageSum = 0.0f
        state.reviews.forEach { review ->
            if (review.eventID == eventId) averageSum += review.rate
        }
        return averageSum/state.reviews.size
    }

    /**
     * refreshes the state items to those with location
     */
    suspend private fun refreshItems(){
        //TODO: Modify this function to provide state with information fetched from dbFi
        //new version
        var localItems: List<CulturalEvent> = emptyList()
        viewModelScope.async {
            localItems = culturalEventRepository.getCulturalEventsWithLocation()
        }.await()
        if (hasUser){
            localItems.forEach { localItem ->
                var eventReview = EventReview()
                var storedAverageRate: Float = 0.0f
                viewModelScope.async {
                    eventReview = firestoredbRepository.getReview(
                        userID = loginUiState.currentUserMail,
                        eventID = localItem.id.toString()
                    )
                    storedAverageRate = firestoredbRepository.getEventAverageRate(
                        eventID = localItem.id.toString()
                    ){}
                }.await()
                with(localItem) {
                    review = eventReview.review
                    rate = eventReview.rate
                    favorite = eventReview.favorite
                    averageRate = storedAverageRate
                }
            }
            state = state.copy(
                items = localItems.toMutableList()
            )
        }
        else{
            var storedAverageRate: Float = 0.0f
            localItems.forEach{localItem ->
                viewModelScope.async {
                    storedAverageRate = firestoredbRepository.getEventAverageRate(
                        eventID = localItem.id.toString()
                    ){}
                }.await()
                localItem.averageRate = storedAverageRate
            }
            state = state.copy(
                items = localItems.toMutableList()
            )
        }
    }

    /**
     * Adds a new cultural event into the dbLo
     */
    private fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
        }
    }

    /**
     * updates a cultural event information in the dbLo
     */
    private fun updateCulturalEvent(
        culturalEvent: CulturalEvent
    ){
        viewModelScope.launch{
            culturalEventRepository.updateCulturalEvent(
                culturalEvent = culturalEvent
            )
        }
    }

    /**
     * provides state for the current selected item
     */
    fun setCurrentItem(newId: String){
        state = state.copy(
            currentItem = newId
        )
    }

    /**
     * returns the event with the currentID at the state, or an empty event if it doesn't exist.
     */
    fun getCurrentEvent(): CulturalEvent? {
        /*var currentEvent = CulturalEvent()
        viewModelScope.launch {
            currentEvent =  culturalEventRepository.getCulturalEventEntityById(state.currentItem.toInt())
        }
        return currentEvent*/
        return state.items.find { it.id == state.currentItem.toInt() }
    }

    /**
     * deletes a cultural event from the dbLo
     */
    private fun deleteCulturalEvent(eventID: Int){
        viewModelScope.launch {
            culturalEventRepository.deleteCulturalEvent(eventID = eventID)
        }
    }

    /**
     * provides state to the splash screen state
     */
    fun hideBottomNavBar(isDisplayed: Boolean) = viewModelScope.launch {
        state = state.copy(isSplashScreenOnRender = isDisplayed)
    }

    fun emptyActiveSearchCategories() = viewModelScope.launch {
        state = state.copy(activeSearchCategories = emptyList<String>().toMutableList())
    }

    fun changeActiveSearchCategories(searchCategory: String) = viewModelScope.launch {
        val categoryPos = state.activeSearchCategories.indexOf(searchCategory)
        val currentActiveSearchCategories = state.activeSearchCategories

        if (categoryPos >= 0){
            currentActiveSearchCategories.removeAt(categoryPos)
        }else{
            currentActiveSearchCategories.add(searchCategory)
        }
        state = state.copy(activeSearchCategories = currentActiveSearchCategories)
    }
    fun clearSearchValue() = viewModelScope.launch {
        state = state.copy(searchValue = "")
    }

    fun changeSearchValue(newValue: String) = viewModelScope.launch {
        state = state.copy(searchValue = newValue)
    }

/****************General-MainState Block************************/

/****************Authentication Block***************************/

    fun refreshCurrentUserMail() = viewModelScope.launch {
        val mail = authenticationRepository.getCurrentUserMail()
        loginUiState = loginUiState.copy(currentUserMail = mail)
    }

    fun onUserNameChange(userName: String){
        loginUiState = loginUiState.copy(userName = userName)
    }

    fun onUserPasswordChange(password: String){
        loginUiState = loginUiState.copy(password = password)
    }

    fun onUserNameSignupChange(userNameSignup: String){
        loginUiState = loginUiState.copy(userNameSignup = userNameSignup)
    }

    fun onPasswordSignupChange(passwordSignup: String){
        loginUiState = loginUiState.copy(passwordSignup = passwordSignup)
    }

    fun onConfirmPasswordSignupChange(confirmPasswordSignup: String){
        loginUiState = loginUiState.copy(confirmPasswordSignup = confirmPasswordSignup)
    }

    private fun validateLoginForm() = loginUiState.userName.isNotBlank()
            && loginUiState.password.isNotBlank()

    private fun validateSignupForm() = loginUiState.userNameSignup.isNotBlank()
            && loginUiState.passwordSignup.isNotBlank()
            && loginUiState.confirmPasswordSignup.isNotBlank()

    fun createUser(
        context: Context,
        onComplete: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {
            if (!validateSignupForm())
                throw IllegalArgumentException("e-mail and/or password can not be blank.")
            loginUiState = loginUiState.copy(isLoading = true)
            if (loginUiState.passwordSignup != loginUiState.confirmPasswordSignup)
                throw IllegalArgumentException("passwords do not match.")
            loginUiState = loginUiState.copy(signupError = null)
            authenticationRepository.createUser(
                email = loginUiState.userNameSignup,
                password = loginUiState.passwordSignup
            ){isSuccessful ->
                if (isSuccessful){
                    onComplete.invoke(true)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = true)
                    //loginUiState = loginUiState.copy(currentUser = currentUser)
                    Toast.makeText(context, "Signup Successful!", Toast.LENGTH_LONG).show()
                }else{
                    onComplete.invoke(false)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = false)
                    Toast.makeText(context, "Signup Failure!", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e: Exception){
            loginUiState = loginUiState.copy(signupError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun loginUser(
        context: Context,
        onComplete: (Boolean) -> Unit
    ) = viewModelScope.launch {
        try {
            if (!validateLoginForm())
                throw IllegalArgumentException("e-mail and/or password can not be blank.")
            loginUiState = loginUiState.copy(isLoading = true)
            loginUiState = loginUiState.copy(loginError = null)
            authenticationRepository.logIn(
                email = loginUiState.userName,
                password = loginUiState.password
            ){isSuccessful ->
                if (isSuccessful){
                    onComplete.invoke(true)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = true)
                    Toast.makeText(context, "Login Successful!", Toast.LENGTH_LONG).show()
                }else{
                    onComplete.invoke(false)
                    loginUiState = loginUiState.copy(isSuccessOnLogin = false)
                    Toast.makeText(context, "Login Failure!", Toast.LENGTH_LONG).show()
                }
            }
        }catch (e: Exception){
            loginUiState = loginUiState.copy(loginError = e.localizedMessage)
            e.printStackTrace()
        }finally {
            loginUiState = loginUiState.copy(isLoading = false)
        }
    }

    fun clearStateValues() = viewModelScope.launch{
        loginUiState = loginUiState.copy(userName = "")
        loginUiState = loginUiState.copy(password = "")
        loginUiState = loginUiState.copy(userNameSignup = "")
        loginUiState = loginUiState.copy(passwordSignup = "")
        loginUiState = loginUiState.copy(confirmPasswordSignup = "")
    }

    fun resetErrors() = viewModelScope.launch {
        loginUiState = loginUiState.copy(loginError = null)
        loginUiState = loginUiState.copy(signupError = null)
    }

    fun logOutUser() = viewModelScope.launch {
        authenticationRepository.logOut()
    }

/****************Authentication Block***************************/

/****************Firestore Block********************************/
    fun addReview(
        culturalEvent: CulturalEvent,
        review: String,
        rate: Float,
        favorite: Boolean
    ) = viewModelScope.launch {
        if (hasUser){
            firestoredbRepository.addReview(
                userID = loginUiState.currentUserMail,
                eventID = culturalEvent.id.toString(),
                review = review,
                favorite = review.toString(),
                rate = rate.toString()
            )
        }
    }

    /**
     * TODO:    Create getEventReviews()
     *          Create deleteEventReviews()
     */

    /**
     * returns the averaged rate for a given cultural event, calculated with the data stored at dbFi.
     */
    suspend fun getEventAverageRate(
        culturalEvent: CulturalEvent
    ): Float{
        return viewModelScope.async {
            firestoredbRepository.getEventAverageRate(eventID = culturalEvent.id.toString()){}
        }.await()
        /*var result: Float = 0.0f
        viewModelScope.launch{
            result = firestoredbRepository.getEventAverageRate(
                eventID = culturalEvent.id.toString()
            ){}
        }
        return result*/
    }

    fun getUserEventReview(
        userID: String,
        eventID: Int
    ): EventReview {
        var result = EventReview()

        viewModelScope.launch {
            result = firestoredbRepository.getReview(
                userID = userID,
                eventID = eventID.toString()
            )
        }
        return result
    }

    suspend fun loadUserEventsReviews() = viewModelScope.async{
        if (hasUser){
            state.items.forEach { stateEvent ->
                val eventReview = getUserEventReview(
                    userID = loginUiState.currentUserMail,
                    eventID = stateEvent.id ?: 0
                )
                val averageRate = getEventAverageRate(stateEvent)

                state.items.find { it.id == stateEvent.id }?.review = eventReview.review
                state.items.find { it.id == stateEvent.id }?.favorite = eventReview.favorite
                state.items.find { it.id == stateEvent.id }?.rate = eventReview.rate
                state.items.find { it.id == stateEvent.id }?.averageRate = averageRate

            }
        }
    }.await()

    /**
     * updates only the favorite state for a cultural event at the dbFi
     */
    fun changeFavoriteState(
        culturalEvent: CulturalEvent,
        favorite: Boolean
    ){
        viewModelScope.launch {
            firestoredbRepository.updateFavorite(
                userID = loginUiState.currentUserMail,
                eventID = culturalEvent.id.toString(),
                favorite = favorite
            ){isSuccessful ->
                if (isSuccessful){//provide state
                    val index = state.items.indexOfFirst { it.id == culturalEvent.id }
                    state.items[index].favorite = favorite
                }
            }
        }
    }

    /**
     * Writes into the dbFi a given rate value for a given cultural event.
     */
    fun setEventRate(
        culturalEvent: CulturalEvent,
        rate: Float
    ){
        viewModelScope.launch {
            var success = false
            firestoredbRepository.updateRate(
                userID = loginUiState.currentUserMail,
                eventID = culturalEvent.id.toString(),
                rate = rate
            ){isSuccesful ->
                //TODO: Must call getEventRate() and provide state accordingly
                success = isSuccesful
            }
            if (success){
                val index = state.items.indexOfFirst { it.id == culturalEvent.id }
                state.items[index].averageRate =
                    getEventAverageRate(culturalEvent = culturalEvent)
            }
        }
    }

    fun setEventReview(
        culturalEvent: CulturalEvent,
        review: String
    ){
        viewModelScope.launch {
            firestoredbRepository.updateReview(
                userID = loginUiState.currentUserMail,
                eventID = culturalEvent.id.toString(),
                review = review
            ){isSuccessful ->
                if (isSuccessful){//provide state
                    val index = state.items.indexOfFirst { it.id == culturalEvent.id }
                    state.items[index].review = review
                }
            }
        }
    }

/****************Firestore Block********************************/
/****************Utils Block************************************/

    /**
     * Returns the distance in Km between two points give geographical coordinates. Uses the Earth's
     * mean radius equal to 6'371Km.
     *
     * @param positionA first coordinate.
     * @param position second coordinate.
     *
     * @return The distance between coordinates in Km
     */
    //@Composable
    fun calculateDistanceOverEarth(
        latitude: Double,
        longitude: Double
    ): Double? {
        //var deviceLatitude: Double? by remember { mutableStateOf(null) }
        val deviceLatitude = state.deviceLocation?.latitude ?: null
        //var deviceLongitude: Double? by remember { mutableStateOf(null) }
        val deviceLongitude = state.deviceLocation?.longitude ?: null

        if(deviceLatitude != null && deviceLongitude != null){
            val latA = state.deviceLocation!!.latitude * PI / 180
            val latB = latitude * PI / 180

            val deltaLat = (latitude - state.deviceLocation!!.latitude) * PI / 180
            val deltaLng = (longitude - state.deviceLocation!!.longitude) * PI / 180

            val earthRadius: Double = 6371.0 //in Km

            val haversine =
                sin(deltaLat / 2).pow(2) + cos(latA) * cos(latB) * sin(deltaLng / 2).pow(2)
            val c = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

            return earthRadius * c
        }else{
            return null
        }
    }

    fun refreshDeviceLocation(){
        val locationPermissionState = ContextCompat.checkSelfPermission(
            context!!,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (locationPermissionState == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        state = state.copy(
                            deviceLocation = LatLng(location.latitude, location.longitude),
                            isLocationPermissionGranted = true
                        )
                    }
                }
        }
    }

    fun getStateDeviceLocation(): LatLng? {
        return state.deviceLocation
    }

    fun getEventsAndAds(): List<CulturalEvent>{
        val prevList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()

        state.items.forEach { event ->
            if(state.searchValue != ""){
                if (event.title.contains(state.searchValue, ignoreCase = true)){
                    prevList.add(event)
                }
            }else{
                prevList.add(event)
            }
        }
        val resultList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
        var counter = -1
        prevList.forEach { event ->
            if(counter == 2){
                val adItem = CulturalEvent(id = 0)
                resultList.add(adItem)
                resultList.add(event)
                counter = 0
            }else {
                resultList.add(event)
                counter++
            }
        }
        return resultList.toList()
    }

    //@Composable
    fun getNearByEvents(
        radius: Int = 5
    ): List<CulturalEvent>{
        val prevList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()

        state.items.forEach { event ->
            if (calculateDistanceOverEarth(
                    latitude = event.latitude.toDouble(),
                    longitude = event.longitude.toDouble()
                )!! <= radius.toDouble()){
                if(state.searchValue != ""){
                    if (event.title.contains(state.searchValue, ignoreCase = true)){
                        prevList.add(event)
                    }
                }else{
                    prevList.add(event)
                }
            }
        }
        val resultList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
        var counter = 0
        prevList.forEach { event ->
            if(counter == 3){
                val adItem = CulturalEvent(id = 0)
                resultList.add(adItem)
                resultList.add(event)
                counter = 0
            }else {
                resultList.add(event)
                counter++
            }
        }
        resultList.sortBy { it.averageRate }
        return resultList.toList()
    }

    fun getMostRatedEvents(
        amount: Int = 50
    ): List<CulturalEvent>{
        val prevList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()

        state.items.forEach { event ->
            if(state.searchValue != ""){
                if (event.title.contains(state.searchValue, ignoreCase = true)){
                    prevList.add(event)
                }
            }else{
                prevList.add(event)
            }
        }
        prevList.sortBy { it.averageRate }

        val resultList: MutableList<CulturalEvent> = emptyList<CulturalEvent>().toMutableList()
        var addCounter = -1
        val limit = if (amount < prevList.size) amount else prevList.size

        for(i in 0 until limit) {
            if (addCounter == 2) {
                val adItem = CulturalEvent(id = 0)
                resultList.add(adItem)
                resultList.add(prevList[i])
                addCounter = 0
            } else {
                resultList.add(prevList[i])
                addCounter++
            }
        }
        return resultList.toList()
    }
/****************Utils Block************************************/
}

