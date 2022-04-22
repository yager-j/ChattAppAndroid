package com.example.chattapp.firebase

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class StorageManager {


    var storage = Firebase.storage.reference

    fun getImageURL(userId: String): StorageReference {
        val imageRef = storage.child("images/$userId")

        return imageRef

    }
}