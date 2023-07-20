package com.example.chatappfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatappfirebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var otherUserId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        FirebaseUtil.userLogin(
            binding.etEmailAddress.text.toString(),
            binding.etPassword.text.toString()
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                getOtherUserId()
            } else {
                Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getOtherUserId() {
        FirebaseUtil.allUserCollectionReference()!!.get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (document in it) {
                    // Access the data in each document
                    val data = document.data
                    if (!data.values.contains(FirebaseUtil.currentUserId())) {
                        otherUserId = data.values.toString().substring(1, data.values.toString().length - 1)
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ChatActivity::class.java)
                        intent.putExtra("otherid", otherUserId)
                        startActivity(intent)
                    }
                }
            }
        }

    }
}