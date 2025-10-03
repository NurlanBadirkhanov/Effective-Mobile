package com.effective.courses.domain

import com.effective.courses.model.Course

interface CoursesRepository {
    suspend fun getCourses(sortDescByPublishDate: Boolean = true): List<Course>
}