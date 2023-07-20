package com.example.chatappfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappfirebase.databinding.ActivityChatBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var otherUserId: String

    private lateinit var chatRoomId : String

    private var chatroomModel : ChatroomModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.imageSendButton.setOnClickListener {
            if (binding.editTextMessage.text.toString() != null) {
                sendMessageToUser(binding.editTextMessage.text.toString())
            }
        }
        otherUserId = intent.getStringExtra("otherid")!!
        chatRoomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId().toString(), otherUserId)
        getOrCreateChatroomModel()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val query = FirebaseUtil.getChatCollectionReference(chatRoomId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<ChatModel>().setQuery(query, ChatModelSnapshotParser(ChatModel::class.java))
                .setLifecycleOwner(this)
                .build()

        var layout = LinearLayoutManager(this@ChatActivity)
        layout.reverseLayout = true
        binding.recyclerView.layoutManager = layout
        chatAdapter = ChatAdapter(options)
        binding.recyclerView.adapter = chatAdapter
        chatAdapter.startListening()
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.recyclerView.smoothScrollToPosition(0)
                chatAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun sendMessageToUser(message: String) {

        chatroomModel!!.lastMessageTimestamp = FieldValue.serverTimestamp()
        chatroomModel!!.lastMessageSenderId = FirebaseUtil.currentUserId()
        chatroomModel!!.lastMessage = message
        FirebaseUtil.getChatroomReference(chatRoomId)!!.set(chatroomModel!!)
        binding.editTextMessage.setText("")

        val chatModel = ChatModel(message, FirebaseUtil.currentUserId().toString(), null)
        chatModel.timestamp = FieldValue.serverTimestamp()
        FirebaseUtil.getChatCollectionReference(chatRoomId).add(chatModel).addOnCompleteListener {
            if (it.isSuccessful) {
            }
        }
    }

    private fun getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatRoomId)!!.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                chatroomModel = ChatroomModel()
                val data = task.result.data
                if (data != null) {
                    //first time chat
                    chatroomModel = ChatroomModel(
                        chatRoomId,
                        listOf(FirebaseUtil.currentUserId(), otherUserId),
                        null,
                        ""
                    )
                    chatroomModel!!.lastMessageTimestamp = FieldValue.serverTimestamp()
                    FirebaseUtil.getChatroomReference(chatRoomId)!!.set(chatroomModel!!)
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        chatAdapter.stopListening()
    }
}