package com.crayosa.surveil

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.crayosa.surveil.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        if (auth.currentUser != null) {
            val photoUrl = auth.currentUser!!.photoUrl
            Glide.with(baseContext).load(photoUrl).into(binding.displayPic)
            binding.displayPic.setOnClickListener {
                auth.signOut()
                onBackPressed()
            }
        }
    }
}