package com.crayosa.surveil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.crayosa.surveil.R
import com.crayosa.surveil.databinding.FragmentClassRoomBinding


class ClassRoomFragment : Fragment() {

    val args : ClassRoomFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentClassRoomBinding>(inflater, R.layout.fragment_class_room, container, false)
        binding.displayClassroomId.text = args.classroom.id
        return binding.root
    }

}