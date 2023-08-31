package com.saxipapsi.saxi_movie.domain.model

import com.saxipapsi.saxi_movie.presentation.get_content_list.components.ViewContentType

data class ContentModel(
    val id: Int,
    val title: String? = null,
    val poster: String? = null,
    val banner: String? = null,
    val date: String? = null,
    var media_type: String? = null,
    var viewContentType: ViewContentType = ViewContentType.ROW_DEFAULT,
    var voteAverage : Int = 0
)

data class ContentHeaderModel(
    val title: String? = null,
    var banner: String? = null,
    val data: Any? = null,
)


object ImageUrlBuilder {
    fun getOriginalPosterUrl(fileName: String) = "https://image.tmdb.org/t/p/original$fileName"
    fun getW780PosterUrl(fileName: String) = "https://image.tmdb.org/t/p/w780$fileName"
    fun getW500PosterUrl(fileName: String) = "https://image.tmdb.org/t/p/w500$fileName"
    fun getW342PosterUrl(fileName: String) = "https://image.tmdb.org/t/p/w342$fileName"
    fun getW300PosterUrl(fileName: String) = "https://image.tmdb.org/t/p/w300$fileName"
}