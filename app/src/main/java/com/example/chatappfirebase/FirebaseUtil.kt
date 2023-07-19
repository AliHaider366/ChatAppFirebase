package com.example.chatappfirebase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class FirebaseUtil {
    companion object {
        fun currentUserId(): String? {
            return FirebaseAuth.getInstance().uid
        }

        fun getChatCollectionReference(name: String): CollectionReference {
            return FirebaseFirestore.getInstance().collection(name)
        }

        fun userLogin(email: String, password: String): Task<AuthResult> {
            return FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email,
                password
            )
        }
    }
}