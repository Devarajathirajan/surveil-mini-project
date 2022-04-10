package com.crayosa.surveil.repository

import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.datamodels.Users
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest


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
                                ).let {classroom ->
                                    result.add(classroom)
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

    fun addClassRoom(classroom : ClassRoom, user: Users){
        firestore.collection(CLASSROOM_COLLECTION).add(
            hashMapOf(
                NAME_STRING to classroom.name,
                SECTION_NAME to classroom.section_name,
                TEACHER_NAME to classroom.teacher_name,
                ENROLLED_MEMBERS to listOf(hashMapOf(
                    user.id to ROLE_ADMIN,
                    NAME_STRING to user.name
                ))
            )
        ).addOnSuccessListener {
            firestore.collection(USERS_COLLECTION).document(user.id!!)
                .update(ENROLLED_CLASSROOMS, FieldValue.arrayUnion(ClassRoom(it.id, classroom.section_name, classroom.name, classroom.teacher_name)))
        }

    }

    private fun getClassroom(cID: String) : Flow<ClassRoom?>{
        return callbackFlow {
            var classroom: ClassRoom?
            firestore.collection(CLASSROOM_COLLECTION).document(cID)
                .get().addOnSuccessListener {
                    val l =it.data as HashMap<String,String>
                     classroom = ClassRoom(
                        l["id"],
                        l["name"]!!,
                        l["section_name"]!!,
                        l["teacher_name"]!!
                    )
                    trySend(classroom).isSuccess
                }
            awaitClose {  }
        }
    }

    suspend fun joinClassRoom(cID : String, user: Users){
        getClassroom(cID).collectLatest {classroom ->
            if(classroom != null){
                firestore.collection(USERS_COLLECTION).document(user.id!!)
                    .update(ENROLLED_CLASSROOMS, FieldValue.arrayUnion(ClassRoom(cID, classroom.section_name, classroom.name, classroom.teacher_name)))
                firestore.collection(CLASSROOM_COLLECTION).document(cID)
                    .update(ENROLLED_MEMBERS, FieldValue.arrayUnion(
                        hashMapOf(
                            user.id to ROLE_STUDENT,
                            NAME_STRING to user.name
                    )))
            }
        }
    }

    companion object{
        const val ENROLLED_CLASSROOMS = "enrolled_classrooms"
        const val ROLE_ADMIN = 0
        const val ROLE_STUDENT = 1
        const val USERS_COLLECTION = "users"
        const val CLASSROOM_COLLECTION = "classroom"
        const val NAME_STRING = "name"
        const val SECTION_NAME = "section_name"
        const val TEACHER_NAME = "teacher_name"
        const val ENROLLED_MEMBERS = "members"
    }

}