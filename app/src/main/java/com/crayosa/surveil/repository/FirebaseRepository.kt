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

    fun getEnrolledRooms(uID: String) : Flow<MutableList<ClassRoom?>>{
        return callbackFlow {
            val result : MutableList<ClassRoom?> = mutableListOf()
            firestore.collection(USERS_COLLECTION).document(uID).get()
                .addOnSuccessListener { it ->
                    val data = it.data?.get(ENROLLED_CLASSROOMS)
                    if (data != null) {
                        val list = data as MutableList<HashMap<String,String>>?
                        if (list != null) {
                            for(l in list){
                                ClassRoom(
                                    l["id"],
                                    l["name"]!!,
                                    l["section_name"]!!,
                                    l["teacher_name"]!!
                                ).let {
                                    result.add(it)
                                }
                            }
                        }
                    }
                    else{
                        return@addOnSuccessListener
                    }
                    trySend(result).isSuccess
                }

            awaitClose {  }
        }
    }

    fun addClassRoom(classroom : ClassRoom, uID: String){
        firestore.collection(CLASSROOM_COLLECTION).add(
            classroom
        ).addOnSuccessListener {
            firestore.collection(USERS_COLLECTION).document(uID)
                .update(ENROLLED_CLASSROOMS, FieldValue.arrayUnion(ClassRoom(it.id, classroom.section_name, classroom.name, classroom.teacher_name)))
        }

    }
    companion object{
        const val ENROLLED_CLASSROOMS = "enrolled_classrooms"
        const val USERS_COLLECTION = "users"
        const val CLASSROOM_COLLECTION = "classroom"
    }

}