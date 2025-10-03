package com.effective.courses.data.mapper

import com.effective.core.network.model.CourseDto
import com.effective.courses.model.Course

fun CourseDto.toDomain(): Course = Course(
    id = id.toString(),
    title = title,
    text = text,
    price = price,
    rate = rate.toDoubleOrNull() ?: 0.0,
    startDate = startDate,
    hasLike = hasLike,
    publishDate = publishDate
)