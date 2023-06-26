package com.udemy.studentmanagement.ui.transcript

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.udemy.studentmanagement.databinding.FragmentTranscriptEditBinding
import kotlinx.coroutines.launch

class TranscriptEditFragment : Fragment() {

    private var _binding: FragmentTranscriptEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel : TranscriptViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranscriptEditBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTranscriptGrade15.setText(viewModel.selectedTranscript.grade15.toString())
        binding.editTranscriptGrade45.setText(viewModel.selectedTranscript.grade45.toString())
        binding.editTranscriptSemesterGrade.setText(viewModel.selectedTranscript.semesterGrade.toString())

        binding.editBtn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.updateTranscript(
                    binding.editTranscriptGrade15.text.toString().trim(),
                    binding.editTranscriptGrade45.text.toString().trim(),
                    binding.editTranscriptSemesterGrade.text.toString().trim()
                ))
                    Toast.makeText(requireContext(),"Cập nhật thành công", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(requireContext(),"Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}