package com.saxipapsi.saxi_movie.presentation.get_content_list.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.saxi_movie.common.ContentTrendingType
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.domain.model.ContentHeaderModel
import com.saxipapsi.saxi_movie.domain.model.ContentModel
import com.saxipapsi.saxi_movie.domain.use_case.GetPopularListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetTopRatedListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetTrendingListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetUpComingListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GetContentListViewModel(
    private val getTrendingListUseCase: GetTrendingListUseCase,
    private val getPopularListUseCase: GetPopularListUseCase,
    private val getTopRatedListUseCase: GetTopRatedListUseCase,
    private val getUpComingListUseCase: GetUpComingListUseCase
) : ViewModel() {

    val  contentTrendingType : List<ContentTrendingType> = arrayListOf(ContentTrendingType.day, ContentTrendingType.week)
    val  contentType : List<ContentType> = arrayListOf(ContentType.movie, ContentType.tv)

    private val _upComingState = MutableStateFlow(ContentListState())
    val upComingState: StateFlow<ContentListState> = _upComingState.apply { getUpComingList() }
    private fun getUpComingList() {
        val header = ContentHeaderModel(title = "Upcoming Soon")
        getUpComingListUseCase().onEach { resource ->
            when (resource) {
                is Resource.Loading -> _upComingState.value = ContentListState(header = header, isLoading = true)
                is Resource.Error -> _upComingState.value = ContentListState(header = header,error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> {
                    header.banner = resource.data?.asSequence()?.shuffled()?.find { true }?.banner /*"/hPcP1kv6vrkRmQO3YgV1H97FE5Q.jpg"*/
                    _upComingState.value = ContentListState(header = header,data = resource.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    private val _trendingState = MutableStateFlow(ContentListState())
    val trendingState: StateFlow<ContentListState> = _trendingState.apply { getTrendingList() }
    fun getTrendingList(contentTrendingType: ContentTrendingType = ContentTrendingType.day) {
        val header = ContentHeaderModel(title = "What's Trending")
        getTrendingListUseCase(contentTrendingType).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _trendingState.value = ContentListState(header = header, isLoading = true)
                is Resource.Error -> _trendingState.value = ContentListState(header = header, error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _trendingState.value = ContentListState(header = header, data = resource.data)
            }
        }.launchIn(viewModelScope)
    }

    private val _popularState = MutableStateFlow(ContentListState())
    val popularState: StateFlow<ContentListState> = _popularState.apply { getPopularList() }
    fun getPopularList(contentType: ContentType = ContentType.movie) {
        val header = ContentHeaderModel(title = "What's Popular")
        getPopularListUseCase(contentType).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _popularState.value = ContentListState(header = header, isLoading = true)
                is Resource.Error -> _popularState.value = ContentListState(header = header, error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _popularState.value = ContentListState(header = header, data = resource.data)
            }
        }.launchIn(viewModelScope)
    }

    private val _topRatedState = MutableStateFlow(ContentListState())
    val topRatedState: StateFlow<ContentListState> = _topRatedState.apply { getTopTrendList() }
    fun getTopTrendList(contentType: ContentType = ContentType.movie) {
        val header = ContentHeaderModel(title = "Top Rated")
        getTopRatedListUseCase(contentType).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _topRatedState.value = ContentListState(header = header, isLoading = true)
                is Resource.Error -> _topRatedState.value = ContentListState(header = header, error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _topRatedState.value = ContentListState(header = header, data = resource.data)
            }
        }.launchIn(viewModelScope)
    }
}

data class ContentListState(
    val isLoading: Boolean = false,
    val header: ContentHeaderModel? = null,
    val data: List<ContentModel>? = null,
    val error: String = ""
)