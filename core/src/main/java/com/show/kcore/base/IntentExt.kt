package com.show.kcore.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity



inline fun <reified T> FragmentActivity.startActivity(bundle: Bundle? = null, transition: Boolean = false) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(
        intent, if (transition) {
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        } else {
            null
        }
    )
}

inline fun <reified T> FragmentActivity.startActivityWithPair(
    bundle: Bundle? = null,
    vararg pair: android.util.Pair<View, String>
) {
    val intent = Intent(this, T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (pair.isNotEmpty()) {
        val transitionActivityOptions =
            ActivityOptions.makeSceneTransitionAnimation(this, *pair)
        startActivity(intent, transitionActivityOptions.toBundle())
    } else {
        startActivity(intent)
    }
}

