package com.upmgeoinfo.culturamad.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.upmgeoinfo.culturamad.services.authentication.AuthenticationRepository
import com.upmgeoinfo.culturamad.services.firestoredb.FirestoredbRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import com.upmgeoinfo.culturamad.services.room.CulturalEventRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.MainState
import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository
import com.upmgeoinfo.culturamad.viewmodels.auth.model.LoginUiState
import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview
import kotlinx.coroutines.launch

class MainViewModel(
    private val apiEventsRepository: ApiEventsRepository,//API REST resource consumption
    private val culturalEventRepository: CulturalEventRepository,//Local database (dbLo)
    private val firestoredbRepository: FirestoredbRepository,//Firebase Firestore data base (dbFi)
    private val authenticationRepository: AuthenticationRepository
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
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
            )
            refreshCurrentUserMail()
        }
    }

    /**
     * refreshes the state items to those with location
     */
    private fun refreshItems(){
        //TODO: Modify this function to provide state with information fetched from dbFi
        viewModelScope.launch {
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
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
     * returns the event with the currentID at the state
     */
    fun getCurrentEvent(): CulturalEvent {
        var currentEvent = CulturalEvent()
        viewModelScope.launch {
            currentEvent =  culturalEventRepository.getCulturalEventEntityById(state.currentItem.toInt())
        }
        return currentEvent
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
    fun changeSplashScreenState(isDisplayed: Boolean) = viewModelScope.launch {
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

/****************API consumption Block**************************/
    /**
     * Fetches the list of Events from the URI resource and makes an upsert into the app local database.
     * While doing this, updates the review and favorites information stores at dbFi.
     */
    fun setupEventsData(
        onSuccess: (Boolean) -> Unit
    ) = viewModelScope.launch{

        //Fetching data from API End Point
        var connectionSuccessful = false
        val eventsListFromJsonFile = apiEventsRepository.parseJasonFile()
            {isSuccessful -> connectionSuccessful = isSuccessful}
        if (connectionSuccessful){//Load data into dbLo
            if (state.items.isEmpty()){//There are no entries at the dbLo
                eventsListFromJsonFile.forEach { jsonEntry -> saveCulturalEvent(jsonEntry) }
            }else{//There are entries ar dbLo so, and upsert is required
                eventsListFromJsonFile.forEach {jsonItem -> //first we update with the API data
                    val dbItem = state.items.find { dbItem -> dbItem.id == jsonItem.id}
                    if (dbItem != null) updateCulturalEvent(//Update only in the dbLo
                        culturalEvent = jsonItem,
                    )else saveCulturalEvent(jsonItem)
                }
                state.items.forEach {dbItem -> //then eliminate old entries from dbLo
                    val jsonItem = eventsListFromJsonFile.find { jsonItem -> jsonItem.id == dbItem.id }
                    if (jsonItem == null) deleteCulturalEvent(dbItem.id ?: 0)
                }
            }
            //with the dbLo updated we must provide state
            refreshItems()
            //now we must load data from dbFi
            if (hasUser) {
                //TODO: Get the userID information from the state: MainState
                //      Update the information stored at dbFi
                loadUserEventsReviews()
            }
            onSuccess.invoke(true)
        }else{//not a successful connection
            if (state.items.isEmpty()){//there is no data to work with
                onSuccess.invoke(false)
            }else {
                refreshItems()//use the dbLo data
                if (hasUser){
                    //TODO: Get the userID information from the state: MainState
                    //      Update the information stored at dbFi
                    loadUserEventsReviews()
                }
                onSuccess.invoke(true)
            }
        }
    }

/****************API consumption Block**************************/

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
    fun getEventRate(
        culturalEvent: CulturalEvent
    ): Float{
        var result: Float = 0.0f
        viewModelScope.launch{
            result = firestoredbRepository.getEventRate(
                eventID = culturalEvent.id.toString()
            ){}
        }
        return result
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

    fun loadUserEventsReviews(){
        if (hasUser){
            state.items.forEach { stateEvent ->
                val eventReview = getUserEventReview(
                    userID = loginUiState.currentUserMail,
                    eventID = stateEvent.id ?: 0
                )
                stateEvent.review = eventReview.review
                stateEvent.favorite = eventReview.favorite
                stateEvent.rate = eventReview.rate
            }
        }
    }

    /**
     * updates only the favorite state for a cultural event at the dbFi
     */
    fun changeFavoriteState(
        culturalEvent: CulturalEvent,
        favorite: Boolean
    ){
        viewModelScope.launch {
            /*culturalEventRepository.updateCulturalEvent(
                culturalEvent = culturalEvent,
                favorite = favorite
            )*/
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
            firestoredbRepository.updateRate(
                userID = loginUiState.currentUserMail,
                eventID = culturalEvent.id.toString(),
                rate = rate
            ){isSuccesful ->
                //TODO: Must call getEventRate() and provide state accordingly
                val index = state.items.indexOfFirst { it.id == culturalEvent.id }
                state.items[index].rate = getEventRate(culturalEvent = culturalEvent)
            }
        }
    }

/****************Firestore Block********************************/
}

