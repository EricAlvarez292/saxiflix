package com.saxipapsi.saxi_movie.domain.use_case

import android.util.Log
import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.domain.model.ContentHeaderModel
import com.saxipapsi.saxi_movie.domain.model.ContentModel
import com.saxipapsi.saxi_movie.domain.model.RecyclerViewContainer
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.ViewContentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetTopRatedListUseCase (private val tmdbRepository: TMDBRepository) {
    operator fun invoke(contentType: ContentType): Flow<Resource<List<ContentModel>>> = flow {
        try {
            Log.d("eric", "LoadGeoLocationUseCase")
            emit(Resource.Loading())
            val data = tmdbRepository.getTopRatedContents(contentType.name)
//            val contentList = data.results.map { it.toContentModel(ViewContentType.ROW_TOPRATED) }
//            emit(Resource.Success(contentList))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}