package com.show.wanandroid

import android.app.Application
import androidx.lifecycle.*
import com.show.kcore.base.AppContext


val viewModelStore by lazy { SingleViewModelStore() }

inline fun <reified T : ViewModel> Class<T>.getAppViewModel() = ViewModelProvider(
    viewModelStore, ViewModelProvider.AndroidViewModelFactory(AppContext.get().context.applicationContext as Application)).get(this)

class SingleViewModelStore : ViewModelStoreOwner{
    companion object{
        private val modelStore by lazy { ViewModelStore() }
    }
    override fun getViewModelStore(): ViewModelStore = modelStore
}

private val viewModel by lazy { SharedViewModel::class.java.getAppViewModel() }
fun getShareViewModel() = viewModel

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val popBack by lazy { MutableLiveData<Int>() }


}