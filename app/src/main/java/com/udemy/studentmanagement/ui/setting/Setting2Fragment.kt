package com.udemy.studentmanagement.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        setUpSpinner(binding.deleteClass, Constraint.NamesOfClass)
        setUpSpinner(binding.classOldName, Constraint.NamesOfClass)

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
                    setUpSpinner(binding.deleteClass, Constraint.NamesOfClass)
                    setUpSpinner(binding.classOldName, Constraint.NamesOfClass)
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
}