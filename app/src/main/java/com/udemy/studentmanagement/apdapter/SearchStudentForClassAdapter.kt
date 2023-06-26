package com.udemy.studentmanagement.apdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.SearchClassStudentBinding
import com.udemy.studentmanagement.model.Student

class SearchResultsAdapter(
    private var studentsList : ArrayList<Student>,
    private val listener : OnItemClickListener
) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>(), Filterable {

    interface OnItemClickListener {
        fun onItemClick(student: Student)
    }

    private val studentListFull = studentsList
    fun removeStudent(student : Student) {
        val position = studentsList.indexOf(student)
        studentsList.remove(student)
        studentListFull.remove(student)
        notifyItemRemoved(position)
    }

    class ViewHolder(val binding : SearchClassStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student : Student, listener: OnItemClickListener) {
            binding.searchResultName.text = student.name
            binding.searchResultID.text = student.ID
            if (student.imageUri == null) {
                Glide
                    .with(binding.root)
                    .load(R.drawable.user_avatar)
                    .into(binding.searchResultAvt)
            } else Glide
                .with(binding.root)
                .load(student.imageUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.searchResultAvt)


            binding.searchResultItem.setOnClickListener {
                listener.onItemClick(student)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchClassStudentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentsList[position]
        holder.bind(student, listener)
    }

    override fun getItemCount(): Int {
        return studentsList.size
    }

    override fun getFilter(): Filter {
        val filter = object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResults.values = studentListFull
                    filterResults.count = studentListFull.size
                } else {
                    val searchStr = constraint.toString().lowercase().trim()
                    val searchResult = ArrayList<Student>()
                    for (student in studentListFull) {
                        if (student.ID.lowercase().contains(searchStr) ||
                            student.name.lowercase().contains(searchStr))
                            searchResult.add(student)
                    }
                    filterResults.values = searchResult
                    filterResults.count = searchResult.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                studentsList = results?.values as ArrayList<Student>
                notifyDataSetChanged()
            }
        }

        return filter
    }
}