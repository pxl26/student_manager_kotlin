package com.udemy.studentmanagement.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.studentmanagement.adapter.SearchResultAdapter
import com.udemy.studentmanagement.databinding.FragmentSearchBinding
import com.udemy.studentmanagement.ui.student.StudentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val viewModel : SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)

        setUpSearchView()
        setUpRecyclerView()

        return binding.root
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showResult(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showResult(query: String?) {
        if (query == null || query.isEmpty()) {
            return
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.searchStudent(query)
            }
        }
    }

    private fun setUpRecyclerView() {
        viewModel.searchResultList.observe(viewLifecycleOwner) {
            val searchResultAdapter = SearchResultAdapter(it)
            val layoutManger = LinearLayoutManager(context)
            binding.searchResult.apply {
                setHasFixedSize(true)
                adapter = searchResultAdapter
                layoutManager = layoutManger
            }
        }
    }

}