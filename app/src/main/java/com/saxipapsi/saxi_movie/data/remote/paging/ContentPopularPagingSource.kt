@file:OptIn(ExperimentalPagingApi::class)

package com.saxipapsi.saxi_movie.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.saxipapsi.saxi_movie.data.remote.TMDBApi
import com.saxipapsi.saxi_movie.data.remote.dto.ContentDto
import retrofit2.HttpException
import java.io.IOException

class ContentPopularPagingSource(
    private val contentType: String,
    private val tmdbApi: TMDBApi
) :
    PagingSource<Int, ContentDto>() {
    override fun getRefreshKey(state: PagingState<Int, ContentDto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentDto> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = tmdbApi.getPopularContents(content_type = contentType, page = page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page.minus(1),
                nextKey = if (response.results.isEmpty()) null else page.plus(1)
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}