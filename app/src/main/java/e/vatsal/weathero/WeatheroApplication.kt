package e.vatsal.weathero

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatheroApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}