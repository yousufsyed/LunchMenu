package com.gusto.lunchmenu.di

import com.gusto.lunchmenu.provider.DispatcherProvider
import com.gusto.lunchmenu.LunchMenuViewModelFactory
import com.gusto.lunchmenu.data.DefaultLunchMenuDataSource
import com.gusto.lunchmenu.data.LunchMenuDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GestoModule {

    @Provides
    @Singleton
    fun provideLunchMenuDataSource(
        impl: DefaultLunchMenuDataSource
    ): LunchMenuDataSource = impl

    @Provides
    fun provideDefaultLunchMenuViewModelFactory(
        dataSource: LunchMenuDataSource,
        dispatchers: DispatcherProvider
    ) = LunchMenuViewModelFactory(
        dataSource,
        dispatchers,
    )

    @Provides
    fun provideDispatcherProvider() = DispatcherProvider(
        main = Dispatchers.Main,
        io = Dispatchers.IO,
        default = Dispatchers.Default
    )

}