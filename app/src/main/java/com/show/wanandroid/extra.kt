package com.show.wanandroid

import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.Lifecycle
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialSharedAxis
import com.show.kcore.base.AppContext
import com.show.kcore.glide.TGlide
import com.show.kcore.rden.Stores
import com.show.wanandroid.bean.UserBean
import com.show.wanandroid.const.StoreConst
import com.show.wanandroid.plugin.BannerPlugin
import com.show.wanandroid.ui.main.LoginActivity


fun BottomNavigationView.setOnNavigationSingleItemSelectedListener(listener: (it: MenuItem) -> Unit) {
    var lastTime = 0L
    setOnItemSelectedListener {
        if (System.currentTimeMillis() - lastTime >= 300) {
            lastTime = System.currentTimeMillis()
            listener.invoke(it)
            true
        } else {
            false
        }
    }
}

val bannerPlugin = BannerPlugin()

inline val CONFIG
    get() = TGlide.Config.newConfig().apply {
        cacheMode = DiskCacheStrategy.DATA
    }

inline val SCALE_CONFIG
    get() = TGlide.Config.newConfig().apply {
        cacheMode = DiskCacheStrategy.DATA
        isRevealScale = true
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
    id: Int = R.id.mainTree,
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
            tempFragment = replaceFragment.apply {

            }
            transaction
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

fun toast(error: Int, message: Int) {
    val ctx = AppContext.get().getActivity()
    Toast.makeText(ctx, ctx?.getString(message) ?: "网络错误", Toast.LENGTH_SHORT).show()
    if (error == -1001) {
        logOut()
        ctx?.startActivity(Intent(ctx, LoginActivity::class.java))
    }
}

fun toast(error: Int, message: String?) {
    val ctx = AppContext.get().getActivity()
    Toast.makeText(ctx, message ?: "网络错误", Toast.LENGTH_SHORT).show()
    if (error == -1001) {
        logOut()
        ctx?.startActivity(Intent(ctx, LoginActivity::class.java))
    }
}

fun logoutWhenSessionIsEmpty() {
    val ctx = AppContext.get().getActivity()
    val session = Stores.getString("sessionId", null)
    if (session == null) {
        logOut()
        ctx?.startActivity(
            Intent(ctx, LoginActivity::class.java),
            ActivityOptions.makeSceneTransitionAnimation(ctx).toBundle()
        )
    }
}

fun logOut() {
    Stores.putObject<UserBean>(StoreConst.UserInfo, null)
    Stores.put(StoreConst.IsLogin, false)
    Stores.put("sessionId", null)
}


