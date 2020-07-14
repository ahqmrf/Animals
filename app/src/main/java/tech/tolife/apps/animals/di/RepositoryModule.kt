package tech.tolife.apps.animals.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.tolife.apps.animals.model.AnimalApiService
import tech.tolife.apps.animals.model.AnimalRepository

@Module
open class RepositoryModule {

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net/"

    @Provides
    fun provideAnimalApiService(): AnimalApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnimalApiService::class.java)
    }

    @Provides
    open fun provideAnimalRepository(): AnimalRepository {
        return AnimalRepository()
    }
}