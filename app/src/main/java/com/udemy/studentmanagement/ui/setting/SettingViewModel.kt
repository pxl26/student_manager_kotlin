package com.udemy.studentmanagement.ui.setting

import androidx.lifecycle.LiveData
import com.udemy.studentmanagement.util.Constraint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository : SettingRepository
) : ViewModel() {


    private val _subjectList = MutableLiveData<ArrayList<String>>()
    val subjectList : LiveData<ArrayList<String>> = _subjectList

    private var temporaryDeletedSubject = String()
    val withoutDeletedSubject = MutableLiveData<ArrayList<String>>()

    init {
        viewModelScope.launch {
            settingRepository.getAllSubject().collect() {
                _subjectList.postValue(it)
                withoutDeletedSubject.postValue(it)
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
        return settingRepository.setChangeForClass(classMaxSize, addClass, deleteClass, classOldName, classNewName)
    }

    suspend fun setChangeForSubject(
        addSubject: String, deleteSubject : String,
        subjectOldName: String, subjectNewName: String): Boolean {
        return settingRepository.setChangeForSubject(addSubject, deleteSubject, subjectOldName, subjectNewName)
    }

    fun deleteClassChosen(selectedDeleteClass : String) {
        if (temporaryDeletedSubject.isNotEmpty())
            withoutDeletedSubject.value?.add(temporaryDeletedSubject)
        temporaryDeletedSubject = selectedDeleteClass
        withoutDeletedSubject.value?.remove(selectedDeleteClass)
        withoutDeletedSubject.postValue(withoutDeletedSubject.value)
    }

    suspend fun setChangeForAdmissionScore(admissionScore: String) : Boolean {
        return settingRepository.setChangeForAdmissionScore(admissionScore.toDouble())
    }
}