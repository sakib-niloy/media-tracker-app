package com.example.movielist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MovieDatabase.getInstance(application)
    private val repo = MovieRepository(db.movieDao())
    private val connectivity = ConnectivityObserver(application)

    val toWatch: StateFlow<List<Movie>> = repo.toWatch()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val watched: StateFlow<List<Movie>> = repo.watched()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        // Whenever the device (re)gains connectivity, fill in details for any
        // movies that were added while offline.
        viewModelScope.launch {
            connectivity.isOnline.collect { online ->
                if (online) syncPendingDetails()
            }
        }
    }

    /**
     * Add a movie by name only. It is stored immediately so it shows up offline;
     * if we're online the rest of the details are fetched right away, otherwise
     * they'll be filled in automatically once the device is back online.
     */
    fun addMovie(name: String) = viewModelScope.launch {
        val title = name.trim()
        if (title.isEmpty()) return@launch
        val id = repo.add(Movie(title = title, posterUrl = null, year = null, director = null, casts = null))
        if (connectivity.isCurrentlyOnline()) {
            repo.fetchDetailsFor(id)
        }
    }

    private suspend fun syncPendingDetails() {
        repo.pendingMovies().forEach { repo.fetchDetailsFor(it.id) }
    }

    fun markWatched(movie: Movie) = viewModelScope.launch {
        repo.markWatched(movie)
    }

    fun delete(movie: Movie) = viewModelScope.launch {
        repo.delete(movie)
    }
}
