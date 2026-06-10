package com.example.movielist

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MovieRepository(
    private val dao: MovieDao,
    private val api: OmdbApi = OmdbApi.create()
) {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val currentUser get() = auth.currentUser

    fun toWatch(userId: String?): Flow<List<Movie>> = dao.getToWatch(userId)
    fun watched(userId: String?): Flow<List<Movie>> = dao.getWatched(userId)

    suspend fun add(movie: Movie): Long {
        val withUser = movie.copy(userId = currentUser?.uid)
        val id = dao.insert(withUser)
        syncToRemote(id)
        return id
    }

    suspend fun update(movie: Movie) {
        dao.update(movie)
        syncToRemote(movie.id)
    }

    suspend fun delete(movie: Movie) {
        dao.delete(movie)
        movie.remoteId?.let { remoteId ->
            try {
                firestore.collection("movies").document(remoteId).delete().await()
            } catch (_: Exception) {}
        }
    }

    suspend fun markWatched(movie: Movie) {
        val updated = movie.copy(watched = true, watchedAt = System.currentTimeMillis())
        dao.update(updated)
        syncToRemote(updated.id)
    }

    private suspend fun syncToRemote(localId: Long) = withContext(Dispatchers.IO) {
        val movie = dao.getById(localId) ?: return@withContext
        val userId = currentUser?.uid ?: return@withContext
        
        try {
            val data = hashMapOf(
                "title" to movie.title,
                "posterUrl" to movie.posterUrl,
                "year" to movie.year,
                "director" to movie.director,
                "casts" to movie.casts,
                "watched" to movie.watched,
                "watchedAt" to movie.watchedAt,
                "imdbId" to movie.imdbId,
                "imdbRating" to movie.imdbRating,
                "userId" to userId
            )

            if (movie.remoteId == null) {
                val ref = firestore.collection("movies").add(data).await()
                dao.update(movie.copy(remoteId = ref.id, userId = userId))
            } else {
                firestore.collection("movies").document(movie.remoteId).set(data).await()
            }
        } catch (_: Exception) {}
    }

    suspend fun syncFromRemote() = withContext(Dispatchers.IO) {
        val userId = currentUser?.uid ?: return@withContext
        try {
            val snapshot = firestore.collection("movies")
                .whereEqualTo("userId", userId)
                .get().await()
            
            snapshot.documents.forEach { doc ->
                val remoteId = doc.id
                if (dao.getByRemoteId(remoteId) != null) return@forEach

                val movie = Movie(
                    title = doc.getString("title") ?: return@forEach,
                    posterUrl = doc.getString("posterUrl"),
                    year = doc.getLong("year")?.toInt(),
                    director = doc.getString("director"),
                    casts = doc.getString("casts"),
                    watched = doc.getBoolean("watched") ?: false,
                    watchedAt = doc.getLong("watchedAt"),
                    imdbId = doc.getString("imdbId"),
                    imdbRating = doc.getString("imdbRating"),
                    userId = userId,
                    remoteId = remoteId,
                    detailsFetched = true
                )
                dao.insert(movie)
            }
        } catch (_: Exception) {}
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
                        imdbRating = resp.imdbRating.orNullIfNa(),
                        detailsFetched = true
                    )
                )
            }
        } catch (_: Exception) {
            // Stay pending; ConnectivityObserver will trigger a retry later.
        }
    }
}
