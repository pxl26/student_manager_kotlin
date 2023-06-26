package com.udemy.studentmanagement.ui.student

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isEmpty
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.SpinnerAdapter
import com.udemy.studentmanagement.databinding.AddstudentDialogBinding
import com.udemy.studentmanagement.model.Student
import com.udemy.studentmanagement.model.viewModel
import com.udemy.studentmanagement.util.Constraint
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class StudentDialog : BottomSheetDialogFragment() {

    private var _binding: AddstudentDialogBinding? = null
    private val binding get() = _binding!!

    private val studentViewModel: StudentViewModel by activityViewModels()

    //Activity launcher to get image
    private lateinit var getImage : ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri : String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddstudentDialogBinding.inflate(inflater, container, false)

        // Activity to get the student's avatar from the device's gallery
        getImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
            if (uri != null) {
                imageUri = uri.toString()
                Glide
                    .with(this)
                    .load(uri)
                    .into(binding.studentAvatar)
            } else {
                Toast.makeText(context,"Không cập nhật được hình ảnh",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()
        setUpDatePicker()
        setUpButton()
    }

    private fun setUpSpinner() {
        val genderList = ArrayList<SpinnerAdapter.SpinnerItem>()
        genderList.add(SpinnerAdapter.SpinnerItem(R.drawable.baseline_male_24, "Nam"))
        genderList.add(SpinnerAdapter.SpinnerItem(R.drawable.baseline_female_24, "Nữ"))
        genderList.add(SpinnerAdapter.SpinnerItem(R.drawable.baseline_transgender_24, "Khác"))
        binding.studentGender.adapter = context?.let { SpinnerAdapter(it, genderList) }
        binding.studentGender.setSelection(0)
    }

    private fun setUpDatePicker() {
        binding.studentBirthDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, _year, _month, dayOfMonth -> // Update the EditText field with the selected date
                    binding.studentBirthDate.setText("$dayOfMonth/${_month + 1}/$_year")
                }, year, month, day
            )

            calendar.add(Calendar.YEAR, - Constraint.studentMinAge)
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis

            calendar.add(Calendar.YEAR, - (Constraint.studentMaxAge - Constraint.studentMinAge))
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }
    }

    private fun setUpButton() {

        binding.studentAvatar.setOnClickListener {
            getImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.addStudentComplete.setOnClickListener {
            if (checkEmptyField())
                Toast.makeText(context, "Bạn không được bỏ trống bất kì thông tin nào", Toast.LENGTH_SHORT).show()
            else {
                val student = getStudent()
                // thêm học sinh mới vào database
                lifecycleScope.launch {
                    //Nếu thêm học sinh mới vào database thành công thì sẽ tắt dialog
                    if (studentViewModel.addStudent(student)) {
                        Toast.makeText(context,"Thêm học sinh thành công",Toast.LENGTH_SHORT).show()
                        dismiss()
                    }
                    // Nếu không thành công, cho người dùng nhập lại
                    else {
                        Toast.makeText(context,"Đã có lỗi xảy ra, vui lòng thử lại",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkEmptyField() : Boolean {
        return (binding.studentName.text.isEmpty() || binding.studentGender.isEmpty() ||
                binding.studentEmail.text.isEmpty() || binding.studentAddress.text.isEmpty() ||
                binding.studentBirthDate.text.isEmpty())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getStudent() : Student {
        val gender = binding.studentGender.selectedItem as SpinnerAdapter.SpinnerItem
        return Student(
            binding.studentID.text.toString().trim(),
            binding.studentName.text.toString().trim(),
            gender.spinnerText,
            binding.studentBirthDate.text.toString().trim(),
            binding.studentAddress.text.toString().trim(),
            binding.studentEmail.text.toString().trim(),
            null,
            null,
            imageUri
        )
    }

}