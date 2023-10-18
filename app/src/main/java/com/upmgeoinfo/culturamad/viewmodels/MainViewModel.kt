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
        }
    }

    /**
     * refreshes the state items to those with location
     */
    private fun refreshItems(){
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
        culturalEvent: CulturalEvent,
        favorite: Boolean,
        review: String,
        rate: Float
    ){
        viewModelScope.launch{
            culturalEventRepository.updateCulturalEvent(
                culturalEvent = culturalEvent,
                favorite = favorite,
                review = review,
                rate = rate
            )
        }
    }

    /**
     * updates only the favorite state for a cultural event at the dbLo
     */
    fun changeBookmarkState(
        culturalEvent: CulturalEvent,
        favorite: Boolean
    ){
        viewModelScope.launch {
            culturalEventRepository.updateCulturalEvent(
                culturalEvent = culturalEvent,
                favorite = favorite
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
    fun fetchCulturalEventsFormJsonFile() = viewModelScope.launch{
        //TODO: Get the userID information from the state: MainState
        //      Update the information stored at dbFi

        val eventsListFromJsonFile = apiEventsRepository.parseJasonFile()

        if (state.items.isEmpty()){//There are no entries at the dbLo
            eventsListFromJsonFile.forEach { jsonEntry -> saveCulturalEvent(jsonEntry) }
        }else{//an upsert is needed
            eventsListFromJsonFile.forEach {jsonItem ->
                val dbItem = state.items.find { dbItem -> dbItem.id == jsonItem.id}
                if (dbItem != null) updateCulturalEvent(
                    culturalEvent = jsonItem,
                    favorite = dbItem.favorite,
                    rate = dbItem.rate ?: 0.0f,
                    review = dbItem.review
                )
                else saveCulturalEvent(jsonItem)
            }
            state.items.forEach {dbItem ->
                val jsonItem = eventsListFromJsonFile.find { jsonItem -> jsonItem.id == dbItem.id }
                if (jsonItem == null) deleteCulturalEvent(dbItem.id ?: 0)
            }
        }
        refreshItems()
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
}

/****************Authentication Block***************************/

/****************Firestore Block********************************/