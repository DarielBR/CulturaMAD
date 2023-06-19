package com.upmgeoinfo.culturamad.datamodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import com.upmgeoinfo.culturamad.datamodel.database.MainState
import kotlinx.coroutines.launch

class MainViewModel(
    private val culturalEventRepository: CulturalEventRepository
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

    fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
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
}