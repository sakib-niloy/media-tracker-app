package com.example.movielist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MovieDatabase.getInstance(application)
    private val repo = MovieRepository(db.movieDao())
    private val connectivity = ConnectivityObserver(application)

    val toWatch: StateFlow<List<Movie>> = repo.toWatch()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val watched: StateFlow<List<Movie>> = repo.watched()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val isOnline: StateFlow<Boolean> = connectivity.isOnline
        .stateIn(viewModelScope, SharingStarted.Eagerly, connectivity.isCurrentlyOnline())

    // --- Live search -------------------------------------------------------

    private val query = MutableStateFlow("")

    val searchState: StateFlow<SearchState> = query
        .map { it.trim() }
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { q ->
            flow {
                when {
                    q.length < 2 -> emit(SearchState.Idle)
                    !connectivity.isCurrentlyOnline() -> emit(SearchState.Offline)
                    else -> {
                        emit(SearchState.Loading)
                        emit(repo.search(q))
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SearchState.Idle)

    fun onSearchQueryChanged(text: String) {
        query.value = text
    }

    init {
        // When the device (re)gains connectivity, fill in details for any
        // movies that were added while offline.
        viewModelScope.launch {
            isOnline.collect { online ->
                if (online) syncPendingDetails()
            }
        }
    }

    // --- Adding ------------------------------------------------------------

    /** Add the exact movie the user picked from the search results. */
    fun addFromSearch(item: OmdbSearchItem) = viewModelScope.launch {
        val title = item.title?.trim().orEmpty()
        if (title.isEmpty()) return@launch
        val movie = Movie(
            title = title,
            posterUrl = item.poster.orNullIfNa(),
            year = item.year.orNullIfNa()?.filter { it.isDigit() }?.take(4)?.toIntOrNull(),
            director = null,
            casts = null,
            imdbId = item.imdbId.orNullIfNa(),
            detailsFetched = false
        )
        val id = repo.add(movie)
        if (connectivity.isCurrentlyOnline()) repo.fetchDetailsFor(id)
    }

    /**
     * Add by name only (offline, or "add it anyway" when no match is found).
     * Details are fetched automatically once the device is online.
     */
    fun addByName(name: String) = viewModelScope.launch {
        val title = name.trim()
        if (title.isEmpty()) return@launch
        val id = repo.add(Movie(title = title, posterUrl = null, year = null, director = null, casts = null))
        if (connectivity.isCurrentlyOnline()) repo.fetchDetailsFor(id)
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
