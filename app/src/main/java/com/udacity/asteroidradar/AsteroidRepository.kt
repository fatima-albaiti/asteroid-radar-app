package com.udacity.asteroidradar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidRepository(private val database: AsteroidsDatabase) {
    private val days = getNextSevenDaysFormattedDates()

    val pictureOfDay: LiveData<PictureOfDay> = Transformations.map( database.asteroidDao.getPictureOfDay()) {
        it.asDomainModel()
    }

    fun getAsteroids(filter: String): LiveData<List<Asteroid>> {
        return Transformations.map(when(filter){
            AsteroidFilter.SAVED -> database.asteroidDao.getAllAsteroids()
            AsteroidFilter.TODAY -> database.asteroidDao.getTodayAsteroids(days[0])
            else -> database.asteroidDao.getWeekAsteroids(days[0], days.last())
        }) {
            it.asDomainModel()
        }
    }

    suspend fun getPictureOfDay(apiKey: String){
        withContext(Dispatchers.IO){
            try {
                val picture = NasaApi.retrofitService.getPictureOfTheDay(apiKey).await()
                database.asteroidDao.insertPicture(picture.asDatabaseModel())
            }

            catch (e: Exception){
                Log.e("AsteroidRepository", e.message.orEmpty())
            }
        }
    }

    suspend fun updateAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsString = NasaApi.retrofitService.getAsteroids(days[0], days.last(), ApiConstant.API_KEY).await()
                val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsString))
                database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
            } catch (e: java.lang.Exception){
                Log.e("AsteroidRepository", e.message.orEmpty())
            }
        }
    }
}