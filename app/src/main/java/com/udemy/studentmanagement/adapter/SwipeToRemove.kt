package com.udemy.studentmanagement.adapter

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.udemy.studentmanagement.model.viewModel
import kotlinx.coroutines.launch

class SwipeToRemove(
    private val viewModel : viewModel
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val snackBar = Snackbar.make(viewHolder.itemView.rootView,"Xóa thành công", Snackbar.LENGTH_LONG)
        viewModel.viewModelScope.launch {
            viewModel.deleteItem(viewHolder.adapterPosition)
        }
        snackBar.show()
    }
}