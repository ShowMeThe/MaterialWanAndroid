package com.show.kcore.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


inline fun <reified T> FragmentActivity.startActivity(
    bundle: Bundle? = null,
    transition: Boolean = false
) {
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


inline fun <reified T> Fragment.startActivity(bundle: Bundle? = null, transition: Boolean = false) {
    val intent = Intent(requireContext(), T::class.java)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    startActivity(
        intent, if (transition) {
            ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle()
        } else {
            null
        }
    )
}

