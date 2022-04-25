package com.example.chattapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.chattapp.databinding.ActivityLoginScreenBinding
import com.example.chattapp.firebase.FirestoreUserDao

class LoginScreen : AppCompatActivity() {

    private lateinit var binder: ActivityLoginScreenBinding

    private lateinit var titles: Array<TextView>
    private lateinit var name: EditText
    private lateinit var lastname: EditText
    private lateinit var email: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var passwordConfirm: EditText

    private var isLogin = true
    private var checksOut = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binder.root)

        isLogin = intent.getBooleanExtra("loginPressed", true)

        titles = arrayOf(
            binder.titleTitle,
            binder.titleName,
            binder.titleLastname,
            binder.titleMail,
            binder.titlePasswordConfirm
        )
        name = binder.inputName
        lastname = binder.inputLastname
        email = binder.inputEMail
        username = binder.inputUsername
        password = binder.inputPassword
        passwordConfirm = binder.inputPasswordConfirm

        if (isLogin) {
            loginSelected()
        }

        binder.inputName.setOnClickListener {
            name.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputLastname.setOnClickListener {
            lastname.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener {
            email.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener {
            username.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPassword.setOnClickListener {
            password.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPasswordConfirm.setOnClickListener {
            password.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.buttonLogin.setOnClickListener {
            if (!isLogin) {
                isLogin = true
                loginSelected()
            } else {
                checksOut = true

                //checks username
                FirestoreUserDao.userExists(
                    username.text.toString(),
                ) { exists ->
                    if (username.text.toString() == "" || !exists) {
                        username.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        password.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        password.setText("")
                        checksOut = false
                    } else {
                        username.setBackgroundColor(resources.getColor(R.color.textInputBG))

                        //checks password only if username exists
                        FirestoreUserDao.checkPassword(
                            username.text.toString(),
                            password.text.toString()
                        ) { isCorrect ->
                            Log.d("login", "............................statusInLogin: $isCorrect")
                            if (password.text.toString() == "" || !isCorrect) {
                                password.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                                password.setText("")
                                checksOut = false
                            } else {
                                password.setBackgroundColor(resources.getColor(R.color.textInputBG))
                            }
                            if (checksOut) {
                                onBackPressed()
                                //startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                    }
                }

            }
        }

        binder.buttonRegister.setOnClickListener {
            if (isLogin) {
                isLogin = false
                registerSelected()
            } else {
                checksOut = true

                //checks name
                if (name.text.toString() == "") {
                    name.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    name.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks lastname
                if (lastname.text.toString() == "") {
                    lastname.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    lastname.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks passwords
                if (password.text.toString() == "" || !isLogin && passwordConfirm.text.toString() == "" || !isLogin && password.text.toString() != passwordConfirm.text.toString()) {
                    password.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    password.setText("")
                    passwordConfirm.setText("")
                    checksOut = false
                } else {
                    password.setBackgroundColor(resources.getColor(R.color.textInputBG))
                    passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks username
                FirestoreUserDao.userExists(
                    username.text.toString()
                ) { nameTaken ->
                    if (username.text.toString() == "" || nameTaken) {
                        username.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        checksOut = false
                    } else {
                        username.setBackgroundColor(resources.getColor(R.color.textInputBG))
                    }
                    //checks email
                    FirestoreUserDao.userExists(
                        username.text.toString()
                    ) { mailTaken ->
                        if (email.text.toString() == "" || !email.text.toString()
                                .contains("@") || mailTaken
                        ) {
                            email.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                            checksOut = false
                        } else {
                            email.setBackgroundColor(resources.getColor(R.color.textInputBG))
                        }
                        if (checksOut) {
                            FirestoreUserDao.addUser(
                                name.text.toString(),
                                lastname.text.toString(),
                                username.text.toString(),
                                email.text.toString(),
                                password.text.toString()
                            )
                            onBackPressed()
                            //startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            }
        }
    }

    private fun loginSelected() {
        titles[0].text = resources.getString(R.string.login_title)
        for (i in 1 until titles.size) {
            titles[i].visibility = View.GONE
        }
        name.visibility = View.GONE
        lastname.visibility = View.GONE
        email.visibility = View.GONE
        passwordConfirm.visibility = View.GONE
        setBackgroundGrey()
        clearInputs()
    }

    private fun registerSelected() {
        titles[0].text = resources.getString(R.string.register_title)
        for (i in 1 until titles.size) {
            titles[i].visibility = View.VISIBLE
        }
        name.visibility = View.VISIBLE
        lastname.visibility = View.VISIBLE
        email.visibility = View.VISIBLE
        passwordConfirm.visibility = View.VISIBLE
        setBackgroundGrey()
        clearInputs()
    }

    private fun setBackgroundGrey() {
        name.setBackgroundColor(resources.getColor(R.color.textInputBG))
        lastname.setBackgroundColor(resources.getColor(R.color.textInputBG))
        email.setBackgroundColor(resources.getColor(R.color.textInputBG))
        username.setBackgroundColor(resources.getColor(R.color.textInputBG))
        password.setBackgroundColor(resources.getColor(R.color.textInputBG))
        passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
    }

    private fun clearInputs() {
        name.setText("")
        lastname.setText("")
        email.setText("")
        username.setText("")
        password.setText("")
        passwordConfirm.setText("")
    }
}