package com.show.wanandroid.ui.login.fragment

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.show.wanandroid.ui.main.MainActivity
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FragmentLoginBinding
import com.show.wanandroid.ui.login.vm.LoginViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.onGlobalLayout
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.extras.valueSameAs

/**
 * 登录注册都是这个界面
 */
class LoginInFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    private lateinit var behavior : BottomSheetBehavior<View>
    override fun initViewModel(): LoginViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_login

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this
        behavior = BottomSheetBehavior.from(bottomReg)
        ivBack.onGlobalLayout {
            if(viewModel.replaceFragment.valueSameAs(2)){
                onRegisterShow()
            }
        }


    }

    override fun initListener() {



    }

    /**
     * 登录 , 暂时登录到主页
     */
    fun onLogin(){
        startActivity<MainActivity>()
    }

    /**
     * 注册
     */
    fun onRegister(){

    }

    /**
     * 拉起注册
     */
    fun onRegisterShow(){
        if(behavior.state == BottomSheetBehavior.STATE_COLLAPSED || behavior.state == BottomSheetBehavior.STATE_HIDDEN){
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    fun onBackPress(){
        if(behavior.state == BottomSheetBehavior.STATE_EXPANDED){
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }else{
            viewModel.backPress set true
        }
    }

}