package tech.tolife.apps.animals.model

import io.reactivex.Single
import tech.tolife.apps.animals.di.DaggerApiComponent
import javax.inject.Inject

class AnimalRepository {

    @Inject
    lateinit var apiService: AnimalApiService

    init {
        DaggerApiComponent.create().inject(apiService)
    }

    fun getApiKey(): Single<ApiKey> {
        return apiService.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return apiService.getAnimals(key)
    }
}