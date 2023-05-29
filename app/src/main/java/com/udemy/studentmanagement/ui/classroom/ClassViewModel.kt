package com.udemy.studentmanagement.ui.classroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.model.Class
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.repository.ClassRepository
import com.udemy.studentmanagement.util.Constraint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class ClassViewModel @Inject constructor(
    private val classRepository: ClassRepository
) : ViewModel() {

    //Live Data to observe change for class's list
    private val _classList = MutableLiveData<ArrayList<Class>>()
    val classList : LiveData<ArrayList<Class>> = _classList

    //Live Data to observe change for student of the selected class
    private val _studentOfSelectedClass = MutableLiveData<ArrayList<Student>>()
    val studentOfSelectedClass : LiveData<ArrayList<Student>> = _studentOfSelectedClass

    //the whole student
    private var _studentList = ArrayList<Student>()

    //Get the chosen class
    var selectedClass = Class()

    init {
        _studentOfSelectedClass.postValue(ArrayList())
        // Lấy dữ liệu ban đầu để xây dựng UI
        viewModelScope.launch {
            classRepository.getAllClasses().collect {
                _classList.postValue(it)
            }
        }
        viewModelScope.launch {
            classRepository.getAllStudents().collect {
                _studentList = it
            }
        }

    }

    // Lấy học sinh mà chưa thuộc về lớp học nào
     fun getStudentsWithoutClass(): ArrayList<Student> {
         val result = _studentList
             .filter {
                 var hasClass = false
                 for (_class in classList.value!!)
                     if (_class.students.contains(it.ID)) {
                         hasClass = true
                         break
                     }
                 !hasClass
             }
         return if (result.isEmpty()) ArrayList() else ArrayList(result)
    }

    //Lấy học sinh của một lớp học được chọn
    fun getStudentsOfSelectedClass() {
        val arrayList : ArrayList<Student> = ArrayList( _studentList
            .filter {
                selectedClass.students.contains(it.ID)
            })
        if (arrayList.isEmpty())
            _studentOfSelectedClass.postValue(ArrayList())
        else
            _studentOfSelectedClass.postValue(arrayList)
    }


    // Thêm học sinh vào một lớp
    suspend fun addStudentToClass(student: Student) : Int {
        if (selectedClass.students.size > Constraint.classMaxSize)
            return -1

        return if (classRepository.addStudentToClass(selectedClass, student)) {
            _studentOfSelectedClass.value?.add(student) // Thêm học sinh đó vào lớp hiện tại đang chọn
            _studentOfSelectedClass.postValue(_studentOfSelectedClass.value)

            // Thêm ID học sinh đó vào lớp học hiện tại
            selectedClass.students.add(student.ID)
            1
        } else 0
    }

    suspend fun removeStudentInClass(position: Int) {
        if (classRepository.removeStudentInClass(
                selectedClass,
                _studentOfSelectedClass.value!![position].ID)
        ) {
            val id = _studentOfSelectedClass.value!![position].ID
            _studentOfSelectedClass.value!!.removeAt(position)
            selectedClass.students.remove(id)

            // Thông báo cho UI biết dữ liệu thay đổi
            _studentOfSelectedClass.postValue(_studentOfSelectedClass.value)
        }
    }

}