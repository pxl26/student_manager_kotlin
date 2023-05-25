package com.udemy.studentmanagement.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.udemy.studentmanagement.MainActivity
import com.udemy.studentmanagement.databinding.ActivitySignupBinding
import com.udemy.studentmanagement.model.SettingResponse
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupBinding: ActivitySignupBinding
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var fireStore : DocumentReference

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(signupBinding.root)

        signUpButtonClick()
        logInButtonClick()
    }

    private fun logInButtonClick() {
        signupBinding.tvLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private fun signUpButtonClick() {
        signupBinding.btnSignup.setOnClickListener {
            MainScope().launch {
                setClick()
            }
        }
    }

    private suspend fun setClick() {
        withContext(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(
                signupBinding.signupEmail.text.toString().trim(),
                signupBinding.signupPassword.text.toString().trim()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Lay ten dang nhap va email roi luu vao database
                    User.email = signupBinding.signupEmail.text.toString().trim()
                    User.name = signupBinding.signupName.text.toString().trim()
                    User.id = task.result.user?.uid ?: ""
                    fireStore = FirebaseFirestore.getInstance().collection(FirebaseCollection.User)
                        .document(User.id)
                    fireStore.set(User)
                    sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE)
                    with (sharedPreferences.edit()) {
                        putString("id",  User.id)
                        putString("email", User.email)
                        apply()
                    }

                    // Tạo ra các thông tin mặc định của ứng dụng
                    val basicSetting = SettingResponse(
                        15,
                        20,
                        40,
                        9,
                        arrayListOf("10A1","10A2","10A3","10A4","11A1","11A2","11A3","12A1","12A2"),
                        9,
                        arrayListOf("Toán","Lý","Hóa","Sinh","Sử","Địa","Văn","Đạo đức","Thể dục"),
                        5.00
                    )
                    fireStore = FirebaseFirestore.getInstance().collection(FirebaseCollection.Setting)
                        .document(User.id)
                    fireStore.set(basicSetting)

                    startActivity(Intent(this@SignUpActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        baseContext, "Đã có lỗi xảy ra, vui lòng thử lại !",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.i("ERROR MESSAGE",task.exception.toString())
                }
            }
        }
    }
}