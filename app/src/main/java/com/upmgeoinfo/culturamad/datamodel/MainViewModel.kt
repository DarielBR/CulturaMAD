package com.upmgeoinfo.culturamad.datamodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import com.upmgeoinfo.culturamad.datamodel.database.MainState
import com.upmgeoinfo.culturamad.ui.composables.currentNavigationEntry
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val culturalEventRepository: CulturalEventRepository
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

    fun refreshItems(){
        viewModelScope.launch {
            state = state.copy(
                items = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
            )
        }
    }

    fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
        }
    }

    fun updateCulturalEvent(culturalEvent: CulturalEvent, bookmark: Boolean, review: Int){
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

    fun deleteCulturalEvent(culturalEvent: CulturalEvent){
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
        var categoryPos = state.activeSearchCategories.indexOf(searchCategory)
        var currentActiveSearchCategories = state.activeSearchCategories

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
}