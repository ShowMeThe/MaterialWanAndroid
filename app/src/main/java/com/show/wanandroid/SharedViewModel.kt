package com.show.wanandroid

import android.app.Application
import androidx.lifecycle.*
import com.show.kcore.base.AppContext
import com.show.kcore.extras.gobal.getAppViewModel

private val viewModel by lazy { SharedViewModel::class.java.getAppViewModel() }
fun getShareViewModel() = viewModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val popBack by lazy { MutableLiveData<Int>() }


}