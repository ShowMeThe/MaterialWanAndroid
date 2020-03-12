package com.show.wanandroid.app

import com.show.wanandroid.api.Main
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
    }
}