package com.udemy.studentmanagement.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.SettingGridViewAdapter
import com.udemy.studentmanagement.databinding.FragmentSettingBinding
import com.udemy.studentmanagement.model.SettingItem

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SettingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpGridView()
    }

    private fun setUpGridView() {
        val data = arrayListOf(
            SettingItem(R.drawable.baseline_age,
                requireContext().getString(R.string.setting_header1),
                requireContext().getString(R.string.setting_description1)),
            SettingItem(R.drawable.baseline_people_24,
                requireContext().getString(R.string.setting_header2),
                requireContext().getString(R.string.setting_description2)),
            SettingItem(R.drawable.baseline_note_add_24,
                requireContext().getString(R.string.setting_header3),
                requireContext().getString(R.string.setting_description3)),
            SettingItem(R.drawable.baseline_check_box_24,
                requireContext().getString(R.string.setting_header4),
                requireContext().getString(R.string.setting_description4)),
        )
        val adapter = SettingGridViewAdapter(requireContext(), data, object : SettingGridViewAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> findNavController().navigate(R.id.action_nav_setting_to_setting1Fragment)
                    1 -> findNavController().navigate(R.id.action_nav_setting_to_setting2Fragment)
                    2 -> findNavController().navigate(R.id.action_nav_setting_to_setting3Fragment)
                    3 -> findNavController().navigate(R.id.action_nav_setting_to_setting4Fragment)
                }
            }
        })
        binding.settingGridView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}