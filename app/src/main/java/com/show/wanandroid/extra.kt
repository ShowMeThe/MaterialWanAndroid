package com.show.wanandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.show.kcore.glide.TGlide


inline val CONFIG
    get() = TGlide.Config.newConfig().apply {
        cacheMode = DiskCacheStrategy.DATA
    }

val themes_res = arrayListOf(
    R.color.colorAccent, R.color.color_304ffe,
    R.color.color_6200ea, R.color.color_f4511e, R.color.color_FBC02D
)
val themes_name = arrayListOf("BlueTheme", "RedTheme", "PurpleTheme", "OrangeTheme", "YellowTheme")


val colors = arrayListOf(
    "#f48fb1",
    "#ce93d8",
    "#b39ddb",
    "#81d4fa",
    "#a5d6a7",
    "#ffab91",
    "#ffe082",
    "#bcaaa4"
)


fun Fragment.replaceFragment(
    replaceFragment: Fragment,
    id: Int = R.id.frameLayout,
    transition: androidx.transition.TransitionSet? = null
) {
    val tag = replaceFragment::class.java.name
    var tempFragment = childFragmentManager.findFragmentByTag(tag)
    val transaction = childFragmentManager.beginTransaction()
    if (tempFragment == null) {
        try {
            tempFragment = replaceFragment.apply {
                transition?.apply {
                    enterTransition = this
                }
            }
            transaction.addToBackStack(null)
                .add(id, tempFragment, tag)
                .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    val fragments = childFragmentManager.fragments

    for (i in fragments.indices) {
        val fragment = fragments[i]
        if (fragment.tag == tag) {
            transaction
                .show(fragment)
        } else {
            transaction
                .hide(fragment)
        }
    }
    transaction.commitAllowingStateLoss()
}


fun FragmentActivity.replaceFragment(replaceFragment: Fragment, id: Int = R.id.frameLayout) {
    val tag = replaceFragment::class.java.name
    var tempFragment = supportFragmentManager.findFragmentByTag(tag)
    val transaction = supportFragmentManager.beginTransaction()
    if (tempFragment == null) {
        try {
            tempFragment = replaceFragment
            transaction.addToBackStack(null)
                .add(id, tempFragment, tag)
                .setMaxLifecycle(tempFragment, Lifecycle.State.RESUMED)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    val fragments = supportFragmentManager.fragments

    for (i in fragments.indices) {
        val fragment = fragments[i]
        if (fragment.tag == tag) {
            transaction
                .show(fragment)
        } else {
            transaction
                .hide(fragment)
        }
    }
    transaction.commitAllowingStateLoss()
}