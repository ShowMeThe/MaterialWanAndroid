package com.show.wanandroid.app

import com.show.kInject.core.initScope
import com.show.kcore.base.BaseApplication
import com.show.kcore.http.Http
import com.show.wanandroid.api.Main

class AppApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()

        initScope {
            single { Http.createApi(Main::class.java) }
        }

    }
}