package com.udemy.studentmanagement.ui.transcript

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.SpinnerAdapter
import com.udemy.studentmanagement.adapter.StudentTranscriptAdapter
import com.udemy.studentmanagement.adapter.SwipeToRemove
import com.udemy.studentmanagement.databinding.FragmentTranscriptBinding
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.Transcript
import kotlinx.coroutines.launch

//Fragment to see all the transcript of every subject
class TranscriptFragment : Fragment() {

    private var _binding: FragmentTranscriptBinding? = null
    private val binding get() = _binding!!

    private val viewModel : TranscriptViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranscriptBinding.inflate(inflater, container, false)

        viewModel.classList.observe(viewLifecycleOwner) {
            setUpSpinner(binding.spinnerClassName, it)
        }

        viewModel.subjectList.observe(viewLifecycleOwner) {
            setUpSpinner(binding.spinnerSubject, it)
        }
        setUpSpinner(binding.spinnerSemester, arrayListOf("1","2"))

        onClickButton()

        viewModel.studentList.observe(viewLifecycleOwner) {
            val layoutManger = LinearLayoutManager(context)
            val studentAdapter = StudentTranscriptAdapter(it,
                binding.spinnerSubject.selectedItem as String,
                binding.spinnerSemester.selectedItem as String,
                object : StudentTranscriptAdapter.OnItemClickListener {
                override fun onItemClick(item: Student) {
                    //Show student's information by moving to another fragment
                    viewModel.selectedStudent = item
                    viewModel.selectedTranscript = item.transcript?.get("${viewModel.currentSubject}/${viewModel.currentSemester}")!!
                    findNavController().navigate(R.id.action_nav_transcript_to_transcriptEditFragment)
                }
            })
            binding.transcriptRecyclerView.apply {
                setHasFixedSize(true)
                adapter = studentAdapter
                layoutManager = layoutManger
            }
        }
        return binding.root
    }

    private fun onClickButton() {
        binding.seeTranscriptBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.currentClass = binding.spinnerClassName.selectedItem as String
                viewModel.currentSubject = binding.spinnerSubject.selectedItem as String
                viewModel.currentSemester = binding.spinnerSemester.selectedItem as String

                viewModel.getRequestedTranscript()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpinner(spinner : Spinner, data : ArrayList<String>) {
        val adapter = ArrayAdapter(requireContext(),R.layout.simple_spinner_item,data)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter =  adapter
        spinner.setSelection(0)
    }
}