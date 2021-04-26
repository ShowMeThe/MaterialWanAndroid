package com.show.wanandroid

import android.view.animation.LinearInterpolator
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.transition.MaterialSharedAxis
import com.show.kcore.glide.TGlide


inline val CONFIG get() = TGlide.Config.newConfig().apply {
    cacheMode = DiskCacheStrategy.DATA
}




fun FragmentActivity.replaceFragment(replaceFragment: Fragment, id: Int = R.id.frameLayout) {
    val tag = replaceFragment::class.java.name
    var tempFragment = supportFragmentManager.findFragmentByTag(tag)
    val transaction = supportFragmentManager.beginTransaction()
    if (tempFragment == null) {
        try {
            tempFragment = replaceFragment
            tempFragment.exitTransition = createTransition()
            tempFragment.enterTransition = createTransition()
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

private fun createTransition(): androidx.transition.TransitionSet {
    val transitionSet = androidx.transition.TransitionSet()
    val transition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    transition.interpolator = LinearInterpolator()
    transition.duration = 300
    transitionSet.addTarget(R.id.home)
    transitionSet.addTransition(transition)
    transitionSet.addTransition(androidx.transition.Fade())
    return transitionSet
}