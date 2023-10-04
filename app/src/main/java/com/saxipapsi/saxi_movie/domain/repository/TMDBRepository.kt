package com.saxipapsi.saxi_movie.domain.repository

import androidx.paging.PagingData
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDetailsDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentPageDto
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import kotlinx.coroutines.flow.Flow

interface TMDBRepository {
    fun getTrendingContents(trend_type: String): Flow<PagingData<ContentDto>>
    fun getPopularContents(content_type: String): Flow<PagingData<ContentDto>>
    fun getTopRatedContents(content_type: String): Flow<PagingData<ContentDto>>
    fun getUpComingMovies(): Flow<PagingData<ContentDto>>
    suspend fun getContentDetails(contentId: Int, contentType: String): ContentDetailsDto
    suspend fun getContentVideos(content_type: String, content_id: Int): ContentVideoDto
    suspend fun getContentCredits(content_type: String, content_id: Int): CreditsDto
}