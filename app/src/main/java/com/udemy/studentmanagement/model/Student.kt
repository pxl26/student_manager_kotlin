package com.udemy.studentmanagement.model

data class Student(
    var ID : String,
    var name : String,
    var gender : String,
    var birthDate : String,
    var address : String,
    var email : String,
    var transcript: HashMap<String,Transcript>?,
    var className : String?,
    var imageUri : String?
) {
    constructor() : this("","","","","","",
        null,null, null)
}