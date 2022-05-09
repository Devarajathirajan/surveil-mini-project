package com.crayosa.surveil.datamodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lecture(
    val url : String,
    val name : String
) : Parcelable
