package com.example.chattapp.firebase

import android.net.Uri
import com.example.chattapp.UserManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

object ImageManager {


    var storage = Firebase.storage.reference

    fun getImageURL(userId: String): StorageReference {
        val imageRef = storage.child("images/$userId")
        return imageRef
    }

    fun saveImage(imageUri: Uri){
        val storageRef = storage.child("images/${UserManager.currentUser?.id}")
        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                println("Uploaded image to firebase")
            }.addOnFailureListener {
                println("Upload image Failed")
            }
    }
}