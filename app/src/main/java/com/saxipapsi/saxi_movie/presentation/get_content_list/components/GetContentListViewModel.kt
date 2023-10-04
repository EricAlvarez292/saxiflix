package com.saxipapsi.saxi_movie.presentation.get_content_list.components

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.saxipapsi.saxi_movie.common.ContentTrendingType
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.domain.model.ContentModel
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import com.saxipapsi.saxi_movie.domain.use_case.GetPopularListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetTopRatedListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetTrendingListUseCase
import com.saxipapsi.saxi_movie.domain.use_case.GetUpComingListUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class GetContentListViewModel(
    private val getTrendingListUseCase: GetTrendingListUseCase,
    private val getPopularListUseCase: GetPopularListUseCase,
    private val getTopRatedListUseCase: GetTopRatedListUseCase,
    private val getUpComingListUseCase: GetUpComingListUseCase,
    private val tmdbRepository: TMDBRepository,
    private val savedStateHandle: SavedStateHandle

) : ViewModel() {

    val contentTrendingType: List<ContentTrendingType> = arrayListOf(ContentTrendingType.day, ContentTrendingType.week)
    val contentType: List<ContentType> = arrayListOf(ContentType.movie, ContentType.tv)

    /*Up Coming*/
    private val _upComingHeader = MutableStateFlow(ContentHeaderModel())
    val upComingHeader = _upComingHeader
    val upComingDataFlow: Flow<PagingData<ContentModel>> =
        tmdbRepository.getUpComingMovies().map { pagingData ->
            pagingData.map { dataModel -> dataModel.toContentModel(ViewContentType.ROW_UPCOMING) }
        }.cachedIn(viewModelScope)

    /*Trending*/
    val trendingDataFlow: Flow<PagingData<ContentModel>>
    val trendingSFunction: (UiAction) -> Unit

    /*Popular*/
    val popularDataFlow: Flow<PagingData<ContentModel>>
    val popularFunction: (UiAction) -> Unit

    /*Top Rated*/
    val topRatedSDataFlow: Flow<PagingData<ContentModel>>
    val topRatedSFunction: (UiAction) -> Unit

    init {

        /*trending*/
        val trendingQuery: String = savedStateHandle[TRENDING_CONTENT_TYPE] ?: ContentTrendingType.day.name
        val trendingActionStateFlow = MutableSharedFlow<UiAction>()
        val trendingSearches = trendingActionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            ).onStart { emit(UiAction.Search(query = trendingQuery)) }
        trendingDataFlow = trendingSearches
            .flatMapLatest {
                tmdbRepository.getTrendingContents(it.query).map { pagingData ->
                    pagingData.map { dataModel ->
                        dataModel.toContentModel(ViewContentType.ROW_TRENDING)
                    }
                }
            }.cachedIn(viewModelScope)
        trendingSFunction = { action ->
            viewModelScope.launch { trendingActionStateFlow.emit(action) }
        }

        /*Popular*/
        val popularQuery: String = savedStateHandle[POPULAR_CONTENT_TYPE] ?: ContentType.movie.name
        val popularActionStateFlow = MutableSharedFlow<UiAction>()
        val searches = popularActionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            ).onStart { emit(UiAction.Search(query = popularQuery)) }
        popularDataFlow = searches
            .flatMapLatest {
                tmdbRepository.getPopularContents(it.query).map { pagingData ->
                    pagingData.map { dataModel ->
                        dataModel.toContentModel(ViewContentType.ROW_POPULAR)
                    }
                }
            }.cachedIn(viewModelScope)
        popularFunction = { action ->
            viewModelScope.launch { popularActionStateFlow.emit(action) }
        }

        /*Top RatedS*/
        val topRatedQuery: String =
            savedStateHandle[TOP_RATED_CONTENT_TYPE] ?: ContentType.movie.name
        val topRatedActionStateFlow = MutableSharedFlow<UiAction>()
        val topRatedContentType = topRatedActionStateFlow
            .filterIsInstance<UiAction.Search>()
            .distinctUntilChanged()
            .shareIn(
                scope = viewModelScope,
                started = WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            ).onStart { emit(UiAction.Search(query = topRatedQuery)) }
        topRatedSDataFlow = topRatedContentType
            .flatMapLatest {
                tmdbRepository.getTopRatedContents(it.query).map { pagingData ->
                    pagingData.map { dataModel ->
                        dataModel.toContentModel(ViewContentType.ROW_TOPRATED)
                    }
                }
            }.cachedIn(viewModelScope)
        topRatedSFunction = { action ->
            viewModelScope.launch { topRatedActionStateFlow.emit(action) }
        }
    }


    companion object {
        private const val TRENDING_CONTENT_TYPE: String = "trending_content_type"
        private const val POPULAR_CONTENT_TYPE: String = "popular_content_type"
        private const val TOP_RATED_CONTENT_TYPE: String = "top_rated_content_type"
    }
}


sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class ContentHeaderModel(val title : String? = null, val banner : String? = null)
