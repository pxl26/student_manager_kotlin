package com.udemy.studentmanagement.model

data class Class(
    var name : String,
    val students : ArrayList<String>
) {
    constructor() : this("",ArrayList())
}
