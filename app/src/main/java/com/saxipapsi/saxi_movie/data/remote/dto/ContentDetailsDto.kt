package com.saxipapsi.saxi_movie.data.remote.dto

import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.domain.model.ContentDetailsModel
import kotlin.math.roundToInt

data class ContentDetailsDto(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: BelongsToCollection,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String? = null,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String? = null,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: String? = null,

    /*TV Series*/
    val name: String? = null,
    val original_name: String? = null,
    val first_air_date: String? = null,
    val number_of_episodes: Int? = null,
    val number_of_seasons: Int? = null,
) {
    fun toContentDetailsModel(contentType: ContentType): ContentDetailsModel = ContentDetailsModel(
        id,
        original_title ?: original_name,
        backdrop_path,
        poster_path,
        release_date ?: first_air_date,
        tagline,
        overview,
        genreBuilder(),
        getVoteAverage(),
        vote_count,
        contentType
    )

    private fun genreBuilder(): String {
        val values: List<String> = genres.map { it.name }
        return java.lang.String.join(", ", values)
    }

    private fun getVoteAverage(): Int = (vote_average * 10).roundToInt()

}