package com.example.chatappfirebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseUtil {
    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun allUserCollectionReference(): CollectionReference? {
            return FirebaseFirestore.getInstance().collection("users")
        }

        fun getChatCollectionReference(chatroomId: String): CollectionReference {
            return getChatroomReference(chatroomId)!!.collection("chats")
        }

        fun getChatroomReference(chatroomId: String?): DocumentReference? {
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId!!)
        }

        fun getChatroomId(userId1: String, userId2: String): String {
            return if (userId1.hashCode() < userId2.hashCode()) {
                userId1 + "_" + userId2
                } else {
                userId2 + "_" + userId1
            }
        }

        fun userLogin(email: String, password: String): Task<AuthResult> {
            return FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email,
                password
            )
        }
    }
}