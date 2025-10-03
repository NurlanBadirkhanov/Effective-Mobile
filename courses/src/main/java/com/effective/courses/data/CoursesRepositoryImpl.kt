package com.effective.courses.data

import com.effective.core.network.api.CoursesApi
import com.effective.courses.data.mapper.toDomain
import com.effective.courses.domain.CoursesRepository
import com.effective.courses.model.Course
import javax.inject.Inject


class CoursesRepositoryImpl @Inject constructor(
    private val api: CoursesApi
) : CoursesRepository {

    override suspend fun getCourses(sortDescByPublishDate: Boolean): List<Course> {
        val list = api.getCourses(CoursesApi.MOCK_URL).courses.map { it.toDomain() }
        return if (sortDescByPublishDate) list.sortedByDescending { it.publishDate } else list
    }
}
