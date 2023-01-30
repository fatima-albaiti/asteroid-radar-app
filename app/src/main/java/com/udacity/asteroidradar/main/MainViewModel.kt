package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.ApiConstant
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidFilter
import com.udacity.asteroidradar.AsteroidRepository
import com.udacity.asteroidradar.database.getDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    private val _navigateToDetails = MutableLiveData<Asteroid>()
    val navigateToDetails: LiveData<Asteroid> get() = _navigateToDetails

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>> get() = _asteroids

    private val asteroidsFilter = MutableLiveData<String>()

    private val asteroidsList = Transformations.switchMap(asteroidsFilter) {
        filter -> repository.getAsteroids(filter)
    }

    init {
        asteroidsList.observeForever {
            _asteroids.value = it
        }

        asteroidsFilter.value = AsteroidFilter.SAVED

        viewModelScope.launch {
            repository.updateAsteroids()
            repository.getPictureOfDay(ApiConstant.API_KEY)
        }
    }

    val picture = repository.pictureOfDay

    fun onNavigateToAsteroidDetails(asteroid: Asteroid){
        _navigateToDetails.value = asteroid
    }

    fun onNavigateToAsteroidDetailsComplete(){
        _navigateToDetails.value = null
    }

    fun viewWeekAsteroids(){
        asteroidsFilter.value = AsteroidFilter.WEEK
    }

    fun viewTodayAsteroids(){
        asteroidsFilter.value = AsteroidFilter.TODAY
    }

    fun viewSavedAsteroids(){
        asteroidsFilter.value = AsteroidFilter.SAVED
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}