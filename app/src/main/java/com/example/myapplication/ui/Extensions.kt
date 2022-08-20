package com.example.myapplication.ui

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.setImage(url: String, withCenterCrop: Boolean = true) {
    if (withCenterCrop) {
        Glide.with(this.context)
            .load(url.ifEmpty { null })
            .error("") // TODO: replace error image
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    } else {
        Glide.with(this.context)
            .load(url.ifEmpty { null })
            .error("") // TODO: replace error image
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }

}