package com.upmgeoinfo.culturamad.viewmodels.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upmgeoinfo.culturamad.datamodel.CulturalEvent
import com.upmgeoinfo.culturamad.services.room.CulturalEventRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.MainState
import com.upmgeoinfo.culturamad.services.json_parse.reposiroty.ApiEventsRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val culturalEventRepository: CulturalEventRepository,
    private val apiEventsRepository: ApiEventsRepository
    //TODO: other repos will be added here, ie. Firebase Auth and Firebase FireStore
): ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
            )
        }
    }

    private fun refreshItems(){
        viewModelScope.launch {
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
            )
        }
    }

    private fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
        }
    }

    private fun updateCulturalEvent(culturalEvent: CulturalEvent, bookmark: Boolean, review: Int){
        viewModelScope.launch{
            culturalEventRepository.updateCulturalEvent(culturalEvent,bookmark,review)
        }
    }

    fun changeBookmarkState(culturalEvent: CulturalEvent, bookmark: Boolean){
        viewModelScope.launch {
            culturalEventRepository.updateCulturalEvent(culturalEvent, bookmark)
        }
    }

    fun setCurrentItem(newId: String){
        state = state.copy(
            currentItem = newId
        )
    }

    fun getCurrentEvent(): CulturalEvent {
        var currentEvent = CulturalEvent()
        viewModelScope.launch {
            currentEvent =  culturalEventRepository.getCulturalEventEntityById(state.currentItem.toInt())
        }
        return currentEvent
    }

    private fun deleteCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.deleteCulturalEvent(culturalEvent)
        }
    }

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

    /**
     * Fetches the list of Events from the URI resource and makes an upsert into the app database.
     */
    fun fetchCulturalEventsFormJsonFile() = viewModelScope.launch{
        val eventsListFromJsonFile = apiEventsRepository.parseJasonFile()
        if (state.items.isEmpty()){
            eventsListFromJsonFile.forEach {
                saveCulturalEvent(it)
            }
        }else{
            eventsListFromJsonFile.forEach {jsonItem ->
                val dbItem = state.items.find { dbItem -> dbItem.id == jsonItem.id}
                if (dbItem != null) updateCulturalEvent(jsonItem, dbItem.bookmark, dbItem.review!!)
                else saveCulturalEvent(jsonItem)
            }
            state.items.forEach {dbItem ->
                val jsonItem = eventsListFromJsonFile.find { jsonItem -> jsonItem.id == dbItem.id }
                if (jsonItem == null) deleteCulturalEvent(dbItem)
            }
        }
        refreshItems()
        //changeSplashScreenState(true)
        //changeSplashScreenState(false)
    }
}