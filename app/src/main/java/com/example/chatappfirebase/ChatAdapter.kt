package com.example.chatappfirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatappfirebase.databinding.ItemRowBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ChatAdapter(options: FirestoreRecyclerOptions<ChatModel>) : FirestoreRecyclerAdapter<ChatModel, ChatAdapter.ChatViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int, model: ChatModel) {
        holder.bind(model)
    }

    inner class ChatViewHolder(private val binding : ItemRowBinding) : ViewHolder(binding.root){

        fun bind(chatMessage: ChatModel){

            if(chatMessage.senderId == FirebaseUtil.currentUserId().toString()) {
                binding.leftChatLayout.visibility = View.GONE
                binding.rightChatLayout.visibility = View.VISIBLE
                binding.rightChatMessage.text = chatMessage.message
            }else{
                binding.rightChatLayout.visibility = View.GONE
                binding.leftChatLayout.visibility = View.VISIBLE
                binding.leftChatMessage.text = chatMessage.message
            }
        }

    }

}