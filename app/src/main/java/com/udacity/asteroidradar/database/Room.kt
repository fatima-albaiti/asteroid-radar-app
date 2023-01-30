package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid where closeApproachDate between :from and :to order by closeApproachDate")
    fun getWeekAsteroids(from: String, to: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid order by closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid where closeApproachDate is :today")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(picture: DatabasePicture)

    @Query("select * from databasepicture order by date desc limit 1")
    fun getPictureOfDay(): LiveData<DatabasePicture>
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicture::class], version = 2)
abstract class AsteroidsDatabase: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
        return INSTANCE
    }
}
