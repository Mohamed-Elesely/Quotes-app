package com.elesely.quotesapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_quotes")
data class FavoriteQuotes(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val content: String,
    val author: String
)
