package com.art.lloyds.di

import com.art.domain.navigation.NavigationRepository
import com.art.domain.navigation.NavigationUseCase
import com.art.domain.store.StoreRepository
import com.art.domain.store.StoreUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideNavigationUseCase(navigationRepository: NavigationRepository): NavigationUseCase {
        return NavigationUseCase(navigationRepository = navigationRepository)
    }

    @Provides
    fun provideStoreUseCase(storeRepository: StoreRepository): StoreUseCase {
        return StoreUseCase(storeRepository = storeRepository)
    }

}

