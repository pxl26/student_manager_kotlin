package com.udemy.studentmanagement.ui.classroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.ClassStudentAdapter
import com.udemy.studentmanagement.adapter.StudentAdapter
import com.udemy.studentmanagement.adapter.SwipeToRemove
import com.udemy.studentmanagement.adapter.SwipeToRemoveStudentInClass
import com.udemy.studentmanagement.databinding.FragmentClassInfoBinding
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.launch

class ClassInfoFragment : Fragment() {

    //View binding
    private var _binding: FragmentClassInfoBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val viewModel : ClassViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassInfoBinding.inflate(inflater,container, false)

        viewModel.viewModelScope.launch {
            viewModel.getStudentsOfSelectedClass()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Click on the button to add student to the selected class
        binding.fabClass.setOnClickListener {
            // Show UI to add student to the class
            findNavController().navigate(R.id.action_nav_class_info_to_addStudentToClassFragment)
        }

        // set up the student of the selected class
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        viewModel.studentOfSelectedClass.observe(viewLifecycleOwner) {
            val layoutManger = LinearLayoutManager(context)
            val classStudentAdapter = ClassStudentAdapter(it)
            binding.studentOfSelectedClass.apply {
                val swipeToRemove = ItemTouchHelper(SwipeToRemoveStudentInClass(viewModel))
                swipeToRemove.attachToRecyclerView(binding.studentOfSelectedClass)
                setHasFixedSize(true)
                adapter = classStudentAdapter
                layoutManager = layoutManger
            }
        }
    }
}