package com.udemy.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udemy.studentmanagement.databinding.ClassItemBinding
import com.udemy.studentmanagement.model.Class

class ClassAdapter(
    private val classList : ArrayList<Class>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Class)
    }

    class ViewHolder(private val binding: ClassItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind( aClass : Class, listener: OnItemClickListener) {
            binding.className.text = aClass.name
            binding.classAttendants.text = aClass.students.size.toString()
            binding.classItem.setOnClickListener {
                listener.onItemClick(aClass)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ClassItemBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aClass = classList[position]
       holder.bind( aClass , listener)
    }

    override fun getItemCount(): Int {
        return classList.size
    }

}