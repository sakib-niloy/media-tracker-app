package com.example.movielist

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Minimal client for the OMDb API (https://www.omdbapi.com).
 *
 * A single GET by title returns everything we need to fill in a movie.
 *
 * SETUP: get a free API key from https://www.omdbapi.com/apikey.aspx
 * and paste it into [API_KEY] below. Until you do, lookups will fail
 * gracefully and movies will simply stay as name-only entries.
 */
interface OmdbApi {

    @GET("/")
    suspend fun getByTitle(
        @Query("t") title: String,
        @Query("apikey") apiKey: String = API_KEY
    ): OmdbResponse

    /** Full details for one exact movie, looked up by its IMDb id. */
    @GET("/")
    suspend fun getByImdbId(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String = API_KEY
    ): OmdbResponse

    /** Search by title — returns up to 10 matches (multiple movies can share a title). */
    @GET("/")
    suspend fun search(
        @Query("s") query: String,
        @Query("apikey") apiKey: String = API_KEY
    ): OmdbSearchResponse

    companion object {
        // TODO: replace with your own free OMDb API key.
        const val API_KEY = "a1c21eaa"

        fun create(): OmdbApi = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbApi::class.java)
    }
}

/**
 * OMDb returns string fields with "N/A" when a value is unknown,
 * and Response = "True"/"False" to signal whether the title was found.
 */
data class OmdbResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?
) {
    val isFound: Boolean get() = response.equals("True", ignoreCase = true)
}

/** Response for the search endpoint (s=). */
data class OmdbSearchResponse(
    @SerializedName("Search") val search: List<OmdbSearchItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String?,
    @SerializedName("Error") val error: String?
) {
    val isFound: Boolean get() = response.equals("True", ignoreCase = true)
}

/** A single search hit. Has just enough to show the user a choice. */
data class OmdbSearchItem(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("imdbID") val imdbId: String?,
    @SerializedName("Type") val type: String?,
    @SerializedName("Poster") val poster: String?
)

/** Treat OMDb's "N/A" (and blanks) as "no value". */
fun String?.orNullIfNa(): String? =
    this?.trim()?.takeIf { it.isNotEmpty() && !it.equals("N/A", ignoreCase = true) }
