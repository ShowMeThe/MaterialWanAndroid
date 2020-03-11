package com.ken.wanandroid.ui.login.fragment

import android.os.Bundle
import android.util.Log
import com.ken.wanandroid.R
import com.ken.wanandroid.databinding.FragmentLoginBinding
import com.ken.wanandroid.ui.login.vm.LoginViewModel
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.util.extras.set

class LoginInFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {

    override fun initViewModel(): LoginViewModel = createViewModel()
    override fun getViewId(): Int = R.layout.fragment_login

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding?.main = this


    }

    override fun initListener() {


    }

    fun onLogin(){

    }

    fun onBackPress(){
        viewModel.backPress set true
    }

}