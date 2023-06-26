package com.udemy.studentmanagement.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udemy.studentmanagement.model.SearchResult
import com.udemy.studentmanagement.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    //Live Data to observe change for student's list
    private val _searchResultList = MutableLiveData<ArrayList<SearchResult>>()
    val searchResultList : LiveData<ArrayList<SearchResult>> = _searchResultList

    suspend fun searchStudent(query: String) {
        studentRepository.searchStudent(query).collect() { studentList ->
            val result = arrayListOf<SearchResult>()
            for (student in studentList) {
                var transcript1 = 0.00
                var transcript2 = 0.00
                var numberOfTranscript1 = 0
                var numberOfTranscript2 = 0
                student.transcript?.forEach { it ->
                    if (it.value.semester == "1") {
                        transcript1 += (it.value.grade15 + it.value.grade45 + it.value.semesterGrade) / 3
                        numberOfTranscript1++
                    } else {
                        transcript2 += (it.value.grade15 + it.value.grade45 + it.value.semesterGrade) / 3
                        numberOfTranscript2++
                    }
                }
                if (numberOfTranscript1 != 0)
                    transcript1 /= numberOfTranscript1
                if (numberOfTranscript2 != 0)
                    transcript2 /= numberOfTranscript2
                transcript1 = round(transcript1 * 10.0) / 10.0
                transcript2 = round(transcript2 * 10.0) / 10.0
                val searchResult = SearchResult(student.imageUri, student.name, student.className, transcript1, transcript2)
                result.add(searchResult)
            }
            _searchResultList.postValue(result)
        }
    }

}