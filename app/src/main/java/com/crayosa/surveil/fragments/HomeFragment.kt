package com.crayosa.surveil.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.crayosa.surveil.R
import com.crayosa.surveil.adapters.ClassRoomListAdapter
import com.crayosa.surveil.databinding.FragmentHomeBinding
import com.crayosa.surveil.datamodels.ClassRoom

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,R.layout.fragment_home,container,false)
        val list = mutableListOf<ClassRoom>(
            ClassRoom(0,"CSE IIIrd year", "Professional English", "Mr. Vivekanand N"),
            ClassRoom(1,"CSE IIIrd year", "Cyber Security", "Mr. Ezhumalai P")
        )
        val adapter = ClassRoomListAdapter()
        adapter.submitList(list)
        binding.classRoomList.adapter = adapter
        return binding.root
    }
}