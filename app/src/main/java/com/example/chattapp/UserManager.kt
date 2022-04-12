package com.example.chattapp

import android.content.SharedPreferences
import com.example.chattapp.models.User
import com.google.firebase.firestore.FirebaseFirestore


object UserManager {

    var currentUser: User? = null
    val USERNAME_LOGIN_KEY = "eMail"
    val PASSWORD_LOGIN_KEY = "Password"

    //TEMP
    private val ID_KEY = "id"
    private val USERNAME_KEY = "user_name"
    private val EMAIL_KEY = "e_mail"

    private lateinit var sp: SharedPreferences

    fun sharedPrefsSetup(sp: SharedPreferences){

        this.sp = sp

    }

    fun saveUserLogin(userName: String, pw: String){

        val editor = sp.edit()
        editor.putString(USERNAME_LOGIN_KEY, userName)
        editor.putString(PASSWORD_LOGIN_KEY, pw)
        editor.apply()

    }

    fun loadUserLogin(): Boolean{

        val loadUsername = sp.getString(USERNAME_LOGIN_KEY, "null")
        val loadPw = sp.getString(PASSWORD_LOGIN_KEY, "null")

        if (loadUsername != "null" || loadPw != "null"){

            val fb = FirebaseFirestore.getInstance()
            fb.collection("users").whereEqualTo("username", loadUsername).get()
                .addOnSuccessListener {

                    if (it != null){

                        for (doc in it){

                            val user = User()
                            user.id = doc.getString(ID_KEY)!!
                            user.userName = doc.getString(USERNAME_KEY)!!
                            user.eMail = doc.getString(EMAIL_KEY)!!
                            currentUser = user
                            println("VICTORY")

                        }
                    }
                }
                .addOnFailureListener {
                    println("FUCK")
                }

            return true

        }else{

            return false
        }

    }

}