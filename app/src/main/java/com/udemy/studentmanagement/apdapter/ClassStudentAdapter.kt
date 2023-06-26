package com.udemy.studentmanagement.apdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.StudentInClassItemBinding
import com.udemy.studentmanagement.model.Student

class ClassStudentAdapter(
    private val studentList : ArrayList<Student>,
) : RecyclerView.Adapter<ClassStudentAdapter.ViewHolder>() {

    class ViewHolder(private val binding: StudentInClassItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student, position: Int) {
            val newPosition = position + 1
            binding.studentInClassOrder.text = newPosition.toString()
            binding.classStudentName.text = student.name
            binding.classStudentAddress.text = student.address
            binding.classStudentBirthDate.text = student.birthDate
            binding.classStudentGender.text = student.gender
            if (student.imageUri == null) {
                Glide
                    .with(binding.root)
                    .load(R.drawable.user_avatar)
                    .into(binding.classStudentAvt)
            } else Glide
                .with(binding.root)
                .load(student.imageUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.classStudentAvt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = StudentInClassItemBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student, position)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}