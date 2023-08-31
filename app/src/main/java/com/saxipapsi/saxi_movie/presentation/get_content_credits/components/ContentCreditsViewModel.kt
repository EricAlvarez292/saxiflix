package com.saxipapsi.saxi_movie.presentation.get_content_credits.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.domain.model.RecyclerViewContainer
import com.saxipapsi.saxi_movie.domain.use_case.GetCreditsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ContentCreditsViewModel(private val getCreditsUseCase: GetCreditsUseCase) : ViewModel() {

    private val _creditsState: MutableStateFlow<CreditsState> = MutableStateFlow(CreditsState())
    val creditState: StateFlow<CreditsState> = _creditsState
    fun getCredits(content_type: String, content_id: Int) {
        getCreditsUseCase(content_type, content_id).onEach { resource ->
            when (resource) {
                is Resource.Loading -> _creditsState.value = CreditsState(isLoading = true)
                is Resource.Error -> _creditsState.value = CreditsState(error = resource.message ?: "An error occured.")
                is Resource.Success -> _creditsState.value = CreditsState(data = resource.data)
            }
        }.launchIn(viewModelScope)
    }
}

data class CreditsState(
    val isLoading: Boolean = false,
    val data: List<RecyclerViewContainer>? = null,
    val error: String = ""
)
