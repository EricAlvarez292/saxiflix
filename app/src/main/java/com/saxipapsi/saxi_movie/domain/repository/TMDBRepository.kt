package com.saxipapsi.saxi_movie.domain.repository

import com.saxipapsi.saxi_movie.data.remote.dto.ContentDetailsDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentPageDto
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBRepository {
    suspend fun getTrendingContents(trend_type: String): ContentPageDto
    suspend fun getPopularContents(content_type: String): ContentPageDto
    suspend fun getTopRatedContents(content_type: String): ContentPageDto
    suspend fun getUpComingMovies(): ContentPageDto
    suspend fun getContentDetails(contentId: Int, contentType: String): ContentDetailsDto
    suspend fun getContentVideos(content_type: String, content_id: Int): ContentVideoDto
    suspend fun getContentCredits(content_type: String, content_id: Int): CreditsDto
}