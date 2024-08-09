package com.example.e_commerce.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        url?.let {
            // Glide ile resmi yükle
            Glide.with(view.context)
                .load(it)
                .into(view)
        }
    }
}