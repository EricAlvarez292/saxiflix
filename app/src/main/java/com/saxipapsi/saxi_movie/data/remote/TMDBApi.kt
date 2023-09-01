package com.saxipapsi.saxi_movie.data.remote

import com.saxipapsi.saxi_movie.common.API
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDetailsDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentPageDto
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import retrofit2.http.*

interface TMDBApi {
    @GET(API.TMDBApi.POPULAR_CONTENTS)
    suspend fun getPopularContents(
        @Path("content_type") content_type: String,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentPageDto

    @GET(API.TMDBApi.CONTENT_DETAIL)
    suspend fun getContentDetails(
        @Path("content_id") contentId: Int,
        @Path("content_type") contentType: String,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentDetailsDto

    @GET(API.TMDBApi.TRENDING_CONTENTS)
    suspend fun getTrendingContents(
        @Path("trend_type") trend_type: String,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentPageDto

    @GET(API.TMDBApi.TOP_RATED_CONTENTS)
    suspend fun getTopRatedContents(
        @Path("content_type") content_type: String,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentPageDto

    @GET(API.TMDBApi.CONTENT_VIDEOS)
    suspend fun getContentVideos(
        @Path("content_type") content_type: String,
        @Path("content_id") content_id: Int,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentVideoDto

    @GET(API.TMDBApi.UPCOMING_MOVIES)
    suspend fun getUpComingMovies(
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): ContentPageDto

    @GET(API.TMDBApi.CREDITS)
    suspend fun getContentCredits(
        @Path("content_type") content_type: String,
        @Path("content_id") content_id: Int,
        @Query("api_key") api_key: String = "7f4100de1843c4b40555f1a0159d4276"
    ): CreditsDto
}