package com.vineet.visithealthassesment.utils

import android.view.View

object UiUtils {
    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }
}