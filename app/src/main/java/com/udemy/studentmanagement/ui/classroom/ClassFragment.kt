package com.udemy.studentmanagement.ui.classroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.ClassAdapter
import com.udemy.studentmanagement.databinding.FragmentClassBinding
import com.udemy.studentmanagement.model.Class

class ClassFragment : Fragment() {

    // View Binding
    private var _binding : FragmentClassBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val viewModel : ClassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassBinding.inflate(inflater, container, false)

        // Recycler view update every time the data change
        viewModel.classList.observe(viewLifecycleOwner) {
            val layoutManger = LinearLayoutManager(context)
            val studentAdapter = ClassAdapter(it, object : ClassAdapter.OnItemClickListener {
                override fun onItemClick(item: Class) {
                    //Move to fragment showing student of selected class
                    viewModel.selectedClass = item
                    findNavController().navigate(R.id.action_nav_class_to_classInfoFragment)
                }
            })
            binding.classRecyclerView.apply {
                setHasFixedSize(true)
                adapter = studentAdapter
                layoutManager = layoutManger
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}