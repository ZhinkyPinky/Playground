package com.je.playground.hilt

import android.content.Context
import com.je.playground.databaseV2.AppDatabaseV2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context) : AppDatabaseV2 {
        return AppDatabaseV2.getDatabase(context)
    }
}