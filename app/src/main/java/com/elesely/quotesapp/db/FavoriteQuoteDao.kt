package com.elesely.quotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteQuoteDao {
    @Query("SELECT * FROM favorite_quotes")
    fun getAllFavoriteQuotes(): LiveData<List<FavoriteQuotes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteQuote(quote: FavoriteQuotes)

    @Delete
    suspend fun deleteFavoriteQuote(quote: FavoriteQuotes)
}
