package com.example.chattapp

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.okhttp.internal.Internal.instance
import io.realm.Realm


class UserDao {

    val USERS_COLLECTIONS = "users"
    val USERNAME_KEY = "username"
    val EMAIL_KEY = "email"

    val db: Realm = Realm.getDefaultInstance()
    val fb : FirebaseFirestore = FirebaseFirestore.getInstance()
    val usersCol: CollectionReference = fb.collection(USERS_COLLECTIONS)
    //val ref : DatabaseReference = fb.collection(USERS_COLLECTIONS).document("id")


    fun getUsers(): ArrayList<User> {
        val userList = ArrayList<User>()
        userList.addAll(db.where(User::class.java).findAllAsync())
        return userList
    }

    fun addUser(first: String, last: String, user: String, mail: String, pw: String, login: Boolean) {
        db.executeTransactionAsync {
            val newUser = User().apply {
                first_name = first
                last_name = last
                username = user
                email = mail
                password = pw
                loggedIn = login
            }
            it.insert(newUser)
        }
    }

    fun addUserToFirebase(first: String, last: String, user: String, mail: String, pw: String) {
        val newUser = User().apply{
            first_name = first
            last_name = last
            username = user
            email = mail
            password = pw
        }
        val hashUser = HashMap<String, String>()
        hashUser["id"] = newUser.id
        hashUser["first_name"] = newUser.first_name
        hashUser["last_name"] = newUser.last_name
        hashUser["username"] = newUser.username
        hashUser["email"] = newUser.email
        hashUser["password"] = newUser.password
        fb.document("$USERS_COLLECTIONS/${newUser.id}").set(hashUser)
    }

    fun deleteUser(userId: String) {
        db.executeTransaction {
            val user = db.where(User::class.java).equalTo("id", userId).findFirstAsync()
            user?.deleteFromRealm()
        }
    }

    private fun getOneUser(userOrMail: String): Query {
        val query: Query = if (userOrMail.contains("@")){
            usersCol.whereEqualTo(USERNAME_KEY, userOrMail)
        } else {
            usersCol.whereEqualTo(EMAIL_KEY, userOrMail)
        }
        return query
    }

    fun checkIfUserExists(userOrMail: String): Boolean {
        /*
        val query: Query = if (userOrMail.contains("@")){
            usersCol.whereEqualTo(USERNAME_KEY, userOrMail)
        } else {
            usersCol.whereEqualTo(EMAIL_KEY, userOrMail)
        }
         */
        return getOneUser(userOrMail).get() != null
    }

    fun checkPassword(userOrMail: String, password: String): Boolean {
        val query: Query = usersCol.whereEqualTo(USERNAME_KEY, userOrMail)
        query
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }


        val user = if (userOrMail.contains("@")) {
            db.where(User::class.java).equalTo(EMAIL_KEY, userOrMail).findFirst()
        } else {
            db.where(User::class.java).equalTo(USERNAME_KEY, userOrMail).findFirst()
        }
        return user?.password == password
    }

    fun logInUser(userOrMail : String) {
        val user = if (userOrMail.contains("@")) {
            db.where(User::class.java).equalTo(EMAIL_KEY, userOrMail).findFirst()
        } else {
            db.where(User::class.java).equalTo(USERNAME_KEY, userOrMail).findFirst()
        }
        user?.loggedIn = true
    }

    fun logOutUser() {
        val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
        user?.loggedIn = false
    }

    fun displayCurrentUser() : String {
        val user = db.where(User::class.java).equalTo("loggedIn", true).findFirst()
        return user?.username ?: ""
    }

    fun debugShowUser(username : String){
        val user = db.where(User::class.java).equalTo(USERNAME_KEY, username).findFirst()
        Log.d("LoginUser", "user:$user")
    }
}