package com.show.kcore.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.show.kcore.extras.binding.Binding.binding


abstract class SimpleDialogFragment : DialogFragment() {


    private lateinit var mDialog: Dialog

    private var hasInflate = false

    private var onCreate: (() -> Int)? = null

    private var onWindow: (Window.() -> Unit)? = null

    private var onView: (View.() -> Unit)? = null

    abstract fun build(savedInstanceState: Bundle?)

    private var owner: LifecycleOwner? = null

    private fun init(owner: LifecycleOwner) {
        this.owner = owner
    }


    override fun onDestroy() {
        super.onDestroy()
        dismissAllowingStateLoss()
    }


    fun buildDialog(onCreate: (() -> Int)): SimpleDialogFragment {
        this.onCreate = onCreate
        return this
    }


    fun onWindow(onWindow: (Window.() -> Unit)): SimpleDialogFragment {
        this.onWindow = onWindow
        return this
    }

    inline fun <reified T : ViewBinding> View.onBindingView(onBindingView: (T.() -> Unit)) {
        onBindingView.invoke(binding<T>(this)!!)
    }


    fun onView(onView: (View.() -> Unit)): SimpleDialogFragment {
        this.onView = onView
        return this
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LifecycleOwner) {
            init(context)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val param = javaClass.getAnnotation(WindowParam::class.java)?.let {
            WindowParams(
                it.gravity,
                it.styleName,
                it.outSideCanceled,
                it.animRes,
                it.canceled,
                it.dimAmount
            )
        } ?: WindowParams()
        if (hasInflate) {
            return mDialog
        } else {
            mDialog = if (param.styleName.isNotEmpty()) {
                val styleId =
                    resources.getIdentifier(param.styleName, "style", context?.packageName)
                Dialog(requireContext(), styleId)
            } else {
                Dialog(requireContext())
            }
        }
        build(savedInstanceState)
        val viewId = onCreate?.invoke()
        if (viewId != null) {
            val gravity = param.gravity
            val outSideCanceled = param.outSideCanceled
            val canceled = param.canceled
            val dimAmount = param.dimAmount
            val animRes = param.animRes

            val dm = resources.displayMetrics

            val container = FrameLayout(requireContext())
            var view = LayoutInflater.from(requireActivity()).inflate(viewId, container, true)
            val child = (view as ViewGroup).getChildAt(0)
            child.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
            val childLayoutParams = child.layoutParams as ViewGroup.MarginLayoutParams

            val layoutParamsWidth = childLayoutParams.width
            val layoutParamsHeight = childLayoutParams.height


            val width: Int = when (layoutParamsWidth) {
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    dm.widthPixels - childLayoutParams.leftMargin - childLayoutParams.rightMargin
                }
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    child.measuredWidth
                }
                else -> {
                    layoutParamsWidth
                }
            }

            val height: Int = when (layoutParamsHeight) {
                ViewGroup.LayoutParams.MATCH_PARENT -> {
                    dm.heightPixels - childLayoutParams.topMargin - childLayoutParams.bottomMargin
                }
                ViewGroup.LayoutParams.WRAP_CONTENT -> {
                    child.measuredHeight
                }
                else -> {
                    layoutParamsHeight
                }
            }

            view = LayoutInflater.from(requireActivity()).inflate(viewId, null, true)
            mDialog.setContentView(view)
            mDialog.setCanceledOnTouchOutside(outSideCanceled)
            mDialog.setCancelable(canceled)


            val window = mDialog.window
            window?.apply {
                window.statusBarColor = Color.TRANSPARENT


                setLayout(width, height)

                setBackgroundDrawable(ColorDrawable(0x00000000))
                setGravity(gravity)
                if (animRes != -1) {
                    setWindowAnimations(animRes)
                }
                if (dimAmount != -1f) {
                    setDimAmount(dimAmount)
                }
                onWindow?.invoke(this)
            }

            onView?.invoke(view)

            hasInflate = true
        }
        return mDialog
    }


    override fun dismiss() {
        dialog?.apply {
            if (isShowing) {
                super.dismiss()
            }
        }
    }


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (!isAdded) {
                val transaction = manager.beginTransaction()
                transaction.add(this, tag)
                transaction.commitAllowingStateLoss()
                transaction.show(this)
            }
        } catch (e: Exception) {
            Log.e("DialogFragment", "${e.message}")
        }
    }

}