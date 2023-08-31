package com.saxipapsi.saxi_movie.data.remote.dto.credits

import com.saxipapsi.saxi_movie.domain.model.CreditsModel

data class CastDto(
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int,
    val id: Int,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String? = null,
) {
    fun toCreditsModel() = CreditsModel(
        profile_path,
        fullName = original_name,
        character = character,
        department = "Cast",
        null,
        true
    )
}