package com.udemy.studentmanagement.model

data class SettingResponse(
    var studentMinAge : Int,
    var studentMaxAge : Int,
    var classMaxSize : Int,
    var NumberOfClass : Int,
    var NamesOfClass : ArrayList<String>,
    var NumberOfSubject : Int,
    var NamesOfSubject : ArrayList<String>,
    var AdmissionScore : Double
) {
    constructor() : this(0,0,0,0, arrayListOf(),0, arrayListOf(),0.00)
}