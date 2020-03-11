package showmethe.github.core.glide

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import showmethe.github.core.base.AppProvider
import showmethe.github.core.base.ContextProvider
import showmethe.github.core.widget.common.ProgressImageView
import java.io.*
import java.lang.ref.WeakReference
import java.util.HashMap

/**
 * showmethe.github.core.glide
 *
 * 2019/1/10
 **/
class TGlide private constructor(private var context: Context) {
    private val requestManager : RequestManager = GlideApp.with(context.applicationContext)
    private var transitionOptions = DrawableTransitionOptions()
        .crossFade()
    private var requestOptions: RequestOptions? = null
    private val cacheMode = DiskCacheStrategy.ALL


    companion object {

        val interceptor = ProgressInterceptor()

        private  val INSTANT by lazy { TGlide(ContextProvider.get().context) }


        fun get() : TGlide = INSTANT

        fun load(url: Any, placeholder: Int, error: Int, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(
                        RequestOptions().diskCacheStrategy(cacheMode).centerCrop()
                            .placeholder(placeholder)
                            .error(error)
                    ).transition(transitionOptions)
                    .into(imageView)
            }
        }


        fun load(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode).centerCrop())
                    .transition(transitionOptions)
                    .into(imageView)
            }
        }


        fun loadReveal(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode).centerCrop())
                    .transition(DrawableTransitionOptions.with(DrawableScaleFadeFactory(350,isCrossFadeEnabled = true,isReveal = true)))
                    .into(imageView)
            }
        }

        fun loadRevealNoCrop(url: Any, imageView: ImageView) {
            INSTANT.apply {

                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode))
                    .transition(DrawableTransitionOptions.with(DrawableScaleFadeFactory(350,isCrossFadeEnabled = true,isReveal = true)))
                    .into(imageView)
            }
        }


        fun loadScale(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode).centerCrop())
                    .transition(DrawableTransitionOptions.with(DrawableScaleFadeFactory(350,true)))
                    .into(imageView)
            }
        }

        fun loadScaleNoCrop(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode))
                    .transition(DrawableTransitionOptions.with(DrawableScaleFadeFactory(350,true)))
                    .into(imageView)

            }
        }


        fun loadNoCrop(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode))
                    .transition(transitionOptions)
                    .into(imageView)
            }
        }


        fun loadWithCallBack(
            url: Any,
            imageView: ImageView,
            realCallBack: ((width: Int, height: Int) -> Unit)
        ) {
            INSTANT.apply {
                requestManager.asBitmap().load(url).into(object : BitmapTarget() {
                    override fun resourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        imageView.setImageBitmap(resource)
                        realCallBack.apply {
                            invoke(resource.width, resource.height)
                        }
                    }
                })
            }
        }


        fun loadInBackground(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(url)
                    .transition(transitionOptions).into(object : DrawableViewTarget(imageView) {
                        override fun resourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            imageView.background = resource
                        }
                    })
            }
        }

        fun loadProgress(url:String,imageView : ProgressImageView){
            INSTANT.apply {
                requestManager
                    .load(url)
                    .apply(RequestOptions().diskCacheStrategy(cacheMode))
                    .transition(transitionOptions)
                    .into(imageView)
                interceptor.addListener(url,object : ProgressListener{
                    override fun onProgress(progress: Float) {
                        imageView.setPercentage(progress)
                    }
                })
           }
        }

        fun loadFile(url: String, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(File(url))
                    .transition(transitionOptions)
                    .into(imageView)
            }
        }

        fun loadWithKey(res: String, key: String, defaultImg: Int, imageView: ImageView) {
            INSTANT.apply {
                requestManager
                    .load(res)
                    .apply(
                        RequestOptions().diskCacheStrategy(cacheMode).signature(ObjectKey(key)).placeholder(defaultImg).error(
                            defaultImg
                        )
                    )
                    .into(imageView)
            }
        }

        //加载圆型的图片
        fun loadCirclePicture(url: Any, placeholder: Int, error: Int, imageView: ImageView) {
            INSTANT.apply {
                requestOptions = RequestOptions.circleCropTransform()
                requestOptions?.apply {
                    requestManager
                        .load(url).apply(placeholder(placeholder).error(error)).into(imageView)
                }
            }
        }

        //加载圆型的图片
        fun loadCirclePicture(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestOptions = RequestOptions.circleCropTransform()
                requestOptions?.apply {
                    requestManager.load(url).apply(this).into(imageView)
                }
            }
        }


        //加载圆角的图片
        fun loadRoundPicture(url: Any, placeholder: Int, error: Int, imageView: ImageView) {
            INSTANT.apply {
                requestOptions = RequestOptions.bitmapTransform(RoundedCorners(4))
                requestOptions?.apply {
                    requestManager
                        .load(url).apply(placeholder(placeholder).error(error))
                        .into(imageView)
                }
            }

        }


        //加载圆角的图片(带半径）
        fun loadRoundPicture(url: Any, imageView: ImageView, radius: Int) {
            INSTANT.apply {
                val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(radius))
                requestManager.load(url).apply(requestOptions)
                    .transition(transitionOptions)
                    .into(imageView)
            }
        }


        fun loadCutPicture(url: Any, imageView: ImageView, radius: Int) {
            INSTANT.apply {
                requestManager.load(url)
                    .transform(CenterCrop(), GlideRoundCutTransform(radius.toFloat()))
                    .into(imageView)
            }
        }


        fun loadBlurPicture(url: Any, imageView: ImageView) {
            INSTANT.apply {
                requestOptions = RequestOptions.bitmapTransform(BlurImageTransform())
                requestOptions?.apply {
                    requestManager.load(url).apply(this)
                        .transition(transitionOptions)
                        .into(imageView)
                }
            }
        }


        fun loadBlurPicture(url: Any, imageView: ImageView, radius: Int) {
            INSTANT.apply {
                requestOptions = RequestOptions.bitmapTransform(BlurImageTransform(radius))
                requestOptions?.apply {
                    requestManager.load(url).apply(this)
                        .transition(transitionOptions)
                        .into(imageView)
                }

            }
        }

        //读取到Drawable
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

        //读取到Bitmap
        fun loadIntoBitmap(url: Any, target: (bitmap: Bitmap) -> Unit) {
            INSTANT.apply {
                requestManager.asBitmap().load(url).into(object : BitmapTarget() {
                    override fun resourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        target.invoke(resource)
                    }
                })
            }
        }


        //保存到指定文件夹
        fun saveInDir(
            storeDir: String,
            fileName: String,
            url: String,
            callBack: (path: String) -> Unit
        ) {
            INSTANT.apply {
                requestManager.asBitmap().load(url).into(object : BitmapTarget() {
                    override fun resourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        savePicture(storeDir, fileName, resource) {
                            callBack.invoke(it)
                        }
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


    //储存到本地操作
    @SuppressLint("CheckResult")
    @Throws(IOException::class)
    private fun savePicture(
        storeDir: String,
        fileName: String,
        bitmap: Bitmap,
        callBack: (path: String) -> Unit
    ) {
        val dir = File(storeDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val filePath = dir.path + "/" + fileName + ".png"
        val saveFile = File(filePath)
        if (saveFile.exists()) {
            if (dir.delete()) {
                dir.mkdirs()
                saveFile.createNewFile()
            }
        } else {
            saveFile.createNewFile()
        }
        GlobalScope.launch(Dispatchers.IO) {
            val fos = FileOutputStream(saveFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
            try {
                fos.flush()
                callBack.invoke(saveFile.path)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                fos.close()
            }
        }
    }


    /**
     * 获取视频第一帧图片
     * @param videoUrl
     * @param imageView
     */
    @SuppressLint("CheckResult")
    fun getVideoBitmap(videoUrl: String, imageView: ImageView) {
        GlobalScope.launch(Dispatchers.Main) {
            val map = HashMap<String, String>()
            val retriever = MediaMetadataRetriever()
            val bitmap: Bitmap?
            try {
                //根据url获取缩略图
                if (videoUrl.contains("http") || videoUrl.contains("https")) {
                    retriever.setDataSource(videoUrl, map)
                } else {
                    retriever.setDataSource(videoUrl)
                }
                //获得第一帧图片
                bitmap = retriever.frameAtTime
                imageView.setImageBitmap(bitmap)
            } catch (ee: IllegalArgumentException) {
                ee.printStackTrace()
            } finally {
                retriever.release()
            }
        }
    }


}