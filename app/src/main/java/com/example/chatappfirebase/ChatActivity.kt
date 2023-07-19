package com.example.chatappfirebase

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappfirebase.databinding.ActivityChatBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        binding.imageSendButton.setOnClickListener {
            if (binding.editTextMessage.text.toString() != null) {
                sendMessageToUser(binding.editTextMessage.text.toString())
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val query = FirebaseUtil.getChatCollectionReference("chats")
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query, ChatModel::class.java)
                .setLifecycleOwner(this)
                .build()

        var layout = LinearLayoutManager(this@ChatActivity)
        layout.reverseLayout = true
        binding.recyclerView.layoutManager = layout
        chatAdapter = ChatAdapter(options)
        binding.recyclerView.adapter = chatAdapter
        chatAdapter.startListening()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.recyclerView.smoothScrollToPosition(0)
            }
        })
    }

    private fun sendMessageToUser(message: String) {
        val chatModel = ChatModel(message, FirebaseUtil.currentUserId().toString(), Timestamp.now())
        FirebaseUtil.getChatCollectionReference("chats").add(chatModel).addOnCompleteListener {
            if (it.isSuccessful) {
                binding.editTextMessage.setText("")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        chatAdapter.stopListening()
    }
}