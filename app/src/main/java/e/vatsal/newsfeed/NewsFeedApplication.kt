package e.vatsal.newsfeed

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import e.vatsal.newsfeed.utils.timber.DebugTree
import e.vatsal.newsfeed.utils.timber.ReleaseTree
import timber.log.Timber

@HiltAndroidApp
class NewsFeedApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}