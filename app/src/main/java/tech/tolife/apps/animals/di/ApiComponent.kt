package tech.tolife.apps.animals.di

import dagger.Component
import tech.tolife.apps.animals.model.AnimalApiService

@Component(modules = [RepositoryModule::class])
interface ApiComponent {

    fun inject(service: AnimalApiService)
}