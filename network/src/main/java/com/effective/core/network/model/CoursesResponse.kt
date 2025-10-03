package com.effective.core.network.model

import com.squareup.moshi.Json

data class CoursesResponse(
    @Json(name = "courses") val courses: List<CourseDto>
)