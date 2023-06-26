package com.udemy.studentmanagement.ui.setting

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.FragmentSetting2Binding
import com.udemy.studentmanagement.util.Constraint
import kotlinx.coroutines.launch

class Setting2Fragment : Fragment() {

    private var _binding: FragmentSetting2Binding? = null
    private val binding get() = _binding!!

    private val viewModel : SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetting2Binding.inflate(inflater,container,false)

        binding.classMaxSize.setText(Constraint.classMaxSize.toString())


        setUpDeleteSpinner()
        setUpSpinner(binding.classOldName, Constraint.NamesOfClass)
        setUpNameChangeSpinner()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setting2Btn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.setChangeForClass(
                        binding.classMaxSize.text.toString().trim(),
                        binding.addClass.text.toString().trim(),
                        binding.deleteClass.selectedItem.toString().trim(),
                        binding.classOldName.selectedItem.toString().trim(),
                        binding.classNewName.text.toString().trim()
                    )) {
                    Toast.makeText(requireContext(),"Cập nhật thành công", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(requireContext(),"Đã xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpSpinner(spinner : Spinner, data : ArrayList<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item,data)
        adapter.setDropDownViewResource(R.layout.simple_spinner_item)
        spinner.adapter =  adapter
    }

    private fun setUpNameChangeSpinner() {
        viewModel.classList.observe(viewLifecycleOwner) { data ->
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, data)
            adapter.setDropDownViewResource(R.layout.simple_spinner_item)
            binding.classOldName.adapter = adapter
        }
    }

    private fun setUpDeleteSpinner() {
        viewModel.classList.observe(viewLifecycleOwner) {
            it.add(0,"Không chọn")
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                it
            )
            adapter.setDropDownViewResource(R.layout.simple_spinner_item)
            binding.deleteClass.adapter = adapter
            binding.deleteClass.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.deleteClass.setSelection(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    binding.deleteClass.setSelection(0)
                }
            }
        }
    }
}