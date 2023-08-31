package com.saxipapsi.saxi_movie.domain.use_case

import android.util.Log
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.domain.model.ContentDetailsModel
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetContentDetailsUseCase(private val tmdbRepository: TMDBRepository) {
    operator fun invoke(contentId: Int, contentType : String): Flow<Resource<ContentDetailsModel>> = flow {
        try {
            Log.d("eric", "LoadGeoLocationUseCase")
            emit(Resource.Loading())
            val data = tmdbRepository.getContentDetails(contentId, contentType).toContentDetailsModel(ContentType.valueOf(contentType))
            val videos = tmdbRepository.getContentVideos(contentType, data.id)
            data.videos = videos
            emit(Resource.Success(data))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}