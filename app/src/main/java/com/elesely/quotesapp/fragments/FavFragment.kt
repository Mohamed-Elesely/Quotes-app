package com.elesely.quotesapp.fragments

import FavoritesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elesely.quotesapp.databinding.FragmentFavBinding
import com.elesely.quotesapp.db.AppDatabase
import com.elesely.quotesapp.db.FavoriteQuoteDao
import com.elesely.quotesapp.db.FavoriteQuotes
import com.elesely.quotesapp.viewmodel.FavoritesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavFragment : Fragment() {

    private lateinit var favViewModelProvider: FavoritesViewModel
    private lateinit var binding: FragmentFavBinding
    private lateinit var database: AppDatabase
    private lateinit var favoriteQuoteDao: FavoriteQuoteDao
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(requireContext())
        favoriteQuoteDao = database.favoriteQuoteDao()

        // Initialize the ViewModel with the FavoriteQuoteDao
        favViewModelProvider = ViewModelProvider(this)[FavoritesViewModel::class.java]
        favViewModelProvider.init(favoriteQuoteDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and set its layout manager
        val recyclerView = binding.rvFav
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Create an instance of FavoritesAdapter and set it to the RecyclerView
        adapter = FavoritesAdapter(emptyList()) // Start with an empty list
        recyclerView.adapter = adapter

        // Initialize swipe-to-delete functionality
        setupSwipeToDelete(recyclerView)

        // Observe the LiveData in the ViewModel and update the adapter's data when needed
        favViewModelProvider.favoriteQuotes.observe(viewLifecycleOwner, Observer { favoriteQuotes ->
            adapter.favoriteQuotes = favoriteQuotes
            adapter.notifyDataSetChanged()
        })
    }

    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Handle swipe-to-delete here
                val position = viewHolder.adapterPosition
                val favoriteQuote = adapter.favoriteQuotes[position]

                // Delete the swiped item from the database
                deleteFavoriteQuote(favoriteQuote)

                // Show a toast message indicating the quote is deleted
                showToast("Quote Deleted")

                // Update the RecyclerView
                adapter.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun deleteFavoriteQuote(quote: FavoriteQuotes) {
        // Use a Coroutine to perform the deletion on a background thread
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Delete the item from the database
                withContext(Dispatchers.IO) {
                    favoriteQuoteDao.deleteFavoriteQuote(quote)
                }
            } catch (e: Exception) {
                // Handle any errors that may occur during deletion
                // TODO
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
