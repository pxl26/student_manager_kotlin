package com.udemy.studentmanagement.apdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.SpinnerItemBinding

class SpinnerAdapter(private val context: Context,
                     items: ArrayList<SpinnerItem>)
    : ArrayAdapter<SpinnerAdapter.SpinnerItem>(context, R.layout.spinner_item,items) {

    data class SpinnerItem(
        val spinnerImage : Int,
        val spinnerText : String
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = SpinnerItemBinding.inflate(LayoutInflater.from(context),
                parent, false).root
        }
        val item = getItem(position)
        if (item != null) {
            view.findViewById<ImageView>(R.id.spinner_img).setImageResource(item.spinnerImage)
            view.findViewById<TextView>(R.id.spinner_text).text = item.spinnerText
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = SpinnerItemBinding.inflate(LayoutInflater.from(context),
                parent, false).root
        }

        val item = getItem(position)
        if (item != null) {
            view.findViewById<ImageView>(R.id.spinner_img).setImageResource(item.spinnerImage)
            view.findViewById<TextView>(R.id.spinner_text).text = item.spinnerText
            view.findViewById<LinearLayout>(R.id.spinner_item).setPadding(10)
        }

        return view
    }
}