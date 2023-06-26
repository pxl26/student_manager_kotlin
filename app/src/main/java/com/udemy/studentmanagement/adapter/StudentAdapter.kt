package com.udemy.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.StudentItemBinding
import com.udemy.studentmanagement.model.Student

class StudentAdapter(
    private val studentList : ArrayList<Student>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StudentAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Student)
    }

    class ViewHolder(private val binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student, listener: OnItemClickListener) {
            binding.studentItemName.text = student.name
            binding.studentItemAddress.text = student.address
            binding.studentItemEmail.text = student.email
            binding.studentItemBirthDate.text = student.birthDate
            binding.studentItemGender.text = student.gender
            if (student.imageUri == null) {
                Glide
                    .with(binding.root)
                    .load(R.drawable.user_avatar)
                    .into(binding.studentItemAvt)
            } else Glide
                .with(binding.root)
                .load(student.imageUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.studentItemAvt)
            binding.studentItem.setOnClickListener {
                listener.onItemClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = StudentItemBinding.inflate(LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student, listener)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

}
