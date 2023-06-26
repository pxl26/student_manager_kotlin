package com.udemy.studentmanagement.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.FragmentSetting3Binding
import kotlinx.coroutines.launch

class Setting3Fragment : Fragment() {

    private var _binding: FragmentSetting3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetting3Binding.inflate(inflater, container, false)
        setUpDeleteSpinner()
        setUpNameChangeSpinner()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setting3Btn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.setChangeForSubject(
                        binding.addSubject.text.toString().trim(),
                        binding.deleteSubject.selectedItem.toString().trim(),
                        binding.subjectOldName.selectedItem.toString().trim(),
                        binding.subjectNewName.text.toString().trim()
                    )
                ) {
                    Toast.makeText(requireContext(), "Cập nhật thành công", Toast.LENGTH_SHORT)
                        .show()
                } else
                    Toast.makeText(
                        requireContext(),
                        "Đã xảy ra lỗi, vui lòng thử lại",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpNameChangeSpinner() {
        viewModel.subjectList.observe(viewLifecycleOwner) { data ->
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(R.layout.simple_spinner_item)
            binding.subjectOldName.adapter = adapter
        }
    }

    private fun setUpDeleteSpinner() {
        viewModel.subjectList.observe(viewLifecycleOwner) {
            it.add(0,"Không chọn")
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                it
            )
            adapter.setDropDownViewResource(R.layout.simple_spinner_item)
            binding.deleteSubject.adapter = adapter
            binding.deleteSubject.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.deleteSubject.setSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.deleteSubject.setSelection(0)
                }
            }
        }
    }
}