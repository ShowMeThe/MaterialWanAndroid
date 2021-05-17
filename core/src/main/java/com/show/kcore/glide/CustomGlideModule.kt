package com.show.kcore.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream


@GlideModule
class CustomGlideModule : AppGlideModule() {


    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSizeBytes = 1024 * 1024 * 500 // 500 MB
        val memoryCacheBytes = 1024 * 1024 * 200
        builder.setMemoryCache(LruResourceCache(memoryCacheBytes.toLong()))
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, diskCacheSizeBytes.toLong()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
       //registry.replace(GlideUrl::class.java, InputStream::class.java, OkHttpUrlLoader.Factory())
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(TGlide.interceptor)
            .build()
        registry.replace(GlideUrl::class.java, InputStream::class.java,
            OkHttpLoader.Factory(okHttpClient)
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}