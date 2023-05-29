package com.udemy.studentmanagement.ui.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.StudentAdapter
import com.udemy.studentmanagement.adapter.SwipeToRemove
import com.udemy.studentmanagement.databinding.FragmentStudentBinding
import com.udemy.studentmanagement.model.Student
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentFragment : Fragment() {

    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val viewModel : StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentBinding.inflate(inflater, container, false)

        //update the recycler view
        viewModel.studentList.observe(viewLifecycleOwner) {
            val layoutManger = LinearLayoutManager(context)
            val studentAdapter = StudentAdapter(it, object : StudentAdapter.OnItemClickListener {
                override fun onItemClick(item: Student) {
                    //Show student's information by moving to another fragment
                    viewModel.oldStudent = item
                    findNavController().navigate(R.id.action_nav_student_to_nav_student_info)
                }
            })
            binding.studentRecyclerView.apply {
                val swipeToRemove = ItemTouchHelper(SwipeToRemove(viewModel))
                swipeToRemove.attachToRecyclerView(binding.studentRecyclerView)
                setHasFixedSize(true)
                adapter = studentAdapter
                layoutManager = layoutManger
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // show Bottom sheet dialog to add student
        binding.fabStudent.setOnClickListener {
            addStudent()
        }

    }

    private fun addStudent() {
        val bottomSheetFragment = StudentDialog()
        bottomSheetFragment.show(childFragmentManager,bottomSheetFragment.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}