package com.example.readbook.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.readbook.R
import com.example.readbook.databinding.FragmentReadBinding
import com.example.readbook.repository.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

private const val ARG_UID_BOOK = "uidBook"

class ReadFragment : Fragment() {
    private lateinit var binding: FragmentReadBinding
    private var uidBook: String? = null
    private val viewModel: DataViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uidBook = it.getString(ARG_UID_BOOK)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }
    private suspend fun loadCountPage(url: String): String {
        return withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(url).get()
            val bookText = doc.select("div.reading__right").text() // Измените этот селектор в соответствии с вашим сайтом
            bookText.split(" ").last()
        }
    }
    private suspend fun loadTextByPage(url: String): String {
        return withContext(Dispatchers.IO) {
            val doc = Jsoup.connect(url).get()
            val bookText = doc.select("div.reading__text").text() // Измените этот селектор в соответствии с вашим сайтом
            bookText
        }
    }
    private var countPage = 1
    private var nowPage = 1
    private var texts = emptyList<String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        waitingLoad()
        with(binding){
            buttonNext.setOnClickListener {
                if (nowPage != countPage && nowPage < texts.size){
                    nowPage++
                    binding.textViewText.text = texts[nowPage-1]
                    binding.textViewPage.text = "$nowPage/$countPage"
                }
            }
            buttonBack.setOnClickListener {
                if (nowPage != 1){
                    nowPage--
                    binding.textViewText.text = texts[nowPage-1]
                    binding.textViewPage.text = "$nowPage/$countPage"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun waitingLoad(){
        viewLifecycleOwner.lifecycleScope.launch {
            while (viewModel.getUser().uid == "")
                delay(1000)
            try {
                countPage = loadCountPage(viewModel.getBook(uidBook!!).path+"1").toInt()
            }catch (e:Exception){
                Log.e("LoadPages", e.message.toString())
            }
            binding.textViewPage.text = "1/$countPage"
            texts = texts.plus(loadTextByPage(viewModel.getBook(uidBook!!).path+1))

            binding.textViewText.text = texts[nowPage-1]
            binding.progressBar.visibility = View.GONE


            for (page in 2..countPage)
                texts = texts.plus(loadTextByPage(viewModel.getBook(uidBook!!).path+page))
        }
    }

    companion object {
        @JvmStatic
        fun setUidBook(uidBook: String): Bundle {
            return bundleOf(Pair(ARG_UID_BOOK,uidBook))
        }
    }
}