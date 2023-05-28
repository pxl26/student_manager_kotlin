package com.udemy.studentmanagement.database

import androidx.core.net.toUri
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

//This class acts as database for the whole application
object StudentFirebase {

    private var firebase = FirebaseFirestore.getInstance()
        .collection(FirebaseCollection.User)
        .document(User.id)
        .collection(FirebaseCollection.Student)

    //Database to store student's avatar
    private val storage = FirebaseStorage.getInstance()

    // Data holder
    val studentList : Flow<ArrayList<Student>> = callbackFlow {
        val listener = firebase.addSnapshotListener { value, error ->
            if (error != null) {
                cancel("Đã xảy ra lỗi, vui lòng khởi động lại ứng dụng", error)
                return@addSnapshotListener
            }
            value?.let {
                val result = ArrayList<Student>()
                for (document in value) {
                    result.add(document.toObject(Student::class.java))
                }
                trySend(result)
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun deleteStudent(student : Student) : Boolean {
        return try {
            firebase.document(student.ID).delete().await()
            ClassFirebase.deleteStudentInClass(student)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addStudent(student : Student) : Boolean {
        //Update the imageUri to the download URL in the firebase
        student.imageUri?.let {
            val reference = storage.reference.child("${student.ID}.jpg")
            reference.putFile(it.toUri()).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri ->
                    student.imageUri = uri.toString()
                    firebase.document(student.ID).update("imageUri",student.imageUri)
                }
            }
        }

        return try {
            firebase.document(student.ID).set(student).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateStudentID(oldStudent : Student, newStudent : Student) : Boolean {
        return try {
            ClassFirebase.updateStudentID(oldStudent, newStudent)
            firebase.document(oldStudent.ID).delete().await()
            firebase.document(newStudent.ID).set(newStudent).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateStudent(newStudent : Student) : Boolean {
        return try {
            firebase.document(newStudent.ID).set(newStudent).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getStudentByID(ID : String) : Student {
        val result = firebase.document(ID).get().await()
        return if (result.exists())
            result.toObject(Student::class.java)!!
        else Student()
    }

    fun setClassForStudent(studentID : String, className : String) {
        firebase.document(studentID).update("className",className)
    }

    fun removeClassForStudent(studentID : String) {
        firebase.document(studentID).update("className",null)
    }

    fun updateClassNameForStudent(id: String, newClassName: String?) {
        firebase.document(id).update("className", newClassName)
    }

    fun updateSubjectName(subjectOldName: String, subjectNewName: String) {
        firebase.get().addOnSuccessListener {
            for (document in it) {
                val student = document.toObject(Student::class.java)

                // Lấy bảng điểm 2 học kỳ
                val transcript = student.transcript

                if (transcript != null) {
                    val transcript1 = transcript["${subjectOldName}/1"]
                    val transcript2 = transcript["${subjectOldName}/2"]

                    if (transcript1 != null) {
                        transcript["${subjectNewName}/1"] = transcript1
                        transcript.remove("${subjectOldName}/1")
                    }
                    if (transcript2 != null) {
                        transcript["${subjectNewName}/2"] = transcript2
                        transcript.remove("${subjectOldName}/2")
                    }
                    firebase.document(student.ID).update("transcript",transcript)
                }
            }
        }
    }

    fun searchStudent(query: String) : Flow<ArrayList<Student>> = callbackFlow {
        val result = arrayListOf<Student>()
        firebase.get().addOnSuccessListener { studentList ->
            if (studentList == null || studentList.isEmpty)
                return@addOnSuccessListener
            for (document in studentList) {
                val student = document.toObject(Student::class.java)
                if (student.ID.contains(query) || student.name.contains(query)) {
                    result.add(student)
                }
            }
            trySend(result)
        }

        awaitClose {
            cancel()
        }
    }

}