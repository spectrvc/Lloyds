package com.art.lloyds.di

import com.art.domain.common.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProvider()
}