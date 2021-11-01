package com.hidnapp.androidfinder

import android.content.Context
import android.widget.Toast

fun textMessage(c: Context, s: String, long: Boolean = false) {
    Toast.makeText(c, s, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}
