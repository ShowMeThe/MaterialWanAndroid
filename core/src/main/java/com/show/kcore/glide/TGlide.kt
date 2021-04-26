package com.show.kcore.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.FloatRange
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.show.kcore.base.AppContext
import com.show.kcore.glide.TGlide.Companion.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


class TGlide private constructor(private var context: Context) {
    private val requestManager: RequestManager = GlideApp.with(context.applicationContext)
    private var transitionOptions = DrawableTransitionOptions()
        .crossFade()

    private val revealTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            350,
            isCrossFadeEnabled = true,
            isReveal = true,
            isScale = false
        )
    )

    private val scaleTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            350,
            isCrossFadeEnabled = true,
            isReveal = false,
            isScale = true
        )
    )

    private val fadeTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            350,
            isCrossFadeEnabled = true,
            isReveal = false,
            isScale = false
        )
    )
    private val allTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            350,
            isCrossFadeEnabled = true,
            isReveal = true,
            isScale = true
        )
    )

    open class Config private constructor() {
        companion object {
            fun newConfig() = Config()
        }

        var placeholder: Int = -1
        var error: Int = -1
        var placeholderDrawable: Drawable? = null
        var errorDrawable : Drawable? = null
        var isRound = false
        var roundRadius = 15
        var isBlur = false
        @FloatRange(from = 0.0,to = 25.0)
        var blurRadius = 15f
        var isCenterCrop = true
        var isCircle = false
        var isRevealScale = false
        var isReveal = false
        var isScale = false
        var cacheMode: DiskCacheStrategy = DiskCacheStrategy.RESOURCE
        var skipTransition = false
        var waitForLayout = true
    }


    companion object {

        val interceptor = ProgressInterceptor()

        private val INSTANT by lazy { TGlide(AppContext.get().context) }


        fun get(): TGlide = INSTANT


        fun ImageView.load(url: Any, config: Config,
                           viewTarget: DrawableImageViewTarget
        = DrawableImageViewTarget(this)){
            INSTANT.apply {
                val builder = GlideApp.with(this@load)
                    .load(url)
                if (config.placeholder != -1) {
                    builder.placeholder(config.placeholder)
                }
                if (config.placeholderDrawable != null) {
                    builder.placeholder(config.placeholderDrawable)
                }
                if (config.error != -1) {
                    builder.error(config.error)
                }
                if (config.errorDrawable != null) {
                    builder.error(config.errorDrawable)
                }
                val requestOptions : RequestOptions = when {
                    config.isRound -> {
                        RequestOptions.bitmapTransform(
                            if(config.isCenterCrop){
                            MultiTransformation(CenterCrop(),RoundedCorners(config.roundRadius))
                        }else{
                            RoundedCorners(config.roundRadius)
                        })
                    }
                    config.isCircle -> {
                        RequestOptions.circleCropTransform()
                    }
                    config.isBlur -> {
                        RequestOptions.bitmapTransform(BlurImageTransform(config.blurRadius))
                    }
                    else -> {
                        RequestOptions()
                    }
                }
                val transition = when {
                    config.isRevealScale ->{
                        allTransition
                    }
                    config.isReveal -> {
                        revealTransition
                    }
                    config.isScale -> {
                        scaleTransition
                    }
                    else -> {
                        fadeTransition
                    }
                }

                requestOptions.diskCacheStrategy(config.cacheMode)
                if(config.isCenterCrop && !config.isRound){
                    requestOptions.centerCrop()
                }
                if(config.waitForLayout){
                   if(!config.skipTransition){
                       builder.transition(transition)
                    }else{
                       builder
                   }.apply(requestOptions)
                        .into(viewTarget.waitForLayout())
                }else{
                    if(!config.skipTransition){
                        builder.transition(transition)
                    }else{
                        builder
                    }.apply(requestOptions)
                        .into( this@load)
                }
            }
        }

        fun ImageView.load(url: Any, load: (Config.() -> Unit)? = null) {
            val config = Config.newConfig()
            load?.invoke(config)
            load(url,config)
        }


        fun ImageView.loadProgress(url: String, callBack: ((progress: Float) -> Unit)) {
            INSTANT.apply {
                GlideApp.with(this@loadProgress)
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .transition(transitionOptions)
                    .into(this@loadProgress)
                addListener(url, object : ProgressListener {
                    override fun onProgress(progress: Float) {
                        callBack(progress)
                    }
                })
            }
        }


        fun loadIntoDrawable(url: Any, target: (drawable: Drawable) -> Unit) {
            INSTANT.apply {
                requestManager.load(url).into(object : DrawableTarget() {
                    override fun resourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        target.invoke(resource)
                    }
                })
            }
        }


        fun loadIntoBitmap(
            url: Any, width: Int = Target.SIZE_ORIGINAL,
            height: Int = Target.SIZE_ORIGINAL,
            target: (bitmap: Bitmap) -> Unit) {
            INSTANT.apply {
                requestManager.asBitmap().load(url).into(object : BitmapTarget(width, height) {
                    override fun resourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        target.invoke(resource)
                    }
                })
            }
        }

        suspend fun loadFile(url: Any, width: Int = Target.SIZE_ORIGINAL,
                                 height: Int = Target.SIZE_ORIGINAL) = withContext(Dispatchers.IO){
            kotlin.runCatching {
                INSTANT.requestManager
                    .asFile()
                    .load(url)
                    .submit(width,height)
                    .get()
            }.getOrNull()
        }

        suspend fun loadDrawable(url: Any, width: Int = Target.SIZE_ORIGINAL,
                               height: Int = Target.SIZE_ORIGINAL) = withContext(Dispatchers.IO){
            kotlin.runCatching {
                INSTANT.requestManager
                    .load(url)
                    .submit(width,height)
                    .get()
            }.getOrNull()
        }


        suspend fun loadBitmap(url: Any, width: Int = Target.SIZE_ORIGINAL,
                               height: Int = Target.SIZE_ORIGINAL) = withContext(Dispatchers.IO){
            kotlin.runCatching {
                INSTANT.requestManager
                    .asBitmap()
                    .load(url)
                    .submit(width,height)
                    .get()
            }.getOrNull()
        }



        fun resumeRequests() {
            INSTANT.apply {
                GlideApp.with(context.applicationContext).resumeRequests()
            }
        }

        fun pauseRequests() {
            INSTANT.apply {
                GlideApp.with(context.applicationContext).pauseRequests()
            }
        }
    }

}