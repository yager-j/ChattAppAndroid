package com.example.chattapp

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class User : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    var userName: String = "user $id"

    @Required
    var eMail: String = "user@chatApp.com"

    @Required
    var password: String = "password"

}