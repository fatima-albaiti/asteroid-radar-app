package com.udacity.asteroidradar

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidRepository(val database: AsteroidsDatabase) {
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
        it.asDomainModel()
    }

    suspend fun listAsteroids(startDate: String, endDate: String, apiKey: String) {
        withContext(Dispatchers.IO) {
            val asteroidsString = NasaApi.retrofitService.getAsteroids(startDate, endDate, apiKey).await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsString))
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }
}