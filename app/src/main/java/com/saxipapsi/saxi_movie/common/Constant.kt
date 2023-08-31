package com.saxipapsi.saxi_movie.common


object API {
    object TMDBApi {
        const val BASE_URL = "https://api.themoviedb.org/3/"

        const val CONTENT_DETAIL = "$BASE_URL{content_type}/{content_id}"

        /*content_type = movie | tv */
        const val POPULAR_CONTENTS = "$BASE_URL/{content_type}/popular"

        /*trend_type = day | week */
        const val TRENDING_CONTENTS = "$BASE_URL/trending/all/{trend_type}"

        /*content_type = movie | tv */
        const val TOP_RATED_CONTENTS = "$BASE_URL/{content_type}/top_rated"

        /*content_type = movie | tv & content_id = id*/
        const val CONTENT_VIDEOS = "$BASE_URL/{content_type}/{content_id}/videos"

        const val UPCOMING_MOVIES = "$BASE_URL/movie/upcoming"

        const val CREDITS = "$BASE_URL/{content_type}/{content_id}/credits"

    }
}

enum class ContentType { movie, tv }
enum class ContentTrendingType { day, week }
