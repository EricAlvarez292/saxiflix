package com.saxipapsi.saxi_movie.data.remote.dto

import com.saxipapsi.saxi_movie.domain.model.ContentModel
import com.saxipapsi.saxi_movie.presentation.get_content_list.components.ViewContentType
import kotlin.math.roundToInt

data class ContentDto(
    val adult: Boolean,
    val backdrop_path: String,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String? = null,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String? = null,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    val media_type: String? = null,

    /*TV Series*/
    val name: String? = null,
    val original_name: String? = null,
    val first_air_date: String? = null
) {
    fun toContentModel(viewContentType: ViewContentType = ViewContentType.ROW_DEFAULT): ContentModel =
        ContentModel(
            id,
            original_title ?: original_name,
            poster_path,
            backdrop_path,
            release_date ?: first_air_date,
            media_type,
            viewContentType,
            getVoteAverage()
        )

    private fun getVoteAverage(): Int = (vote_average * 10).roundToInt()

}