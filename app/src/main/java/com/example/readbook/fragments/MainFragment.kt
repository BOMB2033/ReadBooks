package com.example.readbook.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.readbook.R
import com.example.readbook.databinding.FragmentMainBinding
import com.example.readbook.repository.Book
import com.example.readbook.repository.BooksAdapter
import com.example.readbook.repository.DataViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: DataViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waitingLoad()

        with(binding){
            val adapter = BooksAdapter(object :BooksAdapter.Listener{
                override fun addBook(book: Book) {
                   viewModel.addBook(book)
                    binding.root.findNavController().navigate(R.id.action_mainFragment_to_readFragment, ReadFragment.setUidBook(book.uid))
                }

            })
            recyclerView.adapter = adapter

            viewModel.data.observe(viewLifecycleOwner){
                adapter.submitList(it.books)
            }

            buttonAdd.setOnClickListener {
                it.findNavController().navigate(R.id.action_mainFragment_to_adminFragment)
            }

        }

    }
    private fun waitingLoad(){
        viewLifecycleOwner.lifecycleScope.launch {
            while (viewModel.getUser().uid == "")
                delay(1000)
            if (viewModel.getUser().isAdmin)
                binding.buttonAdd.visibility = View.VISIBLE
            else
                binding.buttonAdd.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }
}