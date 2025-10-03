package com.effective.courses.di

import com.effective.courses.data.CoursesRepositoryImpl
import com.effective.courses.domain.CoursesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoursesModule {
    @Binds @Singleton
    abstract fun bindCoursesRepository(impl: CoursesRepositoryImpl): CoursesRepository
}
