package com.saxipapsi.saxi_movie.domain.model

import com.saxipapsi.saxi_movie.common.ContentType
import com.saxipapsi.saxi_movie.data.remote.dto.video.ContentVideoDto

data class ContentDetailsModel(
    val id: Int,
    val title: String? = null,
    val banner: String? = null,
    val foster: String? = null,
    val date: String? = null,
    val tagline: String? = null,
    val overView: String? = null,
    val genres: String? = null,
    val voteAverage: Int = 0,
    val voteCount: String? = null,

    val contentType: ContentType,
    var videos: ContentVideoDto? = null
)
