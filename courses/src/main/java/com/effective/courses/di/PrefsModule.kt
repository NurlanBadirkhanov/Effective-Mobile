package com.effective.courses.di


import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {
    @dagger.Provides
    @javax.inject.Singleton
    fun provideSharedPrefs(
        @dagger.hilt.android.qualifiers.ApplicationContext ctx: android.content.Context
    ): android.content.SharedPreferences =
        ctx.getSharedPreferences("em_prefs", android.content.Context.MODE_PRIVATE)
}
