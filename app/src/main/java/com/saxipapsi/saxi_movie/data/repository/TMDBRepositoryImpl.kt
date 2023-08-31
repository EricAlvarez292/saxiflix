package com.saxipapsi.saxi_movie.data.repository

import com.saxipapsi.saxi_movie.data.remote.TMDBApi
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDetailsDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentPageDto
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository

class TMDBRepositoryImpl(private val tmdbApi: TMDBApi) : TMDBRepository {
    override suspend fun getContentDetails(contentId: Int, contentType: String): ContentDetailsDto =
        tmdbApi.getContentDetails(contentId = contentId, contentType = contentType)

    override suspend fun getContentVideos(
        content_type: String,
        content_id: Int
    ): ContentVideoDto =
        tmdbApi.getContentVideos(content_type = content_type, content_id = content_id)

    override suspend fun getContentCredits(content_type: String, content_id: Int): CreditsDto =
        tmdbApi.getContentCredits(content_type = content_type, content_id = content_id)

    override suspend fun getUpComingMovies(): ContentPageDto = tmdbApi.getUpComingMovies()

    override suspend fun getTrendingContents(trend_type: String): ContentPageDto =
        tmdbApi.getTrendingContents(trend_type = trend_type)

    override suspend fun getPopularContents(content_type: String): ContentPageDto =
        tmdbApi.getPopularContents(content_type = content_type)

    override suspend fun getTopRatedContents(content_type: String): ContentPageDto =
        tmdbApi.getTopRatedContents(content_type = content_type)
}