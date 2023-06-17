package com.upmgeoinfo.culturamad.datamodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upmgeoinfo.culturamad.datamodel.database.CulturalEventRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val culturalEventRepository: CulturalEventRepository
): ViewModel() {
    fun saveCulturalEvent(culturalEvent: CulturalEvent){
        viewModelScope.launch {
            culturalEventRepository.insertCulturalEvent(culturalEvent = culturalEvent)
        }
    }

    fun getCulturalEventsWithLocation(): List<CulturalEvent>{
        var culturalEvents = mutableListOf<CulturalEvent>()
        viewModelScope.launch {
            culturalEvents = culturalEventRepository.getCulturalEventsWithLocation().toMutableList()
        }
        return culturalEvents.toList()
    }
}