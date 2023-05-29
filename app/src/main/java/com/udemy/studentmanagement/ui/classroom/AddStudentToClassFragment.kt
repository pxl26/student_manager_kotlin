package com.udemy.studentmanagement.ui.classroom

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.R
import com.udemy.studentmanagement.adapter.SearchResultsAdapter
import com.udemy.studentmanagement.databinding.FragmentAddStudentToClassBinding
import com.udemy.studentmanagement.model.Student
import kotlinx.coroutines.launch

class AddStudentToClassFragment : Fragment() {

    //View binding
    private var _binding: FragmentAddStudentToClassBinding? = null
    private val binding get() = _binding!!

    //View Model
    private val viewModel : ClassViewModel by activityViewModels()

    //Adapter for recycler
    private lateinit var searchResultAdapter : SearchResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStudentToClassBinding.inflate(inflater, container, false)

        setUpRecyclerView()
        setUpSearchView()

        return binding.root
    }

    private fun setUpSearchView() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
                val searchView = menu.findItem(R.id.searchView).actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        //Show search result for student's name or ID

                        //Filter your search results here based on the newText entered by the user
                        searchResultAdapter.filter.filter(newText)
                        return false
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setUpRecyclerView() {
        val layoutManger = LinearLayoutManager(context)
        searchResultAdapter = SearchResultsAdapter(
            viewModel.getStudentsWithoutClass(),
            //Thêm một học sinh vào lớp khi nhấn vào item của học sinh đó
            object : SearchResultsAdapter.OnItemClickListener {
                override fun onItemClick(student : Student) {
                    lifecycleScope.launch {
                        val result = viewModel.addStudentToClass(student)
                        if (result == 1) {
                            Toast.makeText(
                                context,
                                "Thêm học sinh vào lớp thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            searchResultAdapter.removeStudent(student)
                        }
                        else if (result == -1)
                            Toast.makeText(context, "Số học sinh thêm vào đã vượt giới hạn",
                                Toast.LENGTH_SHORT).show()
                        else Toast.makeText(context, "Đã có lỗi xảy ra, vui lòng thử lại",
                                Toast.LENGTH_SHORT).show()

                    }
                }
        })
        binding.searchResultForClass.apply {
            setHasFixedSize(true)
            adapter = searchResultAdapter
            layoutManager = layoutManger
        }
    }
}