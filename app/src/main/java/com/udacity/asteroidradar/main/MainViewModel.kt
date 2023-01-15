package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.ApiConstant
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    init {
        val days = getNextSevenDaysFormattedDates()
        viewModelScope.launch {
            repository.listAsteroids(days[0], days.last(), ApiConstant.API_KEY)
        }
    }

    val asteroids = repository.asteroids

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