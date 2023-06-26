package com.udemy.studentmanagement.ui.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.SummaryAdapter
import com.udemy.studentmanagement.databinding.FragmentSummary2Binding
import kotlinx.coroutines.launch

class Summary2Fragment : Fragment() {

    private var _binding: FragmentSummary2Binding? = null
    private val binding get() = _binding!!

    private val viewModel : SummaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummary2Binding.inflate(inflater, container, false)

        setUpSpinner(binding.spinnerSemester, arrayListOf("1","2"))
        setUpButton()

        viewModel.summaryList2.observe(viewLifecycleOwner) {
            val layoutManger = LinearLayoutManager(context)
            val summaryAdapter = SummaryAdapter(it)
            binding.transcriptRecyclerView.apply {
                setHasFixedSize(true)
                adapter = summaryAdapter
                layoutManager = layoutManger
            }
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpinner(spinner : Spinner, data : ArrayList<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item,data)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter =  adapter
        spinner.setSelection(0)
    }

    private fun setUpButton() {
        // Hiện ra báo cáo tổng kết môn từ môn học và học kỳ chọn
        binding.seeSummaryBtn.setOnClickListener {
            lifecycleScope.launch {
                val selectedSemester = binding.spinnerSemester.selectedItem as String
                viewModel.showRequestedSummary2(selectedSemester)
            }
        }
    }

}