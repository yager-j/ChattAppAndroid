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
import com.example.chattapp.models.User

class LoginScreen : AppCompatActivity() {

    private lateinit var binder: ActivityLoginScreenBinding

    private lateinit var titlesET: Array<TextView>
    private lateinit var nameET: EditText
    private lateinit var lastnameET: EditText
    private lateinit var emailET: EditText
    private lateinit var usernameET: EditText
    private lateinit var passwordET: EditText
    private lateinit var passwordConfirmET: EditText

    private var isLogin = true
    private var checksOut = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binder.root)

        isLogin = intent.getBooleanExtra("loginPressed", true)

        titlesET = arrayOf(
            binder.titleTitle,
            binder.titleName,
            binder.titleLastname,
            binder.titleMail,
            binder.titlePasswordConfirm
        )
        nameET = binder.inputName
        lastnameET = binder.inputLastname
        emailET = binder.inputEMail
        usernameET = binder.inputUsername
        passwordET = binder.inputPassword
        passwordConfirmET = binder.inputPasswordConfirm

        if (isLogin) {
            loginSelected()
        }

        binder.inputName.setOnClickListener {
            nameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputLastname.setOnClickListener {
            lastnameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener {
            emailET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener {
            usernameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPassword.setOnClickListener {
            passwordET.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirmET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPasswordConfirm.setOnClickListener {
            passwordET.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirmET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.buttonLogin.setOnClickListener {
            if (!isLogin) {
                isLogin = true
                loginSelected()
            } else {
                checksOut = true

                //checks username
                FirestoreUserDao.userExists(
                    usernameET.text.toString(),
                ) { exists ->
                    if (usernameET.text.toString() == "" || !exists) {
                        usernameET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        passwordET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        passwordET.setText("")
                        checksOut = false
                    } else {
                        usernameET.setBackgroundColor(resources.getColor(R.color.textInputBG))

                        //checks password only if username exists
                        FirestoreUserDao.checkPassword(
                            usernameET.text.toString(),
                            passwordET.text.toString()
                        ) { isCorrect ->
                            Log.d("login", "............................statusInLogin: $isCorrect")
                            if (passwordET.text.toString() == "" || !isCorrect) {
                                passwordET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                                passwordET.setText("")
                                checksOut = false
                            } else {
                                passwordET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                            }
                            if (checksOut) {
                                startActivity(Intent(this, MainActivity::class.java))
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
                if (nameET.text.toString() == "") {
                    nameET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    nameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks lastname
                if (lastnameET.text.toString() == "") {
                    lastnameET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    lastnameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks passwords
                if (passwordET.text.toString() == "" || !isLogin && passwordConfirmET.text.toString() == "" || !isLogin && passwordET.text.toString() != passwordConfirmET.text.toString()) {
                    passwordET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    passwordConfirmET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    passwordET.setText("")
                    passwordConfirmET.setText("")
                    checksOut = false
                } else {
                    passwordET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                    passwordConfirmET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks username
                FirestoreUserDao.userExists(
                    usernameET.text.toString()
                ) { nameTaken ->
                    if (usernameET.text.toString() == "" || nameTaken) {
                        usernameET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        checksOut = false
                    } else {
                        usernameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                    }
                    //checks email
                    FirestoreUserDao.userExists(
                        usernameET.text.toString()
                    ) { mailTaken ->
                        if (emailET.text.toString() == "" || !emailET.text.toString()
                                .contains("@") || mailTaken
                        ) {
                            emailET.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                            checksOut = false
                        } else {
                            emailET.setBackgroundColor(resources.getColor(R.color.textInputBG))
                        }
                        if (checksOut) {
                            val newUser = User().apply {
                                first_name = nameET.text.toString()
                                last_name = lastnameET.text.toString()
                                username = usernameET.text.toString()
                                email = emailET.text.toString()
                                password = passwordET.text.toString()
                            }
                            FirestoreUserDao.addUser(newUser)
                            FirestoreUserDao.saveToManager(newUser)
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                    }
                }
            }
        }
    }

    private fun loginSelected() {
        titlesET[0].text = resources.getString(R.string.login_title)
        for (i in 1 until titlesET.size) {
            titlesET[i].visibility = View.GONE
        }
        nameET.visibility = View.GONE
        lastnameET.visibility = View.GONE
        emailET.visibility = View.GONE
        passwordConfirmET.visibility = View.GONE
        setBackgroundGrey()
        clearInputs()
    }

    private fun registerSelected() {
        titlesET[0].text = resources.getString(R.string.register_title)
        for (i in 1 until titlesET.size) {
            titlesET[i].visibility = View.VISIBLE
        }
        nameET.visibility = View.VISIBLE
        lastnameET.visibility = View.VISIBLE
        emailET.visibility = View.VISIBLE
        passwordConfirmET.visibility = View.VISIBLE
        setBackgroundGrey()
        clearInputs()
    }

    private fun setBackgroundGrey() {
        nameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        lastnameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        emailET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        usernameET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        passwordET.setBackgroundColor(resources.getColor(R.color.textInputBG))
        passwordConfirmET.setBackgroundColor(resources.getColor(R.color.textInputBG))
    }

    private fun clearInputs() {
        nameET.setText("")
        lastnameET.setText("")
        emailET.setText("")
        usernameET.setText("")
        passwordET.setText("")
        passwordConfirmET.setText("")
    }
}