package com.saxipapsi.saxi_movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.saxipapsi.saxi_movie.data.remote.TMDBApi
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDetailsDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDto
import com.saxipapsi.saxi_movie.data.remote.dto.ContentPageDto
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import com.saxipapsi.saxi_movie.data.remote.paging.ContentPopularPagingSource
import com.saxipapsi.saxi_movie.data.remote.paging.ContentTopRatedPagingSource
import com.saxipapsi.saxi_movie.data.remote.paging.ContentTrendingPagingSource
import com.saxipapsi.saxi_movie.data.remote.paging.ContentUpComingPagingSource
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import kotlinx.coroutines.flow.Flow

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

    override fun getUpComingMovies(): Flow<PagingData<ContentDto>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = { ContentUpComingPagingSource(tmdbApi) }).flow

    override fun getTrendingContents(trend_type: String): Flow<PagingData<ContentDto>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = { ContentTrendingPagingSource(trend_type, tmdbApi) }).flow

    override fun getPopularContents(content_type: String): Flow<PagingData<ContentDto>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = { ContentPopularPagingSource(content_type, tmdbApi) }).flow

    override fun getTopRatedContents(content_type: String): Flow<PagingData<ContentDto>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        pagingSourceFactory = { ContentTopRatedPagingSource(content_type, tmdbApi) }).flow
//        tmdbApi.getTopRatedContents(content_type = content_type)


    companion object {
        const val NETWORK_PAGE_SIZE = 20
    }
}