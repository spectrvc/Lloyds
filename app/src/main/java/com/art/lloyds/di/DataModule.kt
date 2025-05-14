package com.art.lloyds.di

import com.art.data.navigation.NavigationRepositoryImpl
import com.art.data.store.StoreApiService
import com.art.data.store.StoreRepositoryImpl
import com.art.domain.navigation.NavigationRepository
import com.art.domain.store.StoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideNavigationRepository(): NavigationRepository {
        return NavigationRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideStoreRepository(storeApiService: StoreApiService): StoreRepository {
        return StoreRepositoryImpl(storeApiService = storeApiService)
    }

}


