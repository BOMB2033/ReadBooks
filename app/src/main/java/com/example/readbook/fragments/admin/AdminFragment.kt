package com.example.readbook.fragments.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.readbook.R
import com.example.readbook.databinding.FragmentAdminBinding
import com.example.readbook.databinding.FragmentReadBinding
import com.example.readbook.repository.Book
import com.example.readbook.repository.DataViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AdminFragment : Fragment() {
    private lateinit var binding: FragmentAdminBinding
    private val viewModel:DataViewModel by activityViewModels()
    private var databaseBooksReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("books")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            binding.buttonAdd.setOnClickListener {
                val name = editTextName.text.toString().trim()
                val author = editTextAuthor.text.toString().trim()
                val path = editTextURL.text.toString().trim()

                if (name.isEmpty() || author.isEmpty() || path.isEmpty()) {
                    // Выводим сообщение об ошибке
                    Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.addBook(Book(name = name, author = author, path = path))
                it.findNavController().popBackStack()
            }
        }

    }
}