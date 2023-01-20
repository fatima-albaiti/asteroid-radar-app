package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.DatabasePicture
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NetworkPicture (
    @Json(name = "media_type") val mediaType: String, val title: String, val date: String,
    val url: String
        ): Parcelable

fun NetworkPicture.asDatabaseModel(): DatabasePicture {
    return DatabasePicture(
        mediaType = this.mediaType,
        title = this.title,
        date = this.date,
        url = this.url
    )
}