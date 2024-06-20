package com.example.readbook.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.readbook.databinding.ItemBookBinding


class BookDiffCallback : DiffUtil.ItemCallback<Book>(){
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.uid==newItem.uid
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return  oldItem == newItem
    }

}
class BooksViewHolder(private val binding: ItemBookBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(book: Book, listener: BooksAdapter.Listener) {
        binding.apply {
            nameBook.text = book.name
            author.text = book.author
            root.setOnClickListener {
                listener.addBook(book)
            }
        }
    }
}

class BooksAdapter(
    private val listener: Listener,
):ListAdapter<Book, BooksViewHolder>(BookDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BooksViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BooksViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(post, listener)
    }

    interface Listener{
        fun addBook(book: Book)
    }
}
