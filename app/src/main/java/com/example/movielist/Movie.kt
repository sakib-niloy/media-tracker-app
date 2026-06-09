package com.example.movielist

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val posterUrl: String?,
    val year: Int?,
    val director: String?,
    val casts: String?, // comma-separated
    val watched: Boolean = false,
    val watchedAt: Long? = null,
    // The exact OMDb/IMDb id chosen from search results. When present we fetch
    // details by id (precise) instead of by title (best-guess).
    val imdbId: String? = null,
    val imdbRating: String? = null,
    // false until OMDb details (poster/year/director/casts) have been fetched.
    // Movies added while offline start false and are filled in once back online.
    val detailsFetched: Boolean = false
)
