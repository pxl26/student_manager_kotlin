package com.udemy.studentmanagement.repository

import com.udemy.studentmanagement.database.StudentFirebase
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

open class StudentRepository @Inject constructor(
    private val studentFirebase : StudentFirebase,
) {

    open fun getAllStudents() : Flow<ArrayList<Student>> {
        return studentFirebase.studentList
    }

    suspend fun deleteStudent(student : Student) : Boolean {
        return studentFirebase.deleteStudent(student)
    }

    suspend fun addStudent(student : Student) : Boolean {
        return studentFirebase.addStudent(student)
    }

    suspend fun updateStudentID(oldStudent : Student, newStudent : Student) : Boolean {
        return studentFirebase.updateStudentID(oldStudent, newStudent)
    }

    suspend fun updateStudent(newStudent : Student) : Boolean {
        return studentFirebase.updateStudent(newStudent)
    }

    fun searchStudent(query : String) : Flow<ArrayList<Student>> {
        return studentFirebase.searchStudent(query)
    }
}