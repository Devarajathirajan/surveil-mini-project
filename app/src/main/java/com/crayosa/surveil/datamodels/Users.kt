package com.crayosa.surveil.datamodels

import com.google.firebase.firestore.DocumentId

data class Users(
    @DocumentId
    val id : String,
    val name : String,
)
