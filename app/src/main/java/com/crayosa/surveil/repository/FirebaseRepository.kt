package com.crayosa.surveil.repository

import android.util.Log
import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.datamodels.Lecture
import com.crayosa.surveil.datamodels.Members
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
                .addOnSuccessListener {
                    val data = it.data?.get(ENROLLED_CLASSROOMS)
                    if (data != null) {
                        val list = data as MutableList<HashMap<String,String>>?
                        if (list != null) {
                            for(l in list){
                                ClassRoom(
                                    l[FIELD_ID],
                                    l[FIELD_NAME]!!,
                                    l[FIELD_SECTION_NAME]!!,
                                    l[FIELD_TEACHERS_NAME]!!,
                                    l[FIELD_COLOR]!!,
                                    l[FIELD_GENDER]!!
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
                    ROLE_FIELD to ROLE_ADMIN,
                    NAME_STRING to classroom.teacher_name,
                    USER_ID to user.id!!
                )),
                FIELD_COLOR to classroom.color,
                FIELD_GENDER to classroom.gender
            )
        ).addOnSuccessListener {
            firestore.collection(USERS_COLLECTION).document(user.id)
                .update(ENROLLED_CLASSROOMS, FieldValue
                    .arrayUnion(ClassRoom(it.id, classroom.section_name, classroom.name,
                        classroom.teacher_name, classroom.color, classroom.gender)))
        }

    }

    private fun getClassroom(cID: String) : Flow<ClassRoom?>{
        return callbackFlow {
            var classroom: ClassRoom?
            firestore.collection(CLASSROOM_COLLECTION).document(cID)
                .get().addOnSuccessListener {
                    Log.d(TAG,cID)
                    if(it.data != null) {
                        val l = it.data as HashMap<String, String>
                        classroom = ClassRoom(
                            l[FIELD_ID],
                            l[FIELD_NAME]!!,
                            l[FIELD_SECTION_NAME]!!,
                            l[FIELD_TEACHERS_NAME]!!,
                            l[FIELD_COLOR]!!,
                            l[FIELD_GENDER]!!
                        )
                        trySend(classroom).isSuccess
                    }
                }
            awaitClose {  }
        }
    }

    suspend fun getClassRoomMembers(cID: String) : Flow<List<Members>> {
        return callbackFlow {
            val list = mutableListOf<Members>()
            firestore.collection(CLASSROOM_COLLECTION).document(cID)
                .get().addOnSuccessListener {
                    if(it.data != null){
                        val data = it.data as  HashMap<String,*>
                        val members = data[ENROLLED_MEMBERS] as List<HashMap<String,*>>
                        for(m in members ){
                            list.add(Members(
                               m[NAME_STRING]!!.toString(),
                               m[ROLE_FIELD]!! as Long,
                                m[USER_ID]!!.toString()
                            ))
                        }
                        trySend(list)
                    }
                }
            awaitClose {  }
        }
    }

    suspend fun joinClassRoom(cID : String, user: Users){
        getClassroom(cID).collectLatest {classroom ->
            if(classroom != null){
                firestore.collection(USERS_COLLECTION).document(user.id!!)
                    .update(ENROLLED_CLASSROOMS,
                        FieldValue.arrayUnion(ClassRoom(cID, classroom.section_name, classroom.name,
                            classroom.teacher_name, classroom.color, classroom.gender)))
                firestore.collection(CLASSROOM_COLLECTION).document(cID)
                    .update(ENROLLED_MEMBERS, FieldValue.arrayUnion(
                        hashMapOf(
                            ROLE_FIELD to ROLE_STUDENT,
                            NAME_STRING to user.name,
                            USER_ID to user.id
                    )))
            }
        }
    }

    fun addLectures(lecture: Lecture, cID : String){
        firestore.collection(CLASSROOM_COLLECTION).document(cID)
            .update(LECTURES_FIELD, FieldValue.arrayUnion(lecture))
    }

    fun isAdmin(cID: String, uID: String) : Flow<Boolean> {
        return callbackFlow {
            var result = false
            firestore.collection(CLASSROOM_COLLECTION).document(cID)
                .get().addOnSuccessListener {
                    if (it.data != null) {
                        val data = it.data as HashMap<String, *>
                        val members = data[ENROLLED_MEMBERS] as List<HashMap<String, *>>
                        for (m in members) {
                            if (m[USER_ID]!!.toString() == uID) {
                                result = true
                            }
                        }
                        trySend(result)
                    }
                }
            awaitClose{}
        }
    }

    suspend fun getLectures(cID : String) : Flow<MutableList<Lecture>>{
        return callbackFlow {
            val list = mutableListOf<Lecture>()
            Log.d(TAG,"HEllo")
            firestore.collection(CLASSROOM_COLLECTION).document(cID)
                .get().addOnSuccessListener {
                    if(it.data != null){
                        Log.d(TAG,"Fuuuu")
                        val data = it.data as  HashMap<String,*>
                        val members = data[LECTURES_FIELD] as List<HashMap<String,*>>
                        for(m in members ){
                            list.add(
                                Lecture(
                                m[LECTURE_URL]!!.toString(),
                                m[NAME_STRING].toString()
                            )
                            )
                        }
                        trySend(list)
                    }
                }
            awaitClose {  }
        }
    }


    companion object{
        const val ENROLLED_CLASSROOMS = "enrolled_classrooms"
        const val ROLE_ADMIN = 0
        const val ROLE_STUDENT = 1
        const val ROLE_FIELD = "role"
        const val USERS_COLLECTION = "users"
        const val CLASSROOM_COLLECTION = "classroom"
        const val NAME_STRING = "name"
        const val SECTION_NAME = "section_name"
        const val TEACHER_NAME = "teacher_name"
        const val ENROLLED_MEMBERS = "members"

        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_SECTION_NAME = "section_name"
        const val FIELD_TEACHERS_NAME = "teacher_name"
        const val FIELD_COLOR = "color"
        const val FIELD_GENDER = "gender"
        const val USER_ID =  "uid"
        const val LECTURES_FIELD = "Lectures"
        const val LECTURE_URL = "url"

        const val TAG = "FirebaseRepo"
    }

}