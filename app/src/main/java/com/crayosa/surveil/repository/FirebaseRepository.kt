package com.crayosa.surveil.repository

import com.crayosa.surveil.datamodels.ClassRoom
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseRepository(private val firestore: FirebaseFirestore){
    fun getEnrolledRooms(uID : String) : List<String>{
        lateinit var result : MutableList<String>
         firestore.collection(USERS_COLLECTION).document(uID).get()
            .addOnSuccessListener {
                result = it.data?.get(ENROLLED_CLASSROOMS) as MutableList<String>
            }
        return emptyList()
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