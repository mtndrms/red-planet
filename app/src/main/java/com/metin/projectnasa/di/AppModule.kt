package com.metin.projectnasa.di

import com.metin.projectnasa.data.ApiClient
import com.metin.projectnasa.data.repository.NASARepositoryImpl
import com.metin.projectnasa.data.service.NASAService
import com.metin.projectnasa.domain.repository.NASARepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiClient(): Retrofit {
        return ApiClient.getClient()
    }

    @Provides
    @Singleton
    fun provideNASAService(apiClient: Retrofit): NASAService {
        return apiClient.create(NASAService::class.java)
    }

//    @Provides
//    @Singleton
//    fun provideNASARepository(nasaService: NASAService): NASARepository {
//        return NASARepositoryImpl(nasaService)
//    }
}