package com.example.launcherapple.di

import android.content.Context
import com.example.launcherapple.data.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppRepository(@ApplicationContext context: Context): AppRepository {
        return AppRepository(context)
    }
}