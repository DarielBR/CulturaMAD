package com.upmgeoinfo.culturamad.services.firestoredb

import com.google.firebase.firestore.FirebaseFirestore
import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Just in case changes in Firestore collection are necessary in later states of development.
 */
const val COLLECTION_NAME = "event_reviews"
const val USERID_FIELD = "user_id"
const val EVENTID_FIELD = "event_id"
const val REVIEW = "review"
const val RATE = "rate"
const val BOOKMARK = "bookmark"
class FirestoredbRepository {
    val firestoreInstance = FirebaseFirestore.getInstance()

    /**
     * gets all review for a given event. If the operation is successful returns a list (may be empty
     * or not), if not, throws the raised exception.
     *
     * @param eventID the ID of the event whose reviews are required.
     * @return a list with the stored reviews for the event with eventID. If there is no reviews for
     * that event, the list will be empty.
     * @throws  exception an exception if the operation fails.
     */
    suspend fun getEventsReviews(
        eventID: String
    ): List<EventReview> = withContext(Dispatchers.IO){
        val reviewsList: MutableList<EventReview> = emptyList<EventReview>().toMutableList()

        firestoreInstance.collection(COLLECTION_NAME)
            .whereEqualTo(EVENTID_FIELD, eventID)
            .get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    documents.forEach { document ->
                        val eventReview = EventReview(
                            userID = document.get(USERID_FIELD).toString(),
                            eventID = document.get(EVENTID_FIELD).toString(),
                            review = document.get(REVIEW).toString(),
                            rate = document.get(RATE).toString().toFloat(),
                            bookmark = document.get(BOOKMARK).toString().toBoolean()
                        )
                        reviewsList.add(eventReview)
                    }
                }
            }
            .addOnFailureListener {exception ->
                throw exception
            }

        return@withContext reviewsList.toList()
    }

    /**
     * gets the review for a given event and a given user. If the operation is successful returns
     * a EventReview object (may be empty or not), if not, throws the risen exception.
     *
     * @param eventID the ID of the event whose review is required.
     * @param userID the ID of the user whose review is required.
     * @return an EventReview object with the stored review for the event with eventID made by
     * the user with userID. If there is none, the objects will be empty.
     * @throws  exception an exception if the operation fails.
     */
    suspend fun getReview(
        userID: String,
        eventID: String
    ): EventReview = withContext(Dispatchers.IO){
        var eventReview = EventReview()

        firestoreInstance.collection(COLLECTION_NAME)
            .whereEqualTo(USERID_FIELD, userID)
            .whereEqualTo(EVENTID_FIELD, eventID)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty){
                    eventReview = EventReview(
                        userID = result.documents[0].get(USERID_FIELD).toString(),
                        eventID = result.documents[0].get(EVENTID_FIELD).toString(),
                        review = result.documents[0].get(REVIEW).toString(),
                        rate = result.documents[0].get(RATE).toString().toFloat(),
                        bookmark = result.documents[0].get(BOOKMARK).toString().toBoolean(),
                    )
                }
            }
            .addOnFailureListener {exception ->
                throw exception
            }

        return@withContext eventReview
    }

    /**
     * creates a new entry into the collection. If the operation fails, throws the risen exception.
     */
    suspend fun setReview(
        userID: String,
        eventID: String,
        review: String,
        rate: String,
        bookmark: String
    ) = withContext(Dispatchers.IO){
        val data = hashMapOf(
            USERID_FIELD to userID,
            EVENTID_FIELD to eventID,
            REVIEW to review,
            RATE to rate,
            BOOKMARK to bookmark
        )
        firestoreInstance.collection(COLLECTION_NAME)
            .document().set(data)
            .addOnFailureListener { exception ->
                throw exception
            }
    }
}