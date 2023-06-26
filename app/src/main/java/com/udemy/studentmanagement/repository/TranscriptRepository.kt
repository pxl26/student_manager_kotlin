package com.udemy.studentmanagement.repository

import com.udemy.studentmanagement.database.ClassFirebase
import com.udemy.studentmanagement.database.TranscriptFirebase
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranscriptRepository @Inject constructor(
    private val classFirebase : ClassFirebase,
    private val transcriptFirebase : TranscriptFirebase
) {

    // Lấy tên của lớp học
    suspend fun getAllClasses() : Flow<List<String>> {
        return classFirebase.getAllClasses().map { list ->
            list.map {
                it.name
            }
        }
    }

    suspend fun getAllSubjects(): Flow<List<String>> {
        return transcriptFirebase.getAllSubjects()
    }

    suspend fun getRequestedTranscript(selectedClass: String,
                                       selectedSubject: String,
                                       selectedSemester: String) : Flow<ArrayList<Student>> {
        return transcriptFirebase.getRequestedTranscript(selectedClass, selectedSubject, selectedSemester)
    }

    suspend fun updateTranscript(
        currentSubject: String, currentSemester: String, studentID: String,
        grade15: Double, grade45: Double, semesterGrade: Double
    ): Boolean {
        return TranscriptFirebase.updateTranscript(
            currentSubject, currentSemester, studentID,
            grade15, grade45, semesterGrade
        )
    }


}