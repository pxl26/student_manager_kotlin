package com.udemy.studentmanagement.repository

import com.udemy.studentmanagement.database.SummaryFirebase
import com.udemy.studentmanagement.database.TranscriptFirebase
import com.udemy.studentmanagement.model.Summary
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SummaryRepository @Inject constructor(
    private val summaryFirebase : SummaryFirebase,
    private val transcriptFirebase : TranscriptFirebase
) {

    suspend fun getAllSubjects(): Flow<List<String>> {
        return transcriptFirebase.getAllSubjects()
    }

    suspend fun showRequestedSummary(selectedSubject: String, selectedSemester: String) : ArrayList<Summary> {
        return summaryFirebase.showRequestedSummary(selectedSubject, selectedSemester)
    }

    suspend fun showRequestedSummary2(selectedSemester: String) : ArrayList<Summary> {
        return summaryFirebase.showRequestedSummary2(selectedSemester)
    }
}