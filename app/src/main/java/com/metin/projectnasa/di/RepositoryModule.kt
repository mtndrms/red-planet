package com.metin.projectnasa.di

import com.metin.projectnasa.data.repository.NASARepositoryImpl
import com.metin.projectnasa.domain.repository.NASARepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(NASARepositoryImpl: NASARepositoryImpl): NASARepository
}