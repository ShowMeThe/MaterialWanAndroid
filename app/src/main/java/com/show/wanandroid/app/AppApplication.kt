package com.show.wanandroid.app

import androidx.annotation.Keep
import com.show.wanandroid.R
import com.show.wanandroid.api.Main
import com.show.wanandroid.plugin.*
import com.show.wanandroid.themes_name
import com.showmethe.skinlib.SkinManager
import com.showmethe.skinlib.entity.ColorEntity
import showmethe.github.core.base.BaseApplication
import showmethe.github.core.http.RetroHttp
import showmethe.github.core.http.fromJson
import showmethe.github.core.kinit.Module
import showmethe.github.core.kinit.startInit
import showmethe.github.core.util.assetFile.AssetFile
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

        val json = AssetFile.getJson(this,"orange.json")
        val colorEntity = json.fromJson<ColorEntity>()!!
        val json2 = AssetFile.getJson(this,"yellow.json")
        val colorEntity2 = json2.fromJson<ColorEntity>()!!

        SkinManager.init(this).addStyle(
            themes_name[0] to R.style.MaterialTheme_Blue,
            themes_name[1] to R.style.MaterialTheme_Red,
            themes_name[2] to R.style.MaterialTheme_Purple)
            .addJson(themes_name[3] to colorEntity, themes_name[4] to colorEntity2)
            .addPlugin(
                RefreshPlugin(),SmartIPlugin(),
                ExpandIPlugin(), SearchChipGroup(),
                bannerPlugin, ShakingImageViewIPlugin(),
                WaveIPlugin())
            .build()
        if(RDEN.get("theme","").isEmpty()){
            RDEN.put("theme","BlueTheme")
        }
        SkinManager.getInstant().setOnStyleChangeListener {
            RDEN.put("theme",it)
        }
        SkinManager.currentStyle =  RDEN.get("theme","BlueTheme")

    }
}