package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.udacity.asteroidradar.PictureOfDay

@Entity
data class DatabasePicture (
    @Json(name = "media_type") val mediaType: String, val title: String,
    @PrimaryKey
    val date: String,
    val url: String
        )

fun DatabasePicture.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        title = this.title,
        mediaType = this.mediaType,
        url = this.url
    )
}
