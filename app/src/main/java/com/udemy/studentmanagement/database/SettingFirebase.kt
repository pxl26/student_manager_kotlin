package com.udemy.studentmanagement.database

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.udemy.studentmanagement.model.Class
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.Constraint
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object SettingFirebase {

    private val firebase = FirebaseFirestore.getInstance()
        .collection(FirebaseCollection.Setting)
        .document(User.id)

    suspend fun setMinMaxAgeForStudent(studentMaxAge : Int, studentMinAge : Int) : Boolean {
        return try {
            firebase.update("studentMinAge", studentMinAge).await()
            firebase.update("studentMaxAge", studentMaxAge).await()
            Constraint.studentMaxAge = studentMaxAge
            Constraint.studentMinAge = studentMinAge
            true
        } catch (e : Exception) {
            false
        }
    }

    suspend fun setChangeForClass(
        classMaxSize: String, addClass: String, deleteClass : String,
        classOldName : String, classNewName: String): Boolean {
        return try {
            firebase.update("classMaxSize", classMaxSize.toInt()).await()
            if (addClass.isNotEmpty()) {
                firebase.update("namesOfClass", FieldValue.arrayUnion(addClass)).await()
                Constraint.NumberOfClass++
                Constraint.NamesOfClass.add(addClass)
                firebase.update("numberOfClass", Constraint.NumberOfClass)
                ClassFirebase.createClass(addClass)
            }
            if (deleteClass.isNotEmpty()) {
                firebase.update("namesOfClass", FieldValue.arrayRemove(deleteClass)).await()
                Constraint.NumberOfClass--
                Constraint.NamesOfClass.remove(deleteClass)
                firebase.update("numberOfClass", Constraint.NumberOfClass)
                ClassFirebase.deleteClass(deleteClass)
            }
            if (classNewName != "" && classOldName != deleteClass)  {
                Constraint.NamesOfClass[Constraint.NamesOfClass.indexOf(classOldName)] = classNewName
                firebase.update("namesOfClass", Constraint.NamesOfClass).await()
                ClassFirebase.updateClassName(classOldName, classNewName)
            }
            Constraint.classMaxSize = classMaxSize.toInt()
            true
        } catch (e : Exception) {
            Log.i("EXCEPTION FOR SETTING CLASS", e.toString())
            false
        }
    }

    suspend fun setChangeForSubject(addSubject: String, deleteSubject: String, subjectOldName: String, subjectNewName: String): Boolean {
        return try {
            if (addSubject.isNotEmpty()) {
                firebase.update("namesOfSubject", FieldValue.arrayUnion(addSubject)).await()
                Constraint.NumberOfSubject++
                Constraint.NamesOfSubject.add(addSubject)
                firebase.update("numberOfSubject", Constraint.NumberOfSubject)
            }
            if (deleteSubject.isNotEmpty()) {
                Log.i("DELETE SUBJECT",deleteSubject)
                firebase.update("namesOfSubject", FieldValue.arrayRemove(deleteSubject)).await()
                Constraint.NumberOfSubject--
                Constraint.NamesOfSubject.remove(deleteSubject)
                firebase.update("numberOfSubject", Constraint.NumberOfSubject)
            }
            if (subjectNewName != "")  {
                Constraint.NamesOfClass[Constraint.NamesOfClass.indexOf(subjectOldName)] = subjectNewName
                firebase.update("namesOfSubject", Constraint.NamesOfClass).await()
                StudentFirebase.updateSubjectName(subjectOldName, subjectNewName)
            }
            true
        } catch (e : Exception) {
            Log.i("EXCEPTION FOR SETTING CLASS", e.toString())
            false
        }
    }

    fun getAllSubject(): Flow<ArrayList<String>> = callbackFlow {
        val listener = firebase.addSnapshotListener { value, error ->
            if (error != null) {
                cancel("Đã xảy ra lỗi, vui lòng khởi động lại ứng dụng", error)
                return@addSnapshotListener
            }

            val result = value?.get("namesOfSubject")
            if (result != null)
                trySend(result as ArrayList<String>)
        }

        awaitClose {
            listener.remove()
        }
    }

    fun getAllClasses(): Flow<ArrayList<String>> = callbackFlow {
        val listener = firebase.addSnapshotListener { value, error ->
            if (error != null) {
                cancel("Đã xảy ra lỗi, vui lòng khởi động lại ứng dụng", error)
                return@addSnapshotListener
            }

            val result = value?.get("namesOfClass")
            if (result != null)
                trySend(result as ArrayList<String>)
        }

        awaitClose {
            listener.remove()
        }
    }


    suspend fun setChangeForAdmissionScore(admissionScore: Double) : Boolean {
        return try {
            firebase.update("admissionScore", admissionScore).await()
            Constraint.AdmissionScore = admissionScore
            true
        } catch (e : Exception)
        {
            false
        }
    }
}