package com.show.kcore.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.show.kcore.R
import com.show.kcore.extras.binding.Binding.binding

import java.lang.Exception

abstract class SimpleBSheetDialogFragment  : BottomSheetDialogFragment()  {

    private var behavior: BottomSheetBehavior<*>? = null

    private lateinit var mDialog: BottomSheetDialog

    private var hasInflate = false

    private var onCreate :(()->Int)? = null

    private var onWindow :(Window.()->Unit)? = null

    private var onView :(View.()->Unit)? = null

    abstract fun build(savedInstanceState: Bundle?)

    private var owner : LifecycleOwner? = null

    private fun init(owner: LifecycleOwner){
        this.owner = owner
    }



    override fun onDestroy() {
        super.onDestroy()
        dismissAllowingStateLoss()
    }


    fun buildDialog(onCreate :(()->Int)) : SimpleBSheetDialogFragment {
        this.onCreate = onCreate
        return this
    }


    fun onWindow(onWindow :(Window.()->Unit)) : SimpleBSheetDialogFragment {
        this.onWindow = onWindow
        return this
    }

    inline fun <reified T : ViewBinding> View.onBindingView(onBindingView: (T.() -> Unit)) {
        onBindingView.invoke(binding<T>(this)!!)
    }


    fun onView(onView :(View.()->Unit)) : SimpleBSheetDialogFragment {
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
            BottomWindowParams(
                it.gravity,
                it.styleName,
                it.outSideCanceled,
                it.animRes,
                it.canceled,
                it.dimAmount
            )
        } ?: BottomWindowParams()
        if (hasInflate) {
            return mDialog
        }else{
            mDialog = if(param.styleName.isNotEmpty()){
                val styleId = resources.getIdentifier(param.styleName,"style",context?.packageName)
                BottomSheetDialog(requireContext(),styleId)
            }else{
                BottomSheetDialog(requireContext())
            }
        }
        build(savedInstanceState)
        val viewId = onCreate?.invoke()
        if(viewId!= null){

            val gravity  = param.gravity
            val outSideCanceled   = param.outSideCanceled
            val canceled  = param.canceled
            val dimAmount = param.dimAmount
            val animRes = param.animRes



            val view = View.inflate(activity, viewId, FrameLayout(requireContext()))

            mDialog.setContentView(view)
            behavior = BottomSheetBehavior.from<View>(view.parent as View)


            mDialog.setCanceledOnTouchOutside(outSideCanceled)
            mDialog.setCancelable(canceled)

            val window = mDialog.window
            window?.apply {
                setBackgroundDrawable(ColorDrawable(0x00000000))
                findViewById<FrameLayout>(R.id.design_bottom_sheet)
                    ?.setBackgroundColor(Color.TRANSPARENT)
                statusBarColor = Color.TRANSPARENT
                setGravity(gravity)

                if(dimAmount!=-1f){
                    setDimAmount(dimAmount)
                }
                if(animRes!=-1){
                    setWindowAnimations(animRes)
                }
                onWindow?.invoke(this)
            }

            onView?.invoke((view as ViewGroup).getChildAt(0))

            hasInflate = true
            return mDialog
        }
        return super.onCreateDialog(savedInstanceState)
    }


    fun getSheetBehavior() = behavior

    override fun dismiss() {
        dialog?.apply {
            if(isShowing){
                if(activity !=null){
                    super.dismiss()
                }
            }
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if(!isAdded){
                val transaction = manager.beginTransaction()
                transaction.add(this, tag)
                transaction.commitAllowingStateLoss()
                transaction.show(this)
            }
        }catch (e: Exception){}
    }

}