package com.example.e_commerce.extension

import androidx.recyclerview.widget.RecyclerView

var RecyclerView.isLoading: Boolean
    get() = (this.tag as? Boolean) ?: false
    set(value) {
        this.tag = value
    }