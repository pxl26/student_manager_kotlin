package com.udemy.studentmanagement.model

data class Summary(
    var className : String,
    var numOfStudents : Int,
    var numOfQualifiedStudent : Int,
    var rate : Double
) {
    constructor() : this("",0,0,0.00)
}