package com.elesely.quotesapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.elesely.quotesapp.databinding.FragmentHomeBinding
import com.elesely.quotesapp.db.AppDatabase
import com.elesely.quotesapp.db.FavoriteQuoteDao
import com.elesely.quotesapp.db.FavoriteQuotes
import com.elesely.quotesapp.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {

    private lateinit var quoteViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: AppDatabase
    private lateinit var favoriteQuoteDao: FavoriteQuoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the ViewModel
        quoteViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Call fetchRandomQuote to initiate the API request when the fragment is created
        context?.let { quoteViewModel.fetchRandomQuote(it) }

        database = AppDatabase.getDatabase(requireContext())
        favoriteQuoteDao = database.favoriteQuoteDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRandomQuote()

        binding.imgShare.setOnClickListener {
            onShareImageClicked()
        }

        binding.imgFav.setOnClickListener {
            onFavImageClicked()
        }

    }

    private fun onFavImageClicked() {
        val content = binding.quoteContent.text.toString()
        val author = binding.quoteAuthor.text.toString()

        val favoriteQuote = FavoriteQuotes(content = content, author = author)

        // Insert the quote into the favorites
        CoroutineScope(Dispatchers.IO).launch {
            favoriteQuoteDao.insertFavoriteQuote(favoriteQuote)
            showToast("Quote added to favorites")
        }
    }

    private fun onShareImageClicked() {
        val content = binding.quoteContent.text.toString()
        val author = binding.quoteAuthor.text.toString()

        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, "$content \n$author")

        startActivity(Intent.createChooser(share, "Share using"))
    }

    private fun showToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRandomQuote() {
        // Observe quoteLiveData and update the UI
        quoteViewModel.quoteLiveData.observe(viewLifecycleOwner, Observer { quote ->
            if (quote != null) {
                val author = "- ${quote.author}"
                // Start the animation when a new quote is received
                startAnimation(quote.content, author)
            } else {
                // Handle any error or no data scenarios here
            }
        })
    }

    private fun startAnimation(quote: String, author: String) {
        CoroutineScope(Dispatchers.Main).launch {
            // Animate the quote text
            for (i in quote.indices) {
                val quoteSubstring = quote.substring(0, i + 1)

                binding.quoteContent.text = quoteSubstring

                delay(30) // Delay between each character
            }

            // Wait for a moment after the quote text animation is complete
            delay(500)

            // Animate the author's name
            for (i in author.indices) {
                val authorSubstring = author.substring(0, i + 1)

                binding.quoteAuthor.text = authorSubstring

                delay(30) // Delay between each character
            }

        }
    }
}
