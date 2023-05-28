package com.udemy.studentmanagement.repository

import com.udemy.studentmanagement.database.ClassFirebase
import com.udemy.studentmanagement.database.StudentFirebase
import com.udemy.studentmanagement.model.Class
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ClassRepository @Inject constructor(
    private val studentFirebase : StudentFirebase,
    private val classFirebase : ClassFirebase
) {

    suspend fun getAllClasses() : Flow<ArrayList<Class>> {
        return classFirebase.getAllClasses()
    }

    fun getAllStudents(): Flow<ArrayList<Student>> {
        return studentFirebase.studentList
    }

    suspend fun addStudentToClass(selectedClass : Class, student: Student): Boolean {
        return classFirebase.addStudentToClass(selectedClass, student)
    }

    suspend fun removeStudentInClass(selectedClass : Class, ID : String): Boolean {
        return classFirebase.removeStudentInClass(selectedClass, ID)
    }

}