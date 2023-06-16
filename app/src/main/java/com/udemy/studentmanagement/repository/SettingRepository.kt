package com.udemy.studentmanagement.repository

import com.udemy.studentmanagement.database.SettingFirebase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingRepository @Inject constructor(
    private val settingFirebase : SettingFirebase
) {

    suspend fun setMinMaxAgeForStudent(studentMaxAge : Int, studentMinAge : Int): Boolean {
        return settingFirebase.setMinMaxAgeForStudent(studentMaxAge ,studentMinAge)
    }

    suspend fun setChangeForClass(
        classMaxSize: String, addClass: String, deleteClass : String,
        classOldName : String, classNewName: String): Boolean {
        return settingFirebase.setChangeForClass(classMaxSize, addClass, deleteClass,classOldName, classNewName)
    }

    suspend fun setChangeForSubject(addSubject: String, deleteSubject: String,
                                    subjectOldName: String, subjectNewName: String): Boolean {
        return settingFirebase.setChangeForSubject(addSubject, deleteSubject, subjectOldName, subjectNewName)
    }

    fun getAllSubject(): Flow<ArrayList<String>> {
        return settingFirebase.getAllSubject()
    }

    suspend fun setChangeForAdmissionScore(admissionScore: Double) : Boolean {
        return settingFirebase.setChangeForAdmissionScore(admissionScore)
    }

}