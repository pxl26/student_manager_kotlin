package com.udemy.studentmanagement.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.FragmentSetting1Binding
import com.udemy.studentmanagement.databinding.FragmentSettingBinding
import com.udemy.studentmanagement.util.Constraint
import kotlinx.coroutines.launch


class Setting1Fragment : Fragment() {

    private var _binding: FragmentSetting1Binding? = null
    private val binding get() = _binding!!

    private val viewModel : SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetting1Binding.inflate(inflater,container,false)

        binding.studentMaxAge.setText(Constraint.studentMaxAge.toString())
        binding.studentMinAge.setText(Constraint.studentMinAge.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setting1Btn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.setMinMaxAgeForStudent(
                        binding.studentMaxAge.text.toString().trim(),
                        binding.studentMinAge.text.toString().trim()
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

}