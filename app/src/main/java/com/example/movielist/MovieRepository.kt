package com.example.movielist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MovieRepository(
    private val dao: MovieDao,
    private val api: OmdbApi = OmdbApi.create()
) {
    fun toWatch(): Flow<List<Movie>> = dao.getToWatch()
    fun watched(): Flow<List<Movie>> = dao.getWatched()

    suspend fun add(movie: Movie): Long = dao.insert(movie)
    suspend fun update(movie: Movie) = dao.update(movie)
    suspend fun delete(movie: Movie) = dao.delete(movie)

    suspend fun markWatched(movie: Movie) {
        val updated = movie.copy(watched = true, watchedAt = System.currentTimeMillis())
        dao.update(updated)
    }

    /** Movies that still need their OMDb details fetched (e.g. added while offline). */
    suspend fun pendingMovies(): List<Movie> = dao.getPending()

    /**
     * Look the movie up on OMDb by title and fill in poster/year/director/casts.
     * On any failure (no network, no/invalid API key, title not found) the movie is
     * left as-is with detailsFetched = false so it will be retried when back online.
     */
    suspend fun fetchDetailsFor(id: Long) = withContext(Dispatchers.IO) {
        val movie = dao.getById(id) ?: return@withContext
        try {
            val resp = api.getByTitle(movie.title)
            if (resp.isFound) {
                dao.update(
                    movie.copy(
                        title = resp.title.orNullIfNa() ?: movie.title,
                        posterUrl = resp.poster.orNullIfNa(),
                        year = resp.year.orNullIfNa()
                            ?.filter { it.isDigit() }?.take(4)?.toIntOrNull(),
                        director = resp.director.orNullIfNa(),
                        casts = resp.actors.orNullIfNa(),
                        detailsFetched = true
                    )
                )
            }
        } catch (_: Exception) {
            // Stay pending; ConnectivityObserver will trigger a retry later.
        }
    }
}
