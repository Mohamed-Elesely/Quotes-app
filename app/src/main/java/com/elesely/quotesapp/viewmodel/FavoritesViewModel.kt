package com.elesely.quotesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.elesely.quotesapp.db.FavoriteQuotes
import com.elesely.quotesapp.db.FavoriteQuoteDao

class FavoritesViewModel : ViewModel() {
    private lateinit var favoriteQuoteDao: FavoriteQuoteDao

    fun init(favoriteQuoteDao: FavoriteQuoteDao) {
        this.favoriteQuoteDao = favoriteQuoteDao
    }

    val favoriteQuotes: LiveData<List<FavoriteQuotes>> by lazy {
        favoriteQuoteDao.getAllFavoriteQuotes()
    }
}
