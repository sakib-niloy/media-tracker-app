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

/** Treat OMDb's "N/A" (and blanks) as "no value". */
fun String?.orNullIfNa(): String? =
    this?.trim()?.takeIf { it.isNotEmpty() && !it.equals("N/A", ignoreCase = true) }
