package com.udemy.studentmanagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.SearchResultBinding
import com.udemy.studentmanagement.model.SearchResult

class SearchResultAdapter(
    private val searchResultList : ArrayList<SearchResult>,
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(private val binding: SearchResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResult: SearchResult) {
            binding.searchResultClass.text = searchResult.className ?: ""
            binding.searchResultStudentName.text = searchResult.studentName
            binding.searchResultTranscript1.text = searchResult.transcript1.toString()
            binding.searchResultTranscript2.text = searchResult.transcript2.toString()
            if (searchResult.imageUri == null) {
                Glide
                    .with(binding.root)
                    .load(R.drawable.user_avatar)
                    .into(binding.searchResultAvt)
            } else Glide
                .with(binding.root)
                .load(searchResult.imageUri)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.searchResultAvt)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = SearchResultBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = searchResultList[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int {
        return searchResultList.size
    }

}