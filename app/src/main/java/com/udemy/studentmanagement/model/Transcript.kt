package com.udemy.studentmanagement.model

data class Transcript(
    var subject : String,
    var semester : String,
    var grade15 : Double,
    var grade45 : Double,
    var semesterGrade : Double
) {
    constructor(subject : String, semester : String ) : this(subject,semester,0.00,0.00,0.00)

    constructor() : this("", "",0.00,0.00,0.00)
}