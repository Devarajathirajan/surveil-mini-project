package com.crayosa.surveil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.crayosa.surveil.R
import com.crayosa.surveil.databinding.FragmentCreateClassRoomBinding
import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateClassRoom : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentCreateClassRoomBinding>(
            layoutInflater, R.layout.fragment_create_class_room, container, false
        )
        binding.submitClass.setOnClickListener {
            FirebaseRepository(Firebase.firestore)
                .addClassRoom(
                    ClassRoom(null, binding.roomName.text.toString(), binding.roomSectionName
                        .text.toString(),binding.roomFacultyName.text.toString()),
                    FirebaseAuth.getInstance().currentUser!!.uid
                )
            requireView().findNavController().navigateUp()

        }

        return binding.root
    }
}