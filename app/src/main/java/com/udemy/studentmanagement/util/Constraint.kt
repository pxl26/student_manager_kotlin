package com.udemy.studentmanagement.util

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.udemy.studentmanagement.model.SettingResponse
import com.udemy.studentmanagement.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Constraint {
    var studentMinAge : Int = 0
    var studentMaxAge : Int = 0
    var classMaxSize : Int = 0
    var NumberOfClass : Int = 0
    var NamesOfClass : ArrayList<String> = arrayListOf()
    var NumberOfSubject : Int = 0
    var NamesOfSubject : ArrayList<String> = arrayListOf()
    var AdmissionScore : Double = 0.00

    suspend fun fetchData() = withContext(Dispatchers.IO) {
        FirebaseFirestore.getInstance()
            .collection(FirebaseCollection.Setting)
            .document(User.id).get().addOnSuccessListener {
                if (it.data != null && it.data!!.isNotEmpty())
                {
                    convertToObject(it)
                }
            }
    }

    private fun convertToObject(it: DocumentSnapshot) {
        val response = it.toObject(SettingResponse::class.java)
        studentMaxAge = response!!.studentMaxAge
        studentMinAge = response.studentMinAge
        classMaxSize = response.classMaxSize
        NumberOfClass = response.NumberOfClass
        NamesOfClass = response.NamesOfClass
        NumberOfSubject = response.NumberOfSubject
        NamesOfSubject = response.NamesOfSubject
        AdmissionScore = response.AdmissionScore
    }
}