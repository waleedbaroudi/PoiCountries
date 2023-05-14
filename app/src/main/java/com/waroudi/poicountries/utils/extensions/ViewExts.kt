package com.waroudi.poicountries.utils.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.waroudi.poicountries.R

/**
 * Sets the image from a URL source using [Glide]
 * @param url the url of the image
 * @param placeholderResId optional placeholder resource ID
 */
fun ImageView.setGlideImage(url: String?, @DrawableRes placeholderResId: Int? = null) {
    val loadingPlaceholder = getDefaultPlaceHolder(context)

    val builder = Glide.with(context).load(url)
    placeholderResId?.let { builder.error(it) }
    builder.transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(loadingPlaceholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

/**
 * Returns a spinning progress drawable placeholder
 * @param context for placeholder instantiation
 * @return a [CircularProgressDrawable]
 */
private fun getDefaultPlaceHolder(context: Context) = CircularProgressDrawable(context).apply {
    strokeWidth = 4f
    centerRadius = 13f
    arrowEnabled = true
    setColorSchemeColors(R.color.primary)
    start()
}

/**
 * Sets the view visibility to [View.GONE], if the view is not null and not gone
 */
fun View?.toGone() {
    if (this == null) return
    if (visibility == View.GONE) return
    visibility = View.GONE
}

/**
 * Sets the view visibility to [View.VISIBLE], if the view is not null and not visible
 */
fun View?.toVisible() {
    if (this == null) return
    if (visibility == View.VISIBLE) return
    visibility = View.VISIBLE
}

/**
 * Hides soft keyboard
 */
fun View.hideSoftKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}