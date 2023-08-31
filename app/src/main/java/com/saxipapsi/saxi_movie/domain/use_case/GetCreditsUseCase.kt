package com.saxipapsi.saxi_movie.domain.use_case

import com.saxipapsi.saxi_movie.common.Resource
import com.saxipapsi.saxi_movie.data.remote.dto.credits.CreditsDto
import com.saxipapsi.saxi_movie.domain.model.CreditsModel
import com.saxipapsi.saxi_movie.domain.model.RecyclerViewContainer
import com.saxipapsi.saxi_movie.domain.repository.TMDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetCreditsUseCase(private val tmdbRepository: TMDBRepository) {
    operator fun invoke(content_type: String, content_id: Int): Flow<Resource<List<RecyclerViewContainer>>> = flow {
        try {
            emit(Resource.Loading())
            val data = tmdbRepository.getContentCredits(content_type, content_id)
            val castList = data.cast.map { it.toCreditsModel() }
            val crewList = data.crew.map { it.toCreditsModel() }
            val allData = castList + crewList
            val allDataMap = allData.groupBy { it.department }
            val result : MutableList<RecyclerViewContainer> = mutableListOf()
            for (key in allDataMap.keys){
                result.add(RecyclerViewContainer(isHeader = true, title = key))
                val mapValue : List<CreditsModel> = allDataMap[key] ?: listOf()
                val mapCredits = mapValue.map { it.toRecyclerViewContainer() }
                result.addAll(mapCredits)
            }
            emit(Resource.Success(result))
        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "An error occured."))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach server. Please check your internet connection"))
        }
    }
}