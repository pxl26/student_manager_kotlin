package com.udemy.studentmanagement.ui.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udemy.studentmanagement.repository.SummaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.udemy.studentmanagement.model.Summary

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val summaryRepository: SummaryRepository
) : ViewModel() {

    //Live Data to observe change for subject's list
    private val _subjectList = MutableLiveData<ArrayList<String>>()
    val subjectList : LiveData<ArrayList<String>> = _subjectList


    //Live Data to observe change for class's list
    private val _summaryList = MutableLiveData<ArrayList<Summary>>()
    val summaryList : LiveData<ArrayList<Summary>> = _summaryList

    //Live Data to observe change for class's list
    private val _summaryList2 = MutableLiveData<ArrayList<Summary>>()
    val summaryList2 : LiveData<ArrayList<Summary>> = _summaryList2

    init {
        viewModelScope.launch {
            summaryRepository.getAllSubjects().collect() {
                _subjectList.postValue(it as ArrayList<String>?)
            }
        }
    }

    suspend fun showRequestedSummary(selectedSubject : String, selectedSemester : String) {
        _summaryList.postValue(summaryRepository.showRequestedSummary(selectedSubject, selectedSemester))
    }

    suspend fun showRequestedSummary2(selectedSemester : String) {
        val requestedSummary = summaryRepository.showRequestedSummary2(selectedSemester)
        _summaryList2.postValue(requestedSummary)
    }
}