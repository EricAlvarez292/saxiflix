package com.saxipapsi.saxi_movie.domain.model

data class CreditsModel(
    val profilePath: String? = null,
    val fullName: String,
    val character: String? = null,
    val department: String? = null,
    val job: String? = null,
    val isCast: Boolean
){
    fun toRecyclerViewContainer() = RecyclerViewContainer(isHeader = false, title = null, data = this)
}
