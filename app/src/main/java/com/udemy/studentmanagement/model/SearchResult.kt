package com.udemy.studentmanagement.model

data class SearchResult(
    var imageUri : String?,
    var studentName : String,
    var className : String?,
    var transcript1: Double,
    var transcript2: Double
) {
    constructor() : this(null,"",null,0.0,0.0)
}
