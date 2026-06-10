package com.example.movielist

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie): Long

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getById(id: Long): Movie?

    @Query("SELECT * FROM movies WHERE remoteId = :remoteId")
    suspend fun getByRemoteId(remoteId: String): Movie?

    @Query("SELECT * FROM movies WHERE detailsFetched = 0")
    suspend fun getPending(): List<Movie>

    @Query("SELECT * FROM movies WHERE watched = 0 AND (userId = :userId OR userId IS NULL) ORDER BY title COLLATE NOCASE ASC")
    fun getToWatch(userId: String?): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE watched = 1 AND (userId = :userId OR userId IS NULL) ORDER BY title COLLATE NOCASE ASC")
    fun getWatched(userId: String?): Flow<List<Movie>>
}
