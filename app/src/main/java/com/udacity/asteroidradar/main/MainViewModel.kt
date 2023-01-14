package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.ApiConstant
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.api.NetworkAsteroid
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    //private val _asteroids = MutableLiveData<List<NetworkAsteroid>>()

    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    init {
        val days = getNextSevenDaysFormattedDates()
        viewModelScope.launch {
            repository.listAsteroids(days[0], days.last(), ApiConstant.API_KEY)
        }
        //getAsteroidsList(days[0], days.last(), ApiConstant.API_KEY)
    }

    val asteroids = repository.asteroids



//    private fun getAsteroidsList(startDate: String, endDate: String, apiKey: String) {
//        NasaApi.retrofitService.getAsteroids(startDate, endDate, apiKey).enqueue(object: Callback<String>{
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                val obj = response.body()?.let { JSONObject(it) }
//                _asteroids.value = obj?.let { parseAsteroidsJsonResult(it) }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.i("MainViewModel", "Failure: " + t.message)
//            }
//        })
//    }
class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
}