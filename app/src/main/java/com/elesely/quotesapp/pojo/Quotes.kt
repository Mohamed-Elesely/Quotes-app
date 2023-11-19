package com.elesely.quotesapp.pojo


import com.google.gson.annotations.SerializedName

data class Quotes(
    val author: String,
    val authorSlug: String,
    val content: String,
    val dateAdded: String,
    val dateModified: String,
    @SerializedName("_id")
    val id: String,
    val length: Int,
    val tags: List<String>
)