package com.je.playground.hilt

import android.content.Context
import com.je.playground.notification.NotificationScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideNotificationScheduler(@ApplicationContext context : Context) : NotificationScheduler {
        return NotificationScheduler(context = context)
    }
}