package com.udemy.studentmanagement.ui.student

import androidx.core.view.isEmpty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.viewModel
import com.udemy.studentmanagement.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    init {
        getAllStudents()
    }

    private fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            studentRepository.getAllStudents().collect() {
                _studentList.postValue(it)
            }
        }
    }

    suspend fun addStudent(student : Student) : Boolean {
        if (student.name.isEmpty() || student.ID.isEmpty() || student.address.isEmpty() || student.email.isEmpty()
            || student.birthDate.isEmpty() || student.gender.isEmpty())
            return false
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

}

