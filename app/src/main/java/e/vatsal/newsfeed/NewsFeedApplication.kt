package e.vatsal.newsfeed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NewsFeedApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}