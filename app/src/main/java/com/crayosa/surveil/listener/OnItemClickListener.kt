package com.crayosa.surveil.listener

import com.crayosa.surveil.datamodels.ClassRoom

interface OnItemClickListener {
    fun onClick(classroom : ClassRoom)
}