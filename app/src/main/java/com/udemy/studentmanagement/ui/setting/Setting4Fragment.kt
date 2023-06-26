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
import com.udemy.studentmanagement.databinding.FragmentSetting4Binding
import kotlinx.coroutines.launch

class Setting4Fragment : Fragment() {

    private var _binding: FragmentSetting4Binding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetting4Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setting4Btn.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.setChangeForAdmissionScore(binding.admissionScore.text.toString())) {
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
}