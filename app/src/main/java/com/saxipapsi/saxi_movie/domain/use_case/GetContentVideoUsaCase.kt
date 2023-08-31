package com.saxipapsi.saxi_movie.domain.use_case

import android.util.Log
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetContentVideoUsaCase(private val tmdbRepository: TMDBRepository) {
    operator fun invoke(contentId: Int, contentType: String): Flow<Resource<ContentVideoDto>> =
        flow {
            try {
                Log.d("eric", "LoadGeoLocationUseCase")
                emit(Resource.Loading())
                val data = tmdbRepository.getContentVideos(contentType, contentId)
                emit(Resource.Success(data))
            } catch (e: HttpException) {
                emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
            } catch (e: IOException) {
                emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
            }
        }
}