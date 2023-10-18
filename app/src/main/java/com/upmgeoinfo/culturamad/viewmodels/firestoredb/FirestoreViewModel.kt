package com.upmgeoinfo.culturamad.viewmodels.firestoredb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upmgeoinfo.culturamad.services.firestoredb.FirestoredbRepository
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import kotlinx.coroutines.launch

class FirestoreViewModel(
    private val firestoredbRepository: FirestoredbRepository
): ViewModel() {

    fun addReview(
        culturalEvent: CulturalEvent,
        userID: String,
        review: String,
        rate: Float,
        favorite: Boolean
    ) = viewModelScope.launch {
        firestoredbRepository.addReview(
            userID = userID,
            eventID = culturalEvent.id.toString(),
            review = review,
            rate = rate.toString(),
            favorite = favorite.toString(),
        )
    }
}