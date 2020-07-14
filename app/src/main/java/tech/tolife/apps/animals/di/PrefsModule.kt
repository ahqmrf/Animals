package tech.tolife.apps.animals.di

import android.app.Application
import dagger.Module
import dagger.Provides
import tech.tolife.apps.animals.util.PreferenceHelper
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    open fun provideSharedPrefHelper(app: Application): PreferenceHelper {
        return PreferenceHelper(app)
    }
}