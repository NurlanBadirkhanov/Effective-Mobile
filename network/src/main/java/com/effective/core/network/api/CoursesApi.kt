package com.effective.core.network.api

import com.effective.core.network.model.CoursesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface CoursesApi {
    @GET
    suspend fun getCourses(@Url fullUrl: String): CoursesResponse

    companion object {
        const val MOCK_URL =
            "https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download"
    }
}
