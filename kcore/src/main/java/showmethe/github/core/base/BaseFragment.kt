package showmethe.github.core.base

import android.content.Intent
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.jeremyliao.liveeventbus.LiveEventBus
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import showmethe.github.core.R
import showmethe.github.core.base.vmpath.VMRouter
import showmethe.github.core.dialog.DialogLoading
import showmethe.github.core.livebus.LiveBusHelper
import showmethe.github.core.util.toast.ToastFactory

/**
 * Author: showMeThe
 * Update Time: 2019/10/16 10:58
 * Package Name:showmethe.github.core.base
 */
abstract class BaseFragment<V : ViewDataBinding,VM : BaseViewModel> : Fragment() {

    lateinit var router : VMRouter
    private val loadingDialog  =  lazy {  DialogLoading() }
    lateinit var context : BaseActivity<*,*>
    var rootView : View ? = null
    var  binding : V? = null
    lateinit var viewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as BaseActivity<*, *>
        if(arguments!=null){
            onBundle(requireArguments())
        }
        if (isLiveEventBusHere()) {
            LiveEventBus.get("LiveData",LiveBusHelper::class.java).observe(this,observer)
        }
        viewModel = initViewModel()
        router = VMRouter(viewModel,activity)
        viewModel.initOwner(router)
        lifecycle.addObserver(viewModel)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = setContentView(inflater,container,getViewId())
        view?.apply {
            binding = DataBindingUtil.bind(view.rootView)
        }
        binding?.lifecycleOwner = this
        return view
    }


    private var observer: Observer<LiveBusHelper> = Observer {
        it?.apply {
            onEventComing(this)
        }
    }

    open fun onEventComing(helper : LiveBusHelper) {

    }

    open fun sendEvent(helper: LiveBusHelper) {
        LiveEventBus.get("LiveData",LiveBusHelper::class.java).post(helper)
    }


    open fun sendEventDelay(helper: LiveBusHelper, delay: Long) {
        LiveEventBus.get("LiveData",LiveBusHelper::class.java).postDelay(helper, delay)
    }


    open fun isLiveEventBusHere(): Boolean {
        return false
    }


    fun showLoading(){
        childFragmentManager.executePendingTransactions()
        if(!loadingDialog.value.isAdded){
            loadingDialog.value.show(childFragmentManager,"")
        }
    }

    fun dismissLoading(){
        loadingDialog.value.dismissAllowingStateLoss()
    }

    fun showToast(message : String){
        ToastFactory.createToast(message)
    }


    inline fun <reified VM :ViewModel>createViewModel(bindActivity: Boolean = true) : VM{
        return if(bindActivity){
            activityViewModels<VM>{ViewModelProvider.AndroidViewModelFactory(requireActivity().application)}.value
        }else{
            return viewModels<VM> { ViewModelProvider.NewInstanceFactory()}.value
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observerUI()
        init(savedInstanceState)
        initListener()
    }


    /**
     * fragment可见的时候操作，取代onResume，且在可见状态切换到可见的时候调用
     */
    open fun onVisible() {


    }

    /**
     * fragment不可见的时候操作,onPause的时候,以及不可见的时候调用
     */
    open fun onHidden() {

    }

    inline fun <reified T>startActivity(bundle: Bundle? = null) {
        val intent = Intent(context, T::class.java)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        context.overridePendingTransition(R.anim.alpha_in,R.anim.alpha_out)
    }



    override fun onResume() {//和activity的onResume绑定，Fragment初始化的时候必调用，但切换fragment的hide和visible的时候可能不会调用！
        super.onResume()
        if (isAdded && !isHidden) {//用isVisible此时为false，因为mView.getWindowToken为null
            onVisible()
        }
    }

    override fun onPause() {
        if (isVisible)
            onHidden()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(loadingDialog.isInitialized()){
            loadingDialog.value.dismiss()
        }
    }

    //默认fragment创建的时候是可见的，但是不会调用该方法！切换可见状态的时候会调用，但是调用onResume，onPause的时候却不会调用
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onVisible()
        } else {
            onHidden()
        }
    }





    private fun setContentView(inflater: LayoutInflater,container: ViewGroup? ,resId: Int): View? {
        if (rootView == null) {
            rootView = inflater.inflate(resId, container,false)
        }else{
            container?.removeView(rootView)
        }
        return rootView
    }


    abstract fun initViewModel() : VM

    abstract fun getViewId() : Int

    abstract fun onBundle(bundle: Bundle)

    abstract fun observerUI()

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initListener()



}