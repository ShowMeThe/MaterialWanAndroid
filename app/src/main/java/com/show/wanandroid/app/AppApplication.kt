package com.show.wanandroid.app

import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Auth
import com.show.wanandroid.plugin.*
import com.showmethe.skinlib.SkinManager
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.startInit
import showmethe.github.core.util.rden.RDEN

class AppApplication : BaseApplication() {

    companion object{
        val bannerPlugin = BannerPlugin()
    }
    override fun onCreate() {
        super.onCreate()
        startInit {
            modules(Module{
                single{ RetroHttp.createApi(Main::class.java) }
            })
        }

        SkinManager.init(this).addStyle(
            "BlueTheme" to R.style.MaterialTheme_Blue,
            "RedTheme" to R.style.MaterialTheme_Red)
            .addPlugin(RefreshPlugin(),SmartIPlugin(),
                ExpandIPlugin(), SearchChipGroup(),bannerPlugin)
            .build()
        SkinManager.getInstant().setOnStyleChangeListener {
            RDEN.put("theme",it)
        }
        SkinManager.currentStyle =  RDEN.get("theme","BlueTheme")

    }
}