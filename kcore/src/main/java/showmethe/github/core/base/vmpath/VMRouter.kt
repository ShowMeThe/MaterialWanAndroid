package showmethe.github.core.base.vmpath

import android.util.ArrayMap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import showmethe.github.core.util.extras.forEachBreak
import java.lang.reflect.Method
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation


class VMRouter(private var viewModel: ViewModel,var owner: LifecycleOwner) {

    companion object{

        val  callMap = ArrayMap<String,Method>()
    }

    fun toTarget(path: String,vararg args: Any?){

        if(callMap["${viewModel.javaClass.name}/${path}"] == null){
            viewModel.viewModelScope.launch(Dispatchers.Main) {
                try {
                    viewModel::class.java.methods.forEachBreak {
                        val annotation = it.getAnnotation(VMPath::class.java)
                        annotation?.apply {
                            if(this.path == path){
                                if(args.isNotEmpty()){
                                    it.invoke(viewModel,*args)
                                }else{
                                    it.invoke(viewModel)
                                }
                                callMap["${viewModel.javaClass.name}/${path}"] = it
                                return@forEachBreak false
                            }
                        }
                        true
                    }
                }catch (e:Exception){
                    Log.e("ViewModel","Exception  : ${e.message}")
                }
            }
        }else{
            try {
                callMap["${viewModel.javaClass.name}/${path}"]?.apply {
                    if(args.isNotEmpty()){
                        invoke(viewModel,*args)
                    }else{
                        invoke(viewModel)
                    }
                }
            }catch (e:Exception){
                Log.e("ViewModel","Exception  call : ${e.message}")
            }
        }


    }
}