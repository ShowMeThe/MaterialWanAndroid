package com.ken.wanandroid.ui.login.fragment

import android.os.Bundle
import com.ken.wanandroid.R
import com.ken.wanandroid.databinding.FragmentLoginContainerBinding
import com.ken.wanandroid.ui.login.vm.LoginViewModel
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.post
import showmethe.github.core.util.extras.set

class LoginContainerFragment : BaseFragment<FragmentLoginContainerBinding, LoginViewModel>() {

    override fun initViewModel(): LoginViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_login_container

    override fun onBundle(bundle: Bundle) {

    }
    override fun observerUI() {

    }
    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this


    }

    override fun initListener() {

    }

    /**
     * 登录
     */
    fun onLogin(){
        viewModel.replaceFragment set  1
    }

    /**
     * 注册
     */
    fun onRegister(){
        viewModel.replaceFragment set 2
    }

}