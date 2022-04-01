package com.example.chattapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chattapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var binder : ActivityMainBinding
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binder = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binder.root)

        val db = Firebase.firestore
        val user = hashMapOf(

            "first" to "Luca",
            "last" to "Salmi",
            "username" to "SonOfChtulu"
        )
        val user2 = hashMapOf(
            "first" to "Heimir",
            "last" to "Kristmundsson",
            "username" to "originalJared"
        )

        val user3 = hashMapOf(

            "first" to "Rita",
            "last" to "Salmi",
            "username" to "criminalgirll"
        )
        saveToDb(db, user)
        saveToDb(db, user2)
        saveToDb(db, user3)


        binder.buttonAdd.setOnClickListener {

            username = binder.enterUserName.text.toString()

            db.collection("users3")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener {
                        if (!it.isEmpty){

                            for (doc in it){

                                Toast.makeText(this, "user : ${doc.data["first"]}", Toast.LENGTH_SHORT).show()
                            }

                        }
                }
                .addOnFailureListener{
                    Log.v(TAG, "error retrieving")
                }
        }
    }

    fun saveToDb(db: FirebaseFirestore,user: HashMap<String, String>){

        db.collection("users3")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

    }
}