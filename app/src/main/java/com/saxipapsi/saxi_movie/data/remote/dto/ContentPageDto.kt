package com.saxipapsi.saxi_movie.data.remote.dto

data class ContentPageDto(
    val page: Int,
    val results: List<ContentDto>,
    val total_pages: Int,
    val total_results: Int
)