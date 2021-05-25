package com.show.wanandroid.app

import android.app.Application
import com.show.kInject.core.initScope
import com.show.kcore.base.BaseApplication
import com.show.kcore.http.Http
import com.show.kcore.http.jsonToClazz
import com.show.kcore.rden.Stores
import com.show.wanandroid.*
import com.show.wanandroid.api.Main
import com.show.wanandroid.plugin.*
import com.show.wanandroid.utils.AssetFile
import com.showmethe.skinlib.SkinManager
import com.showmethe.skinlib.entity.ColorEntity
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig

class AppApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()

        initScope {
            single { Http.createApi(Main::class.java) }
        }

        initTheme()

        val builder  = Matrix.Builder(this)
        builder.patchListener(TestPluginListener(this))

       val tracePlugin = TracePlugin(
           TraceConfig.Builder()
               .dynamicConfig(DynamicConfigImplDemo())
               .enableFPS(true)
               .enableStartup(true)
               .enableEvilMethodTrace(true)
               .enableAnrTrace(true)
           .build())

        builder.plugin(tracePlugin)
        Matrix.init(builder.build())
        tracePlugin.start()

    }

    private fun initTheme() {
        val json = AssetFile.getJson(this, "orange.json")
        val colorEntity = json.jsonToClazz<ColorEntity>()!!
        val json2 = AssetFile.getJson(this, "yellow.json")
        val colorEntity2 = json2.jsonToClazz<ColorEntity>()!!

        SkinManager.init(this).addStyle(
            themes_name[0] to R.style.MaterialTheme_Blue,
            themes_name[1] to R.style.MaterialTheme_Red,
            themes_name[2] to R.style.MaterialTheme_Purple
        )
            .addJson(themes_name[3] to colorEntity, themes_name[4] to colorEntity2)
            .addPlugin(
                RefreshPlugin(), SmartIPlugin(), LikePlugin(),
                ExpandIPlugin(), SearchChipGroup(),
                bannerPlugin, ShakingImageViewIPlugin(),
            )
            .build()
        if (Stores.getString("theme", "").isNullOrEmpty()) {
            Stores.put("theme", "BlueTheme")
        }
        SkinManager.getManager().setOnStyleChangeListener {
            Stores.put("theme", it)
        }
        Stores.getString("theme", "BlueTheme")?.apply {
            SkinManager.currentStyle = this
        }
    }
}