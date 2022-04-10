package com.crayosa.surveil.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.crayosa.surveil.LoginActivity
import com.crayosa.surveil.R
import com.crayosa.surveil.adapters.ClassRoomListAdapter
import com.crayosa.surveil.databinding.FragmentHomeBinding
import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        if (auth.currentUser != null) {
            val photoUrl = auth.currentUser!!.photoUrl
            Glide.with(requireContext()).load(photoUrl).into(binding.displayPic)
            binding.displayPic.setOnClickListener {
                auth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            val adapter = ClassRoomListAdapter()
            lifecycleScope.launch {
                FirebaseRepository(Firebase.firestore)
                    .getEnrolledRooms(auth.currentUser!!.uid).collectLatest {
                        adapter.submitList(it)
                    }
            }


            binding.classRoomList.adapter = adapter
            binding.createRoom.setOnClickListener {
                binding.createFab.visibility = View.VISIBLE
                binding.joinFab.visibility = View.VISIBLE
            }
            binding.createFab.setOnClickListener {
                requireView().findNavController()
                    .navigate(R.id.action_homeFragment_to_createClassRoom)
            }
            binding.joinFab.setOnClickListener {
                requireView().findNavController()
                    .navigate(R.id.action_homeFragment_to_joinClassFragment)
            }
        }
        return binding.root
    }
    companion object{
        const val TAG = "HomeFragment"
    }
}