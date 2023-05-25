package com.udemy.studentmanagement.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.udemy.studentmanagement.MainActivity
import com.udemy.studentmanagement.databinding.ActivityLoginBinding
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        // check current user
        if (hasLogin()) {
            getUserAndStartActivity()
        }

        loginButtonClick()
        signUpButtonClick()
    }

    private fun hasLogin(): Boolean {
        sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        // Check if the user's credentials are already stored in SharedPreferences
        val id = sharedPreferences.getString("id", null)
        val email = sharedPreferences.getString("email", null)
        return id != null && email != null
    }


    private fun getUserAndStartActivity() {
        User.email = sharedPreferences.getString("email", null)!!
        User.id = sharedPreferences.getString("id", null)!!
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loginButtonClick() {
        loginBinding.btnLogin.setOnClickListener {
            if (loginBinding.loginEmail.text.isEmpty() || loginBinding.loginPassword.text.isEmpty())
                Toast.makeText( applicationContext,
                    "Vui lòng nhập tên tài khoản hoặc mật khẩu của bạn",
                    Toast.LENGTH_LONG
                ).show()
            else {
                MainScope().launch {
                    setClick()
                }
            }
        }
    }

    private suspend fun setClick() {
        withContext(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(
                loginBinding.loginEmail.text.toString().trim(),
                loginBinding.loginPassword.text.toString().trim()
            ).addOnSuccessListener {
                with (sharedPreferences.edit()) {
                    putString("id", it.user?.uid)
                    putString("email", it.user?.email)
                    apply()
                }
                getUserAndStartActivity()
            }.addOnFailureListener {
                Toast.makeText(
                    baseContext, "Bạn đã nhập sai tên đăng nhập hoặc mật khẩu",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun signUpButtonClick() {
        loginBinding.tvSignup.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }
}