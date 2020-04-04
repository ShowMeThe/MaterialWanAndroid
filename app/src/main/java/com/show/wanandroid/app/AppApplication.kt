package com.show.wanandroid.app

import androidx.annotation.Keep
import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.entity.Auth
import com.show.wanandroid.plugin.*
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.SkinManager
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.startInit
import showmethe.github.core.util.rden.RDEN

@Keep
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
            themes_name[0] to R.style.MaterialTheme_Blue,
            themes_name[1] to R.style.MaterialTheme_Red,
            themes_name[2] to R.style.MaterialTheme_Purple)
            .addPlugin(
                RefreshPlugin(),SmartIPlugin(),
                ExpandIPlugin(), SearchChipGroup(),
                bannerPlugin, ShakingImageViewIPlugin(),
                WaveIPlugin())
            .build()
        SkinManager.getInstant().setOnStyleChangeListener {
            RDEN.put("theme",it)
        }
        SkinManager.currentStyle =  RDEN.get("theme","BlueTheme")

    }
}