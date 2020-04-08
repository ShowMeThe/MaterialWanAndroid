package com.show.wanandroid.ui.tree.fragment

import android.os.Bundle
import androidx.core.app.BundleCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.show.wanandroid.R
import com.show.wanandroid.const.Data
import com.show.wanandroid.const.Id
import com.show.wanandroid.databinding.FragmentTreeArticleBinding
import com.show.wanandroid.entity.Article
import com.show.wanandroid.ui.main.adapter.ArticleListAdapter
import com.show.wanandroid.ui.main.vm.MainViewModel
import kotlinx.android.synthetic.main.fragment_tree_article.*
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.*

/**
 *  com.show.wanandroid.ui.tree.fragment
 *  2020/3/21
 *  13:47
 */
class TreeArticleFragment : LazyFragment<FragmentTreeArticleBinding, MainViewModel>() {

    companion object{
        fun get(id:Int,title:String) : Fragment{
            val bundle = Bundle()
            bundle.putInt(Id,id)
            bundle.putString(Data,title)
            val fragment = TreeArticleFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var title = ""
    private var cid  = 0
    private val list = ObList<Article.DatasBean>()
    private lateinit var adapter : ArticleListAdapter
    private  val treeArticle = MutableLiveData<Result<Article>>()
    private val pager = MutableLiveData<Int>()

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.fragment_tree_article

    override fun onBundle(bundle: Bundle) {
        cid = bundle.getInt(Id,0)
        title = bundle.getString(Data,"")
    }

    override fun observerUI() {
        treeArticle.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        if(pager valueSameAs 0){
                            list.clear()
                        }
                        response?.apply {
                            list.addAll(datas)
                            onLoadSize(list.size)
                        }
                    }
                }
            }
        })

        pager.observe(this, Observer {
            it?.apply {
                router.toTarget("getTreeArticle",this,cid,treeArticle)
            }
        })

    }

    override fun init(savedInstanceState: Bundle?) {
        refresh.isRefreshing = true
        tvTitle.text = title
        ivBack.startAnimator()
        wave.startAnimator()

        initAdapter()


        if(treeArticle.valueIsNull()){
            pager set 0
        }
    }

    private fun initAdapter(){
        adapter = ArticleListAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
    }

    override fun initListener() {

        refresh.setOnRefreshListener {
            pager set 0
        }

        rv.setOnLoadMoreListener {
            pager plus 1
        }

        adapter.setOnLikeClickListener { item, isCollect ->
            if(isCollect){
                router.toTarget("homeCollect",item.id)
            }else{
                router.toTarget("homeUnCollect",item.id)
            }
        }


        ivBack.setOnClickListener {
            viewModel.treeNavigator set null
            viewModel.treeNavBack set true
        }

    }

    private fun onLoadSize(size: Int) {
        rv.finishLoading()
        refresh.isRefreshing = false
        if(size == 0){
            rv.setEnableLoadMore(false)
        }else{
            rv.setEnableLoadMore(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ivBack.stopAnimator()
        wave.stopAnimator()
    }
}