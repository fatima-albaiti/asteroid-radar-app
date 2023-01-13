package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.ApiConstant
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _asteroids = MutableLiveData<ArrayList<Asteroid>>()
    val asteroids: LiveData<ArrayList<Asteroid>> get() = _asteroids

    init {
        val days = getNextSevenDaysFormattedDates()
        getAsteroidsList(days[0], days.last(), ApiConstant.API_KEY)
    }


    private fun getAsteroidsList(startDate: String, endDate: String, apiKey: String) {
        NasaApi.retrofitService.getAsteroids(startDate, endDate, apiKey).enqueue(object: Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val obj = response.body()?.let { JSONObject(it) }
                _asteroids.value = obj?.let { parseAsteroidsJsonResult(it) }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("MainViewModel", "Failure: " + t.message)
            }
        })
    }
}