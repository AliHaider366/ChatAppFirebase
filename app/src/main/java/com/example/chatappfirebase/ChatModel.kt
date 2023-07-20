package com.example.chatappfirebase

import com.google.firebase.firestore.FieldValue


class ChatModel {
    var message: String? = null
    var senderId: String? = null
    var timestamp: FieldValue? = null

    constructor()

    constructor(message: String?, senderId: String?, timestamp: FieldValue?) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
    }
}

