package com.example.chattapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.chattapp.R.string.email
import com.example.chattapp.databinding.ActivityLoginScreenBinding
import com.example.chattapp.realm.UserDao

class LoginScreen : AppCompatActivity() {

    private lateinit var userDao: UserDao
    private lateinit var contactDao: ContactDao
    private lateinit var binder : ActivityLoginScreenBinding

    private lateinit var titles : Array<TextView>
    private lateinit var name : EditText
    private lateinit var lastname : EditText
    private lateinit var email : EditText
    private lateinit var username : EditText
    private lateinit var password : EditText
    private lateinit var passwordConfirm : EditText

    private var isLogin = true
    private var checksOut = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binder = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binder.root)
        userDao = UserDao()
        contactDao = ContactDao()

        isLogin = intent.getBooleanExtra("loginPressed", true)

        titles = arrayOf(binder.titleUsername, binder.titleTitle, binder.titleName, binder.titleLastname, binder.titleMail, binder.titlePasswordConfirm)
        name = binder.inputName
        lastname = binder.inputLastname
        email = binder.inputEMail
        username = binder.inputUsername
        password = binder.inputPassword
        passwordConfirm = binder.inputPasswordConfirm

        if (isLogin){
            loginSelected()
        }

        binder.inputName.setOnClickListener{
            name.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputLastname.setOnClickListener{
            lastname.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener{
            email.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputUsername.setOnClickListener{
            username.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPassword.setOnClickListener{
            password.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.inputPasswordConfirm.setOnClickListener{
            password.setBackgroundColor(resources.getColor(R.color.textInputBG))
            passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
        }

        binder.buttonLogin.setOnClickListener{
            if (!isLogin){
                isLogin = true
                loginSelected()
            } else {
                checksOut = true

                //checks username
                if (username.text.toString() == "" || !userDao.checkIfUserExists(username.text.toString())) {
                    username.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    password.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    password.setText("")
                    checksOut = false
                } else {
                    username.setBackgroundColor(resources.getColor(R.color.textInputBG))

                    //checks password only if username exists
                    if (password.text.toString() == "" || !userDao.checkPassword(username.text.toString(), password.text.toString())) {
                        password.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                        password.setText("")
                        checksOut = false
                    } else {
                        password.setBackgroundColor(resources.getColor(R.color.textInputBG))
                    }
                }

                //Log in user
                if (checksOut) {
                    userDao.logInUser(username.text.toString())
                    val toMain = Intent(this, MainActivity::class.java)
                    startActivity(toMain)
                }
            }
        }

        binder.buttonRegister.setOnClickListener{
            if (isLogin){
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

                //checks username
                if (username.text.toString() == "" || userDao.checkIfUserExists(username.text.toString()) || username.text.toString().contains("@")) {
                    username.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    username.setBackgroundColor(resources.getColor(R.color.textInputBG))
                }

                //checks email
                if (email.text.toString() == "" || !email.text.toString().contains("@") || userDao.checkIfUserExists(username.text.toString())) {
                    email.setBackgroundColor(resources.getColor(R.color.textInputBGNotFilled))
                    checksOut = false
                } else {
                    email.setBackgroundColor(resources.getColor(R.color.textInputBG))
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

                //Adds user and corresponding contact
                if (checksOut) {
                    //userDao.addUser(name.text.toString(), lastname.text.toString(), username.text.toString(), email.text.toString(), password.text.toString(), true)
                    userDao.addUserToFirebase(name.text.toString(), lastname.text.toString(), username.text.toString(), email.text.toString(), password.text.toString())
                    //userDao.logInUser(username.text.toString())
                    //contactDao.addContact(username.text.toString())
                    val toMain = Intent(this, MainActivity::class.java)
                    startActivity(toMain)
                }
            }
        }
    }

    private fun loginSelected() {
        titles[0].text = "${resources.getString(R.string.username)} or e-${resources.getString(R.string.email)}"
        titles[1].text = resources.getString(R.string.login_title)
        for (i in 2 until titles.size) {
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
        titles[0].text = resources.getString(R.string.username)
        titles[1].text = resources.getString(R.string.register_title)
        for (i in 2 until titles.size) {
            titles[i].visibility = View.VISIBLE
        }
        name.visibility = View.VISIBLE
        lastname.visibility = View.VISIBLE
        email.visibility = View.VISIBLE
        passwordConfirm.visibility = View.VISIBLE
        setBackgroundGrey()
        clearInputs()
    }

    private fun setBackgroundGrey(){
        name.setBackgroundColor(resources.getColor(R.color.textInputBG))
        lastname.setBackgroundColor(resources.getColor(R.color.textInputBG))
        email.setBackgroundColor(resources.getColor(R.color.textInputBG))
        username.setBackgroundColor(resources.getColor(R.color.textInputBG))
        password.setBackgroundColor(resources.getColor(R.color.textInputBG))
        passwordConfirm.setBackgroundColor(resources.getColor(R.color.textInputBG))
    }

    private fun clearInputs(){
        name.setText("")
        lastname.setText("")
        email.setText("")
        username.setText("")
        password.setText("")
        passwordConfirm.setText("")
    }
}