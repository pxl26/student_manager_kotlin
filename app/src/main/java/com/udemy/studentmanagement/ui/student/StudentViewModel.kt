package com.udemy.studentmanagement.ui.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.viewModel
import com.udemy.studentmanagement.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentViewModel  @Inject constructor(
    private val studentRepository: StudentRepository
) : viewModel() {

    //Data holder
    var oldStudent : Student? = null

    //Live Data to observe change for student's list
    private val _studentList = MutableLiveData<ArrayList<Student>>()
    val studentList : LiveData<ArrayList<Student>> = _studentList

    // Cập nhật danh sách học sinh liên tục
    private var job = viewModelScope.launch(Dispatchers.IO) {
        studentRepository.getAllStudents().collect() {
            _studentList.postValue(it)
        }
    }

    suspend fun addStudent(student : Student) : Boolean {
        return studentRepository.addStudent(student)
    }

    override suspend fun deleteItem(position : Int) {
        studentRepository.deleteStudent(_studentList.value!![position])
    }

    suspend fun updateStudentID(oldStudent: Student, newStudent: Student) : Boolean {
        return studentRepository.updateStudentID(oldStudent,newStudent)
    }

    suspend fun updateStudent(newStudent: Student) : Boolean {
        return studentRepository.updateStudent(newStudent)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

}

