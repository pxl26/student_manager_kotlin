package com.udemy.studentmanagement.model

import androidx.lifecycle.ViewModel

abstract class viewModel  : ViewModel() {

    abstract suspend fun deleteItem(position : Int)
}