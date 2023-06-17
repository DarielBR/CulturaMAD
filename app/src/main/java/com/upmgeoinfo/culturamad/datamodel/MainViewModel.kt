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
                items = culturalEventRepository.getCulturalEventsWithLocation()
            )
        }
    }

    fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
        }
    }

    /*fun getCulturalEventsWithLocation(): List<CulturalEvent>{
        var culturalEvents = mutableListOf<CulturalEvent>()
        viewModelScope.launch {
            val newList = culturalEventRepository.getCulturalEventsWithLocation()
            for(item in newList) culturalEvents.add(item)
        }
        return culturalEvents.toList()
    }*/
}