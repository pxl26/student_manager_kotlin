package com.udemy.studentmanagement.ui.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.databinding.FragmentStudentInfoBinding
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.launch

class StudentInfoFragment : Fragment() {

    private lateinit var binding : FragmentStudentInfoBinding

    //ViewModel
    private val viewModel : StudentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =  FragmentStudentInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData() // Set data of the student to UI

        // click button to update student's info
        binding.btnUpdateStudent.setOnClickListener {
            val newStudent = getStudentInfo()

            // If User change the student's ID
            if (newStudent.ID != viewModel.oldStudent?.ID) {
                lifecycleScope.launch {
                    if (viewModel.updateStudentID(viewModel.oldStudent!!, newStudent)) {
                        Toast.makeText(context,"Cập nhật học sinh thành công", Toast.LENGTH_SHORT).show()
                    } else
                        Toast.makeText(context,"Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                }
            }
            else
                lifecycleScope.launch {
                    if (viewModel.updateStudent(newStudent))
                        Toast.makeText(context,"Cập nhật học sinh thành công", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,"Đã có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show()
                }

            viewModel.oldStudent = newStudent
        }
    }

    private fun getStudentInfo(): Student {
        return Student(
            binding.studentInfoID.text.toString().trim(),
            binding.studentInfoName.text.toString().trim(),
            binding.studentInfoGender.text.toString().trim(),
            binding.studentInfoBirthDate.text.toString().trim(),
            binding.studentInfoAddress.text.toString().trim(),
            binding.studentInfoEmail.text.toString().trim(),
            viewModel.oldStudent?.transcript,
            viewModel.oldStudent?.className,
            viewModel.oldStudent?.imageUri
        )
    }

    private fun setData() {
        binding.studentInfoID.setText(viewModel.oldStudent?.ID)
        binding.studentInfoName.setText(viewModel.oldStudent?.name)
        binding.studentInfoAddress.setText(viewModel.oldStudent?.address)
        binding.studentInfoBirthDate.setText(viewModel.oldStudent?.birthDate)
        binding.studentInfoGender.setText(viewModel.oldStudent?.gender)
        binding.studentInfoEmail.setText(viewModel.oldStudent?.email)
        Glide.with(this)
            .load(viewModel.oldStudent?.imageUri?.toUri() ?: R.drawable.user_avatar)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.studentInfoAvt)
    }

}