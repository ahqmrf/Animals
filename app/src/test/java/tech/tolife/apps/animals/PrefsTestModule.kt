package tech.tolife.apps.animals

import android.app.Application
import tech.tolife.apps.animals.di.PrefsModule
import tech.tolife.apps.animals.util.PreferenceHelper

class PrefsTestModule(val prefHelper: PreferenceHelper): PrefsModule() {
    override fun provideSharedPrefHelper(app: Application): PreferenceHelper {
        return prefHelper
    }
}