package com.crayosa.surveil

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.crayosa.surveil.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val auth = FirebaseAuth.getInstance()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        if (auth.currentUser != null) {
            binding.noSignIn.visibility = View.GONE
            Firebase.firestore.collection("users").document(auth.currentUser!!.uid).let{ it ->
                it.get().addOnSuccessListener { snapshot ->
                    if(!snapshot.exists()){
                        it.set(mapOf("name" to auth.currentUser!!.displayName))
                    }
                }
            }
        }
        binding.signInButton.setOnClickListener {
            startActivity(Intent(baseContext,LoginActivity::class.java))
        }
    }
}