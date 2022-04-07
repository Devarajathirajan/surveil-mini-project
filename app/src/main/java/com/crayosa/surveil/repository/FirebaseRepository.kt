package com.crayosa.surveil.repository

import android.util.Log
import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.fragments.HomeFragment
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class FirebaseRepository(private val firestore: FirebaseFirestore){

    fun getEnrolledRooms(uID: String) : Flow<MutableList<String>>{
        return callbackFlow<MutableList<String>>{
            var result : MutableList<String>? = mutableListOf()
            firestore.collection(USERS_COLLECTION).document(uID).get()
                .addOnSuccessListener {
                    val data = it.data?.get(ENROLLED_CLASSROOMS)
                    if (data != null) {
                        result = data as MutableList<String>
                        Log.d(HomeFragment.TAG, result.toString())
                    }
                    else{
                        return@addOnSuccessListener
                    }
                    trySend(result!!).isSuccess
                }

            awaitClose {  }
        }
    }

    fun addClassRoom(classroom : ClassRoom, uID: String){
        firestore.collection(CLASSROOM_COLLECTION).add(
            classroom
        ).addOnSuccessListener {
            firestore.collection(USERS_COLLECTION).document(uID)
                .update(ENROLLED_CLASSROOMS, FieldValue.arrayUnion(it.id))
        }

    }
    companion object{
        const val ENROLLED_CLASSROOMS = "enrolled_classrooms"
        const val USERS_COLLECTION = "users"
        const val CLASSROOM_COLLECTION = "classroom"
    }

}