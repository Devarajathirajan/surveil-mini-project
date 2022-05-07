package com.crayosa.surveil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.crayosa.surveil.R
import com.crayosa.surveil.databinding.FragmentLecturesBinding

class LecturesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLecturesBinding>(inflater,R.layout.fragment_lectures, container, false)
        return binding.root
    }


}