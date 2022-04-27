package com.example.chattapp

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.Window
import android.widget.Button

object RegLogSelection {
    fun selectOption(context: Context, callback: (Boolean) -> Unit) {
        val createWindow = Dialog(context)
        createWindow.requestWindowFeature(Window.FEATURE_NO_TITLE)
        createWindow.setContentView(R.layout.reg_log_selection_activity)
        createWindow.setCancelable(false)

        val registerButton = createWindow.findViewById<Button>(R.id.button_register)
        val loginButton = createWindow.findViewById<Button>(R.id.button_login)

        Log.d("login", "............................select Option!!!")
        registerButton.setOnClickListener {
            callback(false)
            createWindow.dismiss()
        }
        loginButton.setOnClickListener {
            callback(true)
            createWindow.dismiss()
        }
        createWindow.show()
    }
}