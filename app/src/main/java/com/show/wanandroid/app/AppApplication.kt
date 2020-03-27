package com.show.wanandroid.app

import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Auth
import com.showmethe.skinlib.SkinManager
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.startInit

class AppApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        startInit {
            modules(Module{
                single{ RetroHttp.createApi(Main::class.java) }
            })
        }

        SkinManager.init(this).addStyle(
            "BlueTheme" to R.style.MaterialTheme_Blue,
            "RedTheme" to R.style.MaterialTheme_Red
        ).build()
    }
}