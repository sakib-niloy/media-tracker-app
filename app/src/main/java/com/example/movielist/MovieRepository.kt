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

    /** Live search by title — returns the matches to choose from. */
    suspend fun search(query: String): SearchState = withContext(Dispatchers.IO) {
        try {
            val resp = api.search(query)
            val items = resp.search.orEmpty().filter { !it.imdbId.isNullOrBlank() }
            if (resp.isFound && items.isNotEmpty()) {
                SearchState.Results(items)
            } else {
                SearchState.Empty(resp.error.orNullIfNa() ?: "No matches found")
            }
        } catch (_: Exception) {
            SearchState.Empty("Couldn't search — check your connection.")
        }
    }

    /**
     * Fill in poster/year/director/casts for a stored movie. Uses the precise
     * IMDb id when we have one (chosen from search), otherwise a best-guess by
     * title. On any failure the movie stays pending and is retried when online.
     */
    suspend fun fetchDetailsFor(id: Long) = withContext(Dispatchers.IO) {
        val movie = dao.getById(id) ?: return@withContext
        try {
            val resp = if (!movie.imdbId.isNullOrBlank()) {
                api.getByImdbId(movie.imdbId)
            } else {
                api.getByTitle(movie.title)
            }
            if (resp.isFound) {
                dao.update(
                    movie.copy(
                        title = resp.title.orNullIfNa() ?: movie.title,
                        posterUrl = resp.poster.orNullIfNa() ?: movie.posterUrl,
                        year = resp.year.orNullIfNa()
                            ?.filter { it.isDigit() }?.take(4)?.toIntOrNull() ?: movie.year,
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
