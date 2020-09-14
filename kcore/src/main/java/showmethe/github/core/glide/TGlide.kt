package showmethe.github.core.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.FloatRange
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import showmethe.github.core.base.ContextProvider


class TGlide private constructor(private var context: Context) {
    private val requestManager: RequestManager = GlideApp.with(context.applicationContext)
    private var transitionOptions = DrawableTransitionOptions()
        .crossFade()
    private val revealTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            250,
            isCrossFadeEnabled = true,
            isReveal = true
        )
    )

    private val scaleTransition = DrawableTransitionOptions.with(
        DrawableScaleFadeFactory(
            250,
            isCrossFadeEnabled = true,
            isReveal = false
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
        var isReveal = false
        var isScale = false
        var isCut = false
        var cutRadius = 15f
        var cacheMode = DiskCacheStrategy.ALL
        var isViewTarget = false
    }


    companion object {

        val interceptor = ProgressInterceptor()

        private val INSTANT by lazy { TGlide(ContextProvider.get().context) }


        fun get(): TGlide = INSTANT


        fun ImageView.load(url: Any, config: Config){
            INSTANT.apply {
                val builder = requestManager
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
                        RequestOptions.bitmapTransform(RoundedCorners(config.roundRadius))
                    }
                    config.isCircle -> {
                        RequestOptions.circleCropTransform()
                    }
                    config.isBlur -> {
                        RequestOptions.bitmapTransform(BlurImageTransform(config.blurRadius.toInt()))
                    }
                    config.isCut -> {
                        RequestOptions.bitmapTransform(GlideRoundCutTransform(config.cutRadius))
                    }
                    else -> {
                        RequestOptions()
                    }
                }
                val transition = when {
                    config.isReveal -> {
                        revealTransition
                    }
                    config.isScale -> {
                        scaleTransition
                    }
                    else -> {
                        this.transitionOptions
                    }
                }

                requestOptions.diskCacheStrategy(config.cacheMode)
                if(config.isCenterCrop){
                    requestOptions.centerCrop()
                }
                if(config.isViewTarget){
                    builder.transition(transition)
                        .apply(requestOptions)
                        .into( DrawableImageViewTarget(this@load)
                            .waitForLayout())
                }else{
                    builder.transition(transition)
                        .apply(requestOptions)
                        .into( this@load)
                }
            }
        }

        fun ImageView.load(url: Any, load: Config.() -> Unit) {
            val config = Config.newConfig()
            load.invoke(config)
            load(url,config)
        }


        fun ImageView.loadProgress(url: String, callBack: ((progress: Float) -> Unit)) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .transition(transitionOptions)
                    .into(this@loadProgress)
                interceptor.addListener(url, object : ProgressListener {
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



        fun resumeRequests() {
            INSTANT.apply {
                GlideApp.with(context).resumeRequests()
            }
        }

        fun pauseRequests() {
            INSTANT.apply {
                GlideApp.with(context).pauseRequests()
            }
        }
    }

}