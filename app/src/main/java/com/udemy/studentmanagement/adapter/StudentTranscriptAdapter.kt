package com.udemy.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.TranscriptItemBinding
import com.udemy.studentmanagement.model.Student

class StudentTranscriptAdapter(
    private val studentList : ArrayList<Student>,
    private val selectedSubject : String,
    private val selectedSemester : String,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StudentTranscriptAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Student)
    }

    class ViewHolder(private val binding: TranscriptItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student,
                 selectedSubject : String,
                 selectedSemester : String,
                 listener: OnItemClickListener,
                 position: Int) {
            binding.transcriptStudentName.text = student.name
            binding.transcriptStudentID.text = student.ID
            val newPosition = position + 1
            binding.transcriptStudentOrder.text = newPosition.toString()
            val grade15 = student.transcript?.get("${selectedSubject}/${selectedSemester}")?.grade15 ?: 0
            val grade45 = student.transcript?.get("${selectedSubject}/${selectedSemester}")?.grade45 ?: 0
            val semesterGrade = student.transcript?.get("${selectedSubject}/${selectedSemester}")?.semesterGrade ?: 0
            binding.transcriptGrade15.text = grade15.toString()
            binding.transcriptGrade45.text = grade45.toString()
            binding.transcriptSemesterGrade.text = semesterGrade.toString()

            if (student.imageUri != null)
                Glide.with(binding.root)
                    .load(student.imageUri!!.toUri())
                    .into(binding.transcriptStudentAvt)
            else {
                Glide.with(binding.root)
                    .load(R.drawable.user_avatar)
                    .into(binding.transcriptStudentAvt)
            }

            binding.transcriptItem.setOnClickListener {
                listener.onItemClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = TranscriptItemBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student,selectedSubject, selectedSemester, listener, position)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}