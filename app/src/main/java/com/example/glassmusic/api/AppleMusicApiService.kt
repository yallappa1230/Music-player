package com.example.glassmusic.api

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// Models matching official Apple Music REST API schema
data class AppleMusicSearchResponse(
    val results: AppleMusicSearchResults? = null
)

data class AppleMusicSearchResults(
    val songs: AppleMusicSongCollection? = null,
    val albums: AppleMusicAlbumCollection? = null
)

data class AppleMusicSongCollection(
    val data: List<AppleMusicSongItem> = emptyList()
)

data class AppleMusicAlbumCollection(
    val data: List<AppleMusicAlbumItem> = emptyList()
)

data class AppleMusicSongItem(
    val id: String,
    val type: String,
    val href: String? = null,
    val attributes: AppleMusicSongAttributes? = null
)

data class AppleMusicSongAttributes(
    val name: String,
    val artistName: String,
    val albumName: String? = null,
    val durationInMillis: Long? = null,
    val artwork: AppleMusicArtwork? = null,
    val previews: List<AppleMusicPreview>? = null,
    val genreNames: List<String>? = null,
    val releaseDate: String? = null
)

data class AppleMusicAlbumItem(
    val id: String,
    val type: String,
    val attributes: AppleMusicAlbumAttributes? = null
)

data class AppleMusicAlbumAttributes(
    val name: String,
    val artistName: String,
    val artwork: AppleMusicArtwork? = null,
    val trackCount: Int? = null,
    val genreNames: List<String>? = null,
    val releaseDate: String? = null
)

data class AppleMusicArtwork(
    val url: String,
    val width: Int? = null,
    val height: Int? = null,
    @Json(name = "bgColor") val bgColor: String? = null
) {
    fun formatUrl(targetSize: Int = 600): String {
        return url.replace("{w}", targetSize.toString())
            .replace("{h}", targetSize.toString())
    }
}

data class AppleMusicPreview(
    val url: String
)

interface AppleMusicApi {

    @GET("v1/catalog/{storefront}/search")
    suspend fun searchCatalog(
        @Header("Authorization") authorization: String,
        @Header("Music-User-Token") userToken: String? = null,
        @Path("storefront") storefront: String,
        @Query("term") term: String,
        @Query("types") types: String = "songs,albums",
        @Query("limit") limit: Int = 25
    ): AppleMusicSearchResponse

    @GET("v1/catalog/{storefront}/charts")
    suspend fun getCharts(
        @Header("Authorization") authorization: String,
        @Header("Music-User-Token") userToken: String? = null,
        @Path("storefront") storefront: String,
        @Query("types") types: String = "songs,albums",
        @Query("limit") limit: Int = 20
    ): AppleMusicSearchResponse
}

object AppleMusicApiClient {
    private const val BASE_URL = "https://api.music.apple.com/"

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    val api: AppleMusicApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AppleMusicApi::class.java)
    }
}
