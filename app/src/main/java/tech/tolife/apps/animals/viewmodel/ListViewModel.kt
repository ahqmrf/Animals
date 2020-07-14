package tech.tolife.apps.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import tech.tolife.apps.animals.di.AppModule
import tech.tolife.apps.animals.di.DaggerViewModelComponent
import tech.tolife.apps.animals.model.Animal
import tech.tolife.apps.animals.model.AnimalRepository
import tech.tolife.apps.animals.model.ApiKey
import tech.tolife.apps.animals.util.PreferenceHelper
import javax.inject.Inject

class ListViewModel(application: Application): AndroidViewModel(application) {

    constructor(application: Application, test: Boolean = true): this(application) {
        injected = true
    }

    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    @Inject
    lateinit var repository: AnimalRepository

    @Inject
    lateinit var prefHelper: PreferenceHelper

    private val disposable by lazy {
        CompositeDisposable()
    }

    private var injected = false

    fun inject() {
        if(!injected) {
            DaggerViewModelComponent.builder().appModule(AppModule(getApplication()))
                .build()
                .inject(this)
            injected = true
        }
    }

    fun refresh() {
        inject()
        loadError.value = false
        loading.value = true
        val key = prefHelper.getApiKey()
        if(key.isNullOrEmpty()) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    private fun getKey() {
        disposable.add(
            repository.getApiKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(key: ApiKey) {
                        if(key.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            prefHelper.saveApiKey(key.key)
                            getAnimals(key.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        loadError.value = true
                        loading.value = false
                    }
                })
        )
    }

    private fun getAnimals(key: String) {
        disposable.add(
            repository.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(list: List<Animal>) {
                        loading.value = false
                        loadError.value = false
                        animals.value = list
                    }

                    override fun onError(e: Throwable) {
                        loading.value = false
                        loadError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}