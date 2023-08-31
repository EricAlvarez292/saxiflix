package com.saxipapsi.saxi_movie.data.remote.dto.credits

data class CreditsDto(
    val cast: List<CastDto>,
    val crew: List<CrewDto>,
    val id: Int
)