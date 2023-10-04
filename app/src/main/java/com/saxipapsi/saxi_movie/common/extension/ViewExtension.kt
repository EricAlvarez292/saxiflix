package com.saxipapsi.saxi_movie.common.extension

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.saxipapsi.saxi_movie.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun ImageView.load(url: String, placeHolder : Int = R.drawable.bg) {
    Glide.with(this).load(url).transform(
        CenterInside(),
        RoundedCorners(24)
    ).placeholder(placeHolder).dontAnimate().into(this)
}

fun DialogFragment.width(percent: Int = 75) {
    val data = context?.getWindowMetricsBy(percent)
    data?.first?.let { dialog?.window?.setLayout(it, WindowManager.LayoutParams.WRAP_CONTENT) }
}

fun Activity.width(percent: Int = 75) {
    val data = this.getWindowMetricsBy(percent)
    window?.setLayout(data.first, WindowManager.LayoutParams.WRAP_CONTENT)
}

fun Context.getWindowMetricsBy(percent: Int = 75): Pair<Int, Int> {
    val metrics = DisplayMetrics()
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(metrics)
    val width = metrics.widthPixels * percent / 100
    val height = metrics.heightPixels * percent / 100
    Log.d("Eric", "Width : ${metrics.widthPixels}")
    Log.d("Eric", "Width : ${metrics.heightPixels}")
    return Pair(width, height)
}


fun <T> LifecycleOwner.collectLast(flow: Flow<T>, action: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(action)
        }
    }
}


fun <T> LifecycleOwner.collect(flow: Flow<T>, action: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                action.invoke(it)
            }
        }
    }
}