package com.crayosa.surveil.datamodels

import com.google.firebase.firestore.DocumentId

data class Users(
    @DocumentId
    val id : String,
    val name : List<String> //id roomName subjectName staffName
)
