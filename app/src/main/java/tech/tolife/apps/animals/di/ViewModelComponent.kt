package tech.tolife.apps.animals.di

import dagger.Component
import tech.tolife.apps.animals.viewmodel.ListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, AppModule::class, PrefsModule::class])
interface ViewModelComponent {

    fun inject(viewModel: ListViewModel)
}