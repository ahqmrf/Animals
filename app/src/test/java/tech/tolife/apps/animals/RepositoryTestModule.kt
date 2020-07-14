package tech.tolife.apps.animals

import tech.tolife.apps.animals.di.RepositoryModule
import tech.tolife.apps.animals.model.AnimalRepository

class RepositoryTestModule(val repository: AnimalRepository) : RepositoryModule() {
    override fun provideAnimalRepository(): AnimalRepository {
        return repository
    }
}