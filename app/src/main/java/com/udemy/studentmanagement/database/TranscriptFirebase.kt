package com.udemy.studentmanagement.database

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.udemy.studentmanagement.model.SettingResponse
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.Transcript
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

object TranscriptFirebase {

    private val subjectFirebase = FirebaseFirestore.getInstance()
        .collection(FirebaseCollection.Setting)
        .document(User.id)

    private val transcriptFirebase = FirebaseFirestore.getInstance()
        .collection(FirebaseCollection.User)
        .document(User.id)
        .collection(FirebaseCollection.Student)

    suspend fun getAllSubjects() : Flow<ArrayList<String>> = callbackFlow {
        val listener = subjectFirebase.addSnapshotListener { value, error ->
            if (error != null) {
                cancel("Đã xảy ra lỗi, vui lòng khởi động lại ứng dụng", error)
                return@addSnapshotListener
            }
            value?.let {
                val subjectList = value.toObject(SettingResponse::class.java)?.NamesOfSubject
                if (subjectList != null)
                    trySend(ArrayList(subjectList))
                else trySend(ArrayList())
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    // Hàm để lấy toàn bộ bảng điểm được yêu cầu
    suspend fun getRequestedTranscript(
        selectedClass: String,
        selectedSubject: String,
        selectedSemester: String) : Flow<ArrayList<Student>> = callbackFlow {

        val result = ArrayList<Student>()

        // Lấy toàn bộ học sinh trong lớp được chọn
        val studentList = ClassFirebase.getStudentInClass(selectedClass)

        // Lấy bảng điểm của từng học sinh
        for (student in studentList) {

            var transcript = student.transcript?.get("${selectedSubject}/${selectedSemester}")
            // Nếu học sinh nào chưa có bảng điểm thì tạo ra một bảng điểm mới
            if (transcript == null) {
                transcript = Transcript(selectedSubject,selectedSemester)
                if (student.transcript == null )
                    student.transcript = hashMapOf()
                student.transcript?.set("${selectedSubject}/${selectedSemester}", transcript)

                transcriptFirebase.document(student.ID).update(
                    "transcript", student.transcript
                )
            }
            result.add(student)
        }
        trySend(result)

        awaitClose {
            // Close the Firestore snapshot listener and cancel the callbackFlow
            cancel()
        }
    }

    suspend fun updateTranscript(
        currentSubject: String, currentSemester: String, studentID: String,
        grade15: Double, grade45: Double, semesterGrade: Double
    ): Boolean {
        return try {
            val newTranscript = Transcript(currentSubject,currentSemester,grade15,grade45,semesterGrade)

            val hashMap = hashMapOf("${currentSubject}/${currentSemester}" to newTranscript)
            val data = mapOf("transcript" to hashMap)

            transcriptFirebase.document(studentID).set(data, SetOptions.merge()).await()
            true
        } catch (e : Exception) {
            false
        }

    }

}