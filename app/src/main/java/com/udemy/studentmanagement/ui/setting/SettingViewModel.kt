package com.udemy.studentmanagement.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import com.udemy.studentmanagement.util.Constraint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository : SettingRepository
) : ViewModel() {


    private val _subjectList = MutableLiveData<ArrayList<String>>()
    val subjectList : LiveData<ArrayList<String>> = _subjectList

    private val _classList = MutableLiveData<ArrayList<String>>()
    val classList : LiveData<ArrayList<String>> = _classList

    init {
        viewModelScope.launch {
            settingRepository.getAllSubject().collect() {
                _subjectList.postValue(it)
            }
        }
        viewModelScope.launch {
            settingRepository.getAllClass().collect() {
                _classList.postValue(it)
            }
        }
    }
    suspend fun setMinMaxAgeForStudent(studentMaxAge : String, studentMinAge : String) : Boolean {
        val studentMaxAgeInt = studentMaxAge.toInt()
        val studentMinAgeInt = studentMinAge.toInt()
        if (studentMaxAgeInt < studentMinAgeInt) return false
        return settingRepository.setMinMaxAgeForStudent(studentMaxAgeInt, studentMinAgeInt)
    }

    suspend fun setChangeForClass(
        classMaxSize: String, addClass: String, deleteClass : String,
        classOldName: String, classNewName: String): Boolean {
        var emptyString = deleteClass
        if (emptyString == "Không chọn")
            emptyString = ""
        return settingRepository.setChangeForClass(classMaxSize, addClass, emptyString, classOldName, classNewName)
    }

    suspend fun setChangeForSubject(
        addSubject: String, deleteSubject : String,
        subjectOldName: String, subjectNewName: String): Boolean {
        var emptyString = deleteSubject
        if (emptyString == "Không chọn")
            emptyString = ""
        return settingRepository.setChangeForSubject(addSubject, emptyString, subjectOldName, subjectNewName)
    }


    suspend fun setChangeForAdmissionScore(admissionScore: String) : Boolean {
        return settingRepository.setChangeForAdmissionScore(admissionScore.toDouble())
    }
}