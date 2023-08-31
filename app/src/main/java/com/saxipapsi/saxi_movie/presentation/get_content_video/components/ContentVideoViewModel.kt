package com.saxipapsi.saxi_movie.presentation.get_content_video.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import com.saxipapsi.saxi_movie.domain.use_case.GetContentVideoUsaCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ContentVideoViewModel(
    private val getContentVideoUsaCase: GetContentVideoUsaCase
) : ViewModel() {

    private val _videoState: MutableStateFlow<ContentVideoState> = MutableStateFlow(ContentVideoState())
    val videoState: StateFlow<ContentVideoState> = _videoState
    fun getContentVideo(contentId: Int, contentType : String) {
        getContentVideoUsaCase(contentId, contentType).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _videoState.value = ContentVideoState(isLoading = true)
                is Resource.Error -> _videoState.value = ContentVideoState(error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _videoState.value = ContentVideoState(data = resource.data)
            }
        }.launchIn(viewModelScope)
    }
}

data class ContentVideoState(
    val isLoading: Boolean = false,
    val data: ContentVideoDto? = null,
    val error: String = ""
)