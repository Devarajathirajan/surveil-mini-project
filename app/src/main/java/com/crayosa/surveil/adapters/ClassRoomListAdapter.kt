package com.crayosa.surveil.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.crayosa.surveil.R
import com.crayosa.surveil.databinding.LayoutClassBinding
import com.crayosa.surveil.datamodels.ClassRoom

class ClassRoomListAdapter : ListAdapter<ClassRoom, ClassRoomViewHolder>(RoomDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassRoomViewHolder {
        val binding = DataBindingUtil
            .inflate<LayoutClassBinding>(LayoutInflater.from(parent.context), R.layout.layout_class,parent, false)
        return ClassRoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class RoomDiffUtil : DiffUtil.ItemCallback<ClassRoom>(){
    override fun areItemsTheSame(oldItem: ClassRoom, newItem: ClassRoom): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ClassRoom, newItem: ClassRoom): Boolean {
        return oldItem == newItem
    }

}


class ClassRoomViewHolder(private val binding : LayoutClassBinding) : RecyclerView.ViewHolder(binding.root){
    fun bind(classRoom : ClassRoom){
        binding.className.text = classRoom.name
        binding.courseName.text = classRoom.section_name
        binding.courseStaffName.text = classRoom.teacher_name
    }
}

