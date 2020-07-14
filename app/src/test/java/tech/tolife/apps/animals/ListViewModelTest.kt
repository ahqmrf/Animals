package tech.tolife.apps.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import tech.tolife.apps.animals.di.AppModule
import tech.tolife.apps.animals.di.DaggerViewModelComponent
import tech.tolife.apps.animals.model.Animal
import tech.tolife.apps.animals.model.AnimalRepository
import tech.tolife.apps.animals.model.ApiKey
import tech.tolife.apps.animals.util.PreferenceHelper
import tech.tolife.apps.animals.viewmodel.ListViewModel
import java.util.concurrent.Executor

class ListViewModelTest {

    companion object {
        const val API_KEY = "testApiKey"
        const val INVALID_API_KEY = "invalidApiKey"
        val ANIMAL_LIST = listOf(Animal("cow", null, null, null, null, null, null))
    }

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalRepository: AnimalRepository

    @Mock
    lateinit var prefHelper: PreferenceHelper

    val application = Mockito.mock(Application::class.java)

    var listViewModel = ListViewModel(application, true)

    @Before
    fun setup() {

        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .repositoryModule(RepositoryTestModule(animalRepository))
            .prefsModule(PrefsTestModule(prefHelper))
            .build()
            .inject(listViewModel)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor {
                    it.run()
                }, true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Test
    fun refresh_success_correctAnimalsReturned() {
        successWithKeyAlreadyCached()
        listViewModel.refresh()
        Assert.assertThat(listViewModel.animals.value, `is`(ANIMAL_LIST))
    }

    @Test
    fun refresh_success_loadingFalse() {
        successWithKeyAlreadyCached()
        listViewModel.refresh()
        Assert.assertThat(listViewModel.loading.value, `is`(false))
    }

    @Test
    fun refresh_success_loadErrorFalse() {
        successWithKeyAlreadyCached()
        listViewModel.refresh()
        Assert.assertThat(listViewModel.loadError.value, `is`(false))
    }

    @Test
    fun refresh_successKeyFetched_correctKeyCached() {
        successWithKeyNotCached()
        val ac = ArgumentCaptor.forClass(String::class.java)
        listViewModel.refresh()
        Mockito.verify(prefHelper).saveApiKey(ac.capture())
        Assert.assertThat(ac.value, `is`(API_KEY))
    }

    @Test
    fun refresh_successKeyNotFetched_noKeyCached() {
        successWithKeyAlreadyCached()
        val ac = ArgumentCaptor.forClass(String::class.java)
        listViewModel.refresh()
        Mockito.verify(prefHelper, Mockito.never()).saveApiKey(ac.capture())
    }

    @Test
    fun refresh_invalidApiKey_loadingFalse() {
        failWithInvalidApiKey()
        listViewModel.refresh()
        Assert.assertThat(listViewModel.loading.value, `is`(false))
    }

    @Test
    fun refresh_invalidApiKey_loadErrorTrue() {
        failWithInvalidApiKey()
        listViewModel.refresh()
        Assert.assertThat(listViewModel.loadError.value, `is`(true))
    }

    private fun successWithKeyAlreadyCached() {
        Mockito.`when`(prefHelper.getApiKey()).thenReturn(API_KEY)
        Mockito.`when`(animalRepository.getApiKey()).thenReturn(Single.just(ApiKey("ok", API_KEY)))
        val testSingle = Single.just(ANIMAL_LIST)
        Mockito.`when`(animalRepository.getAnimals(API_KEY)).thenReturn(testSingle)
    }

    private fun successWithKeyNotCached() {
        Mockito.`when`(prefHelper.getApiKey()).thenReturn(null)
        Mockito.`when`(animalRepository.getApiKey()).thenReturn(Single.just(ApiKey("ok", API_KEY)))
        val testSingle = Single.just(ANIMAL_LIST)
        Mockito.`when`(animalRepository.getAnimals(API_KEY)).thenReturn(testSingle)
    }

    private fun failWithInvalidApiKey() {
        Mockito.`when`(animalRepository.getApiKey())
            .thenReturn(Single.just(ApiKey("Ok", INVALID_API_KEY)))
        Mockito.`when`(animalRepository.getAnimals(INVALID_API_KEY)).thenReturn(
            Single.error(
                Throwable()
            )
        )
    }
}