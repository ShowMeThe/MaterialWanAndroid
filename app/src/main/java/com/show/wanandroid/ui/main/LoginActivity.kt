package com.show.wanandroid.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.Transition
import com.show.kcore.base.TransitionMode
import com.show.kcore.extras.binding.DialogFragmentRef
import com.show.kcore.extras.gobal.read
import com.show.kcore.extras.status.statusBar
import com.show.kcore.rden.Stores
import com.show.slideback.annotation.SlideBackBinder
import com.show.wanandroid.R
import com.show.wanandroid.bean.UserBean
import com.show.wanandroid.const.StoreConst
import com.show.wanandroid.databinding.ActivityLoginBinding
import com.show.wanandroid.dialog.LoadingDialog
import com.show.wanandroid.toast
import com.show.wanandroid.ui.main.vm.LoginViewModel
import com.showmethe.skinlib.SkinManager

@SlideBackBinder
@Transition(mode = TransitionMode.SlideBottom)
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var behavior: BottomSheetBehavior<View>
    private val dialog by DialogFragmentRef(LoadingDialog::class.java)

    override fun getViewId(): Int = R.layout.activity_login

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {

        viewModel.login.read(this, loading = {
            showLoading()
        }, error = { exception, t ->
            dismissLoading()
            t?.apply {
                toast(errorCode, errorMsg)
            }
        }) {
            dismissLoading()
            it?.data?.apply {
                Stores.putObject(StoreConst.UserInfo, viewModel.loginBean)
                Stores.put(StoreConst.IsLogin, true)
                Stores.put(StoreConst.UserName, viewModel.loginBean.account)
                toast(0, "登录成功")
                finishAfterTransition()
            }
        }

        viewModel.register.read(this, loading = {
            showLoading()
        }, error = { exception, t ->
            dismissLoading()
            t?.apply {
                toast(errorCode, errorMsg)
            }
        }) {
            dismissLoading()
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show()
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar {
            uiFullScreen(false)
        }
        binding {
            behavior = BottomSheetBehavior.from(bottomReg)

            main = this@LoginActivity

            login = Stores.getObject(StoreConst.UserInfo, viewModel.loginBean.apply {
                account = Stores.getString(StoreConst.UserName, "") ?: ""
            })
            register = viewModel.registerBean

            SkinManager.getManager().autoTheme(SkinManager.currentStyle, binding)


            executePendingBindings()


        }

    }

    override fun initListener() {

    }

    private fun showLoading() {
        dialog.show(supportFragmentManager, "dialog")
    }

    private fun dismissLoading() {
        dialog.dismiss()
    }

    fun onLogin() {
        val bean = viewModel.loginBean
        when {
            bean.account.isBlank() -> {
                Toast.makeText(
                    this,
                    getString(R.string.please_input_your_username),
                    Toast.LENGTH_SHORT
                ).show()
            }
            bean.password.isBlank() -> {
                Toast.makeText(
                    this,
                    getString(R.string.please_input_your_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                viewModel.loginIn()
            }
        }
    }


    fun onRegisterShow() {
        if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED
            || behavior.state == BottomSheetBehavior.STATE_HIDDEN
        ) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun onRegister() {
        val bean = viewModel.registerBean
        when {
            bean.account.isBlank() -> {
                Toast.makeText(
                    this,
                    getString(R.string.please_input_your_username),
                    Toast.LENGTH_SHORT
                ).show()
            }
            bean.password.isBlank() -> {
                Toast.makeText(
                    this,
                    getString(R.string.please_input_your_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                viewModel.register()
            }
        }
    }

    fun onBackPress() {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
}