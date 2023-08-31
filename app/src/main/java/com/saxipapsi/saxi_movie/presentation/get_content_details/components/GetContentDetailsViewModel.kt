package com.saxipapsi.saxi_movie.presentation.get_content_details.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.domain.model.ContentDetailsModel
import com.saxipapsi.saxi_movie.domain.use_case.GetContentDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GetContentDetailsViewModel(private val getContentDetailsUseCase: GetContentDetailsUseCase) :
    ViewModel() {
    private val _state = MutableStateFlow(ContentDetailsState())
    val state: StateFlow<ContentDetailsState> = _state
    fun getContentDetails(contentId: Int, contentType: String) {
        getContentDetailsUseCase(contentId, contentType).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _state.value = ContentDetailsState(isLoading = true)
                is Resource.Error -> _state.value = ContentDetailsState(error = resource.message ?: "Unexpected error occured.")
                is Resource.Success -> _state.value = ContentDetailsState(data = resource.data)
            }
        }.launchIn(viewModelScope)
    }
}

data class ContentDetailsState(
    val isLoading: Boolean = false,
    val data: ContentDetailsModel? = null,
    val error: String = ""
)