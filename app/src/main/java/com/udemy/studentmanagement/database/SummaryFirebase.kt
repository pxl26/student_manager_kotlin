package com.udemy.studentmanagement.database

import com.google.firebase.firestore.FirebaseFirestore
import com.udemy.studentmanagement.model.Summary
import com.udemy.studentmanagement.model.User
import com.udemy.studentmanagement.util.FirebaseCollection
import kotlinx.coroutines.tasks.await
import com.udemy.studentmanagement.model.Class
import com.udemy.studentmanagement.util.Constraint

object SummaryFirebase {

    private val classFirebase = FirebaseFirestore.getInstance().collection(FirebaseCollection.User)
        .document(User.id).collection(FirebaseCollection.Class)


    suspend fun showRequestedSummary(selectedSubject: String, selectedSemester: String) : ArrayList<Summary> {
        val result = ArrayList<Summary>()

        // Lấy toàn bộ lớp học hiện tại
        val classList = classFirebase.get().await()

        // Xét từng lớp học
        for (item in classList) {

            // Gán thông tin lớp học vào bảng tổng kết
            val summary = Summary()
            val aClass = item.toObject(Class::class.java)
            summary.className = aClass.name
            summary.numOfStudents = aClass.students.size

            // Kiểm tra lớp có học sinh không
            if (summary.numOfStudents == 0) {
                summary.numOfQualifiedStudent = 0
                summary.rate = 0.00
            } else {
                // Xét tỉ lệ học sinh đạt trong từng lớp học
                val studentList = ClassFirebase.getStudentInClass(aClass.name)
                for (student in studentList) {
                    // Nếu bảng điểm tồn tại
                    val transcript = student.transcript?.get("${selectedSubject}/${selectedSemester}")
                    if (transcript != null &&
                        (transcript.grade15 + transcript.grade45 + transcript.semesterGrade) / 3 >= Constraint.AdmissionScore)
                    {
                        summary.numOfQualifiedStudent++
                    }
                }
                summary.rate = summary.numOfQualifiedStudent.toDouble() / summary.numOfStudents.toDouble() * 100
            }
            result.add(summary)
        }
        return result
    }

}