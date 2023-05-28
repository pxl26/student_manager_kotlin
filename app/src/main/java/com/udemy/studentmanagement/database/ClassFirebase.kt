package com.udemy.studentmanagement.database

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.udemy.studentmanagement.model.Class
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.Constraint
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object ClassFirebase {

    private val firebase = FirebaseFirestore.getInstance()
        .collection(FirebaseCollection.User)
        .document(User.id)
        .collection(FirebaseCollection.Class)

    suspend fun getAllClasses(): Flow<ArrayList<Class>> = callbackFlow {
        val listener = firebase.addSnapshotListener { value, error ->
            if (error != null) {
                cancel("Đã xảy ra lỗi, vui lòng khởi động lại ứng dụng", error)
                return@addSnapshotListener
            }

            val result = ArrayList<Class>()
            if (value == null || value.isEmpty)
            {
                for (classes in Constraint.NamesOfClass) {
                    firebase.document(classes).set(Class(classes, arrayListOf()))
                }
            }
            for (document in value!!) {
                result.add(document.toObject(Class::class.java))
            }
            trySend(result)
        }

        awaitClose {
            listener.remove()
        }
    }

    suspend fun addStudentToClass(selectedClass : Class, student: Student): Boolean {
        return try {
            StudentFirebase.setClassForStudent(student.ID,selectedClass.name)
            firebase.document(selectedClass.name).update("students", FieldValue.arrayUnion(student.ID)).await()
            true
        } catch (e : Exception) {
            false
        }
    }

    suspend fun removeStudentInClass(selectedClass : Class, id: String): Boolean {
        return try {
            StudentFirebase.removeClassForStudent(id)
            val updates = hashMapOf("students" to FieldValue.arrayRemove(id))
            firebase.document(selectedClass.name).update(updates as Map<String, Any>).await()
            true
        } catch (e : Exception) {
            false
        }
    }

    fun deleteStudentInClass(student : Student) {
        student.className?.let { firebase.document(it).update("students",FieldValue.arrayRemove(student.ID)) }
    }

    suspend fun getStudentInClass(className : String) : ArrayList<Student> {
        val task = firebase.document(className).get().await()
        return if (task.exists()) {
            val studentsID = task.get("students") as ArrayList<String>
            val result = ArrayList<Student>()
            for (Id in studentsID) {
                result.add(StudentFirebase.getStudentByID(Id))
            }
            result
        } else {
            arrayListOf()
        }
    }

    fun updateStudentID(oldStudent : Student, newStudent: Student) {
        oldStudent.className?.let { firebase.document(it).update("students",FieldValue.arrayRemove(oldStudent.ID)) }
        newStudent.className?.let { firebase.document(it).update("students",FieldValue.arrayUnion(newStudent.ID)) }
    }

    suspend fun updateClassName(oldClassName : String, newClassName : String) {

        //Lấy danh sách học sinh cũ trong lớp đó
        val result = firebase.document(oldClassName).get().await()
        val studentList = result.get("students") as ArrayList<String>? ?: arrayListOf()

        // Xóa cái cũ rồi viết tên mới vào do document trong firebase không thể sửa ID được
        firebase.document(oldClassName).delete().await()
        firebase.document(newClassName).set(Class(newClassName,studentList)).await()

        // Cập nhật tên lớp cho từng học sinh trong lớp đó
        val studentsInOldClass = getStudentInClass(newClassName)

        for (student in studentsInOldClass) {
            StudentFirebase.updateClassNameForStudent(student.ID, newClassName)
        }

    }

    fun createClass(newClass : String) {
        firebase.document(newClass).set(Class(newClass, arrayListOf()))
    }

    suspend fun deleteClass(deletedClass : String) {

        // Cập nhật tên lớp cho từng học sinh trong lớp đó
        val studentsInOldClass = getStudentInClass(deletedClass)

        for (student in studentsInOldClass) {
            StudentFirebase.updateClassNameForStudent(student.ID, null)
        }

        firebase.document(deletedClass).delete()

    }
}