package com.crayosa.surveil.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.crayosa.surveil.R
import com.crayosa.surveil.adapters.LecturesListAdapter
import com.crayosa.surveil.databinding.FragmentLecturesBinding
import com.crayosa.surveil.datamodels.ClassRoom
import com.crayosa.surveil.datamodels.Lecture
import com.crayosa.surveil.fragments.viewmodels.LecturesViewModel
import com.crayosa.surveil.listener.OnItemClickListener
import com.google.firebase.auth.FirebaseAuth
import java.lang.IllegalArgumentException

class LecturesFragment : Fragment() {
    private val args : LecturesFragmentArgs by navArgs()
    private val user = FirebaseAuth.getInstance().currentUser!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLecturesBinding>(inflater,R.layout.fragment_lectures, container, false)
        val adapter = LecturesListAdapter(object : OnItemClickListener(){
             override fun onClick(lecture: Lecture) {
                requireActivity().findViewById<View>(
                    R.id.main_frag
                ).findNavController().navigate(ClassRoomFragmentDirections.actionClassRoomFragmentToPlayerFragment(lecture))
            }
        })

        val viewModel : LecturesViewModel by viewModels{LecturesVMFactory(
            requireActivity().application,
            args.classroom.id!!,
            user.uid
        )}

        viewModel.lectureList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        viewModel.isAdmin.observe(viewLifecycleOwner){
            binding.addLecture.visibility = when(it){true->View.VISIBLE false->View.GONE}
        }

        binding.lectureList.adapter = adapter
        binding.addLecture.setOnClickListener {
            binding.root.findNavController().navigate(LecturesFragmentDirections.actionLecturesFragmentToAddLectureFragment(args.classroom))
        }
        return binding.root
    }
}

class LecturesVMFactory(val app : Application, val id: String, private val uid : String) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LecturesViewModel::class.java))
            return LecturesViewModel(app, id, uid) as T
        throw IllegalArgumentException("Unknown ViewModel")
    }

}