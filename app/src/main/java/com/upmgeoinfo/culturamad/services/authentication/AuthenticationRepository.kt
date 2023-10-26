package com.upmgeoinfo.culturamad.services.authentication

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthenticationRepository {
    val currentUser: FirebaseUser? = Firebase.auth.currentUser

    fun hasUser(): Boolean = Firebase.auth.currentUser != null && !Firebase.auth.currentUser!!.isAnonymous

    suspend fun getCurrentUserMail(): String = withContext(Dispatchers.IO){
        return@withContext Firebase.auth.currentUser?.email.orEmpty()
    }

    suspend fun signupAnonymously(
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth.signInAnonymously()
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }.await()
    }

    suspend fun createUser(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) onComplete.invoke(true)
                else onComplete.invoke(false)
            }.await()
    }

    suspend fun logIn(
        email: String,
        password: String,
        onComplete: (Boolean) -> Unit
    ) = withContext(Dispatchers.IO){
        Firebase.auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) onComplete.invoke(true)
                else onComplete.invoke(false)
            }.await()
    }

    suspend fun logOut() = withContext(Dispatchers.IO){
        Firebase.auth.signOut()
    }
}