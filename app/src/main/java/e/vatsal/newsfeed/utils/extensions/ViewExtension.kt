package e.vatsal.newsfeed.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay

fun Context.showToast(message: String?) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_SHORT
    ).show()
}


fun Fragment.showToast(message: String?) {
    Toast.makeText(
        this.activity,
        message,
        Toast.LENGTH_SHORT
    ).show()
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

//fun ViewPager2.autoScroll(lifecycleScope: LifecycleCoroutineScope, interval: Long) {
//    lifecycleScope.launchWhenResumed {
//        scrollIndefinitely(interval)
//    }
//}

private suspend fun ViewPager2.scrollIndefinitely(interval: Long) {
    delay(interval)
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1

    setCurrentItem(nextItem, true)
    scrollIndefinitely(interval)
}

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(createIntent<T>())
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(createIntent<T>())
}

inline fun <reified T : Activity> Context.createIntent() =
    Intent(this, T::class.java)