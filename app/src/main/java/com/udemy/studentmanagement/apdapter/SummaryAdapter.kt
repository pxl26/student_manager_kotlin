package com.udemy.studentmanagement.apdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udemy.studentmanagement.databinding.SummaryItemBinding
import com.udemy.studentmanagement.model.Summary

class SummaryAdapter(
    private val summaryList : ArrayList<Summary>
) : RecyclerView.Adapter<SummaryAdapter.ViewHolder>() {

    class ViewHolder(private val binding: SummaryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(summary: Summary) {
            binding.nextBtn.visibility = View.INVISIBLE
            binding.className.text = summary.className
            binding.numOfStudent.text = summary.numOfStudents.toString()
            binding.numOfQualifiedStudent.text = summary.numOfQualifiedStudent.toString()
            binding.rate.text = summary.rate.toString() + "%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = SummaryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val summary = summaryList[position]
        holder.bind(summary)
    }

    override fun getItemCount(): Int {
        return summaryList.size
    }

}