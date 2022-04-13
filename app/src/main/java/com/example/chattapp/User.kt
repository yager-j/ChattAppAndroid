package com.example.chattapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class User : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    var first_name: String = "dummy"
    var last_name: String = "user"

    @Required
    var username: String = "user:$id"

    @Required
    var email: String = "user@chatApp.com"

    var password: String = "password"

    var loggedIn: Boolean = false

}