package com.upmgeoinfo.culturamad.services.firestoredb

import com.google.firebase.firestore.FirebaseFirestore
import com.upmgeoinfo.culturamad.viewmodels.firestoredb.model.EventReview
import com.upmgeoinfo.culturamad.viewmodels.main.model.CulturalEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * Just in case changes in Firestore collection are necessary in later states of development.
 *
 * current structure:
 *
 *  userID_eventID ->   "user_id" = "" :String
 *                      "event_id" = "" :String
 *                      "review" = "", :String
 *                      "rate" = 0.0f, :String
 *                      "favorite" = false :Boolean
 *
 */

const val COLLECTION_NAME = "event_reviews"
const val USERID_FIELD = "user_id"
const val EVENTID_FIELD = "event_id"
const val REVIEW = "review"
const val RATE = "rate"
const val FAVORITE = "favorite"

class FirestoredbRepository {
    val firestoreInstance = FirebaseFirestore.getInstance()

    /**
     * creates a new document entry into the collection using as name, the user id and the event is.
     * If the operation fails, throws the risen exception.
     *
     * @param userID a string with the user id
     * @param eventID a string with the event id
     *
     *@throws exception if the operation fails.
     */
    suspend fun addReview(
        userID: String,
        eventID: String,
        review: String,
        rate: String,
        favorite: String
    ) = withContext(Dispatchers.IO){
        val docName = userID + "_" + eventID
        val data = hashMapOf(
            USERID_FIELD to userID,
            EVENTID_FIELD to eventID,
            REVIEW to review,
            RATE to rate,
            FAVORITE to favorite
        )

        firestoreInstance.collection(COLLECTION_NAME)
            .document(docName).set(data)
            .addOnFailureListener { exception ->
                throw exception
            }
    }

    suspend fun getAllReviews(
        onSuccess: (Boolean) -> Unit
    ): List<EventReview> {
        var returnList: MutableList<EventReview> = emptyList<EventReview>().toMutableList()

        withContext(Dispatchers.IO){
            firestoreInstance.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener {documents ->
                    if (!documents.isEmpty){
                        documents.forEach { document ->
                            val eventReview = EventReview(
                                userID = document.get(USERID_FIELD)?.toString() ?: "",
                                eventID = document.get(EVENTID_FIELD)?.toString() ?: "",
                                review = document.get(REVIEW)?.toString() ?: "",
                                rate = document.get(RATE)?.toString()?.toFloat() ?: 0.0f,
                                favorite = document.get(FAVORITE)?.toString().toBoolean() ?: false
                            )
                            returnList.add(eventReview)
                        }
                    }
                }
                .addOnFailureListener { onSuccess.invoke(false) }
                .await()
        }
        return returnList
    }

    /**
     * gets all review for a given event. If the operation is successful returns a list (may be empty
     * or not), if not, throws the raised exception.
     *
     * @param eventID the ID of the event whose reviews are required.
     * @return a list with the stored reviews for the event with eventID. If there is no reviews for
     * that event, the list will be empty.
     * @throws  exception an exception if the operation fails.
     */
    suspend fun getEventReviews(
        eventID: String
    ): List<EventReview> = withContext(Dispatchers.IO){
        val reviewsList: MutableList<EventReview> = emptyList<EventReview>().toMutableList()
        val paramSearch = "_" + eventID
        firestoreInstance.collection(COLLECTION_NAME)
            .whereArrayContains(EVENTID_FIELD, eventID)
            .get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    documents.forEach { document ->
                        val eventReview = EventReview(
                            userID = document.get(USERID_FIELD)?.toString() ?: "",
                            eventID = document.get(EVENTID_FIELD)?.toString() ?: "",
                            review = document.get(REVIEW)?.toString() ?: "",
                            rate = document.get(RATE)?.toString()?.toFloat() ?: 0.0f,
                            favorite = document.get(FAVORITE)?.toString().toBoolean() ?: false
                        )
                        reviewsList.add(eventReview)
                    }
                }
            }
            .addOnFailureListener {exception ->
                throw exception
            }
            .await()

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
                        userID = result.documents[0].get(USERID_FIELD)?.toString() ?: "",
                        eventID = result.documents[0].get(EVENTID_FIELD)?.toString() ?: "",
                        review = result.documents[0].get(REVIEW)?.toString() ?: "",
                        rate = result.documents[0].get(RATE)?.toString()?.toFloat() ?: 0.0f,
                        favorite = result.documents[0].get(FAVORITE)?.toString()?.toBoolean() ?: false,
                    )
                }
            }
            .addOnFailureListener {exception ->
                throw exception
            }
            .await()

        return@withContext eventReview
    }

    /**
     * returns the averaged rate of a given Cultural Event
     */
    suspend fun getEventAverageRate(
        eventID: String,
        onSuccess: (Boolean) -> Unit
    ): Float = withContext(Dispatchers.IO){
        var averageRate: Float = 0.0f
        var rateSum: Float = 0.0f

        firestoreInstance.collection(COLLECTION_NAME)
            .whereEqualTo(EVENTID_FIELD, eventID)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty){
                    result.documents.forEach { document ->
                        if (document.get(RATE) != null)
                            rateSum += document.get(RATE).toString().toFloat()
                    }
                    averageRate = rateSum/result.documents.size
                    onSuccess.invoke(true)
                }
            }
            .addOnFailureListener { onSuccess.invoke(false) }
            .await()

        return@withContext averageRate
    }

    suspend fun updateFavorite(
        userID: String,
        eventID: String,
        favorite: Boolean,
        onSuccess: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        val docName = userID + "_" + eventID
        //var isCreated = false

        firestoreInstance.collection(COLLECTION_NAME)
            .document(docName)
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .update(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                FAVORITE to favorite
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }else{
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .set(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                FAVORITE to favorite
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }
            }
    }

    suspend fun updateRate(
        userID: String,
        eventID: String,
        rate: Float,
        onSuccess: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        val docName = userID + "_" + eventID

        firestoreInstance.collection(COLLECTION_NAME)
            .document(docName)
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .update(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                RATE to rate
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }else{
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .set(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                RATE to rate
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }
            }
    }

    suspend fun updateReview(
        userID: String,
        eventID: String,
        review: String,
        onSuccess: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        val docName = userID + "_" + eventID

        firestoreInstance.collection(COLLECTION_NAME)
            .document(docName)
            .get()
            .addOnSuccessListener { document ->
                if (document != null){
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .update(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                REVIEW to review
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }else{
                    firestoreInstance.collection(COLLECTION_NAME)
                        .document(docName)
                        .set(
                            mapOf(
                                USERID_FIELD to userID,
                                EVENTID_FIELD to eventID,
                                REVIEW to review
                            )
                        )
                        .addOnSuccessListener { onSuccess.invoke(true) }
                        .addOnFailureListener { onSuccess.invoke(false) }
                }
            }
    }
}