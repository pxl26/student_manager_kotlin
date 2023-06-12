package com.udemy.studentmanagement.apdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.SettingGridItemBinding
import com.udemy.studentmanagement.model.SettingItem

class SettingGridViewAdapter(
    private val context: Context,
    private val data: ArrayList<SettingItem>,
    private val listener: OnItemClickListener
) : BaseAdapter() {

    interface OnItemClickListener {
        fun onItemClick(position : Int)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding : SettingGridItemBinding = if (convertView == null) {
            SettingGridItemBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        } else {
            SettingGridItemBinding.bind(convertView)
        }

        binding.itemHeader.text = data[position].header
        binding.itemDescription.text = data[position].description
        binding.settingItem.setOnClickListener {
            listener.onItemClick(position)
        }

        return binding.root
    }
}