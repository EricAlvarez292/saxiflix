package com.saxipapsi.saxi_movie.data.remote.dto.credits

import com.saxipapsi.saxi_movie.domain.model.CreditsModel

data class CrewDto(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String? = null,
) {
    fun toCreditsModel() = CreditsModel(
        profile_path,
        fullName = original_name,
        character = null,
        department = department,
        job = job,
        true
    )
}