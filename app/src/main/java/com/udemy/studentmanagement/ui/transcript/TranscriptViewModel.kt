package com.udemy.studentmanagement.ui.transcript

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.Transcript
import com.udemy.studentmanagement.model.viewModel
import com.udemy.studentmanagement.repository.TranscriptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TranscriptViewModel @Inject constructor(
    private val transcriptRepository: TranscriptRepository
) : viewModel() {

    //Live Data to observe change for transcript's list
    private val _studentList = MutableLiveData<ArrayList<Student>>()
    val studentList : LiveData<ArrayList<Student>> = _studentList

    //Live Data to observe change for class's list
    private val _classList = MutableLiveData<ArrayList<String>>()
    val classList : LiveData<ArrayList<String>> = _classList

    //Live Data to observe change for subject's list
    private val _subjectList = MutableLiveData<ArrayList<String>>()
    val subjectList : LiveData<ArrayList<String>> = _subjectList

    var selectedStudent = Student()
    var selectedTranscript = Transcript()
    var currentSemester = String()
    var currentClass = String()
    var currentSubject = String()

    init {
        viewModelScope.launch {
            transcriptRepository.getAllClasses().collect() {
                _classList.postValue(it as ArrayList<String>?)
            }
        }
        viewModelScope.launch {
            transcriptRepository.getAllSubjects().collect() {
                _subjectList.postValue(it as ArrayList<String>?)
            }
        }
    }

    suspend fun getRequestedTranscript() = withContext(Dispatchers.IO) {
        transcriptRepository.getRequestedTranscript(currentClass, currentSubject, currentSemester).collect() {
            _studentList.postValue(it)
        }
    }

    override suspend fun deleteItem(position: Int) {
        return
    }

    suspend fun updateTranscript(grade15 : String, grade45: String, semesterGrade : String) : Boolean {
        return if ( transcriptRepository.updateTranscript(
                currentSubject, currentSemester, selectedStudent.ID,
                grade15.toDouble(), grade45.toDouble(), semesterGrade.toDouble()) ) {

            selectedTranscript = Transcript(currentSubject,currentSemester,grade15.toDouble(),grade45.toDouble(),semesterGrade.toDouble())

            _studentList.value?.get(_studentList.value!!.indexOf(selectedStudent))?.transcript?.set("${currentSubject}/${currentSemester}",
                selectedTranscript
            )

            _studentList.postValue(_studentList.value)
            true
            } else {
                false
        }
    }

}