package com.crayosa.surveil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.crayosa.surveil.R
import com.crayosa.surveil.adapters.ProgressListAdapter
import com.crayosa.surveil.databinding.FragmentReportBinding
import com.crayosa.surveil.repository.FirebaseRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReportFragment : Fragment() {
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentReportBinding>(
            inflater, R.layout.fragment_report, container, false
        )
        val args : ReportFragmentArgs by navArgs()

        val adapter = ProgressListAdapter()
        lifecycleScope.launch {
          FirebaseRepository(Firebase.firestore).getProgressList(args.cID, args.lID).collectLatest {
            adapter.submitList(it)
          }
        }

          binding.progressList.adapter = adapter
        return binding.root
    }
}