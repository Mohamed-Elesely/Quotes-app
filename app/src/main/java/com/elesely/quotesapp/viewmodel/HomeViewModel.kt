package com.elesely.quotesapp.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elesely.quotesapp.pojo.Quotes
import com.elesely.quotesapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _quoteLiveData = MutableLiveData<Quotes>()
    val quoteLiveData: LiveData<Quotes> get() = _quoteLiveData

    fun fetchRandomQuote(context: Context) {
        val call = RetrofitInstance.api.getRandomQuote()

        call.enqueue(object : Callback<Quotes> {
            override fun onResponse(call: Call<Quotes>, response: Response<Quotes>) {
                if (response.isSuccessful) {
                    val quote = response.body()
                    _quoteLiveData.postValue(quote)
                } else {
                    showToast(context, "Network error")
                }
            }

            override fun onFailure(call: Call<Quotes>, t: Throwable) {
                showToast(context, "Network error")
            }
        })
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
