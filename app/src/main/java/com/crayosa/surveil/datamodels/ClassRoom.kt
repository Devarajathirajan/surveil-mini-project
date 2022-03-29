package com.crayosa.surveil.datamodels

import com.google.firebase.firestore.DocumentId

data class ClassRoom(
    @DocumentId
    val id : String?,
    val name : String,
    val section_name :  String,
    val teacher_name : String,
    val members : List<Users> //user ids
)
