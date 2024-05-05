package com.sbmshukla.flavorfriend.ui.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sbmshukla.flavorfriend.R

@BindingAdapter("imageFromUri")
fun ImageView.loadImageUrl(url: String?) {
    url?.let {
        Glide.with(this)
            .load(it)
            .placeholder(R.drawable.ic_viewholder)
            .error(R.drawable.ic_error_viewholder)
            .into(this)
    }
}