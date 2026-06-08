package com.example.movielist

/** UI state for the live "search a movie to add" field. */
sealed interface SearchState {
    /** Query too short — nothing to show yet. */
    data object Idle : SearchState

    /** Device is offline; we can only save the typed name for now. */
    data object Offline : SearchState

    /** A search request is in flight. */
    data object Loading : SearchState

    /** Matches to choose from. */
    data class Results(val items: List<OmdbSearchItem>) : SearchState

    /** No matches / lookup failed. */
    data class Empty(val message: String) : SearchState
}
