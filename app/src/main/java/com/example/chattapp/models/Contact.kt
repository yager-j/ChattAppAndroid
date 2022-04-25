package com.example.chattapp.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class Contact : RealmObject() {

    @PrimaryKey
    var id = UUID.randomUUID().toString()

    @Required
    var name: String = "dummy"

    @Required
    var lastName: String = "user"

    @Required
    var userName: String = "user:$id"

    @Required
    var eMail: String = "user@chatApp.com"
}