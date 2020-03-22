package com.show.wanandroid.ui.tree.fragment

import android.os.Bundle
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.databinding.FramentTreeBodyBinding
import com.show.wanandroid.entity.Tree
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.tree.adapter.TreeBodyAdapter
import kotlinx.android.synthetic.main.frament_tree_body.*
import okhttp3.internal.notifyAll
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.set

/**
 *  com.show.wanandroid.ui.tree.fragment
 *  2020/3/21
 *  10:57
 */
class TreeBodyFragment : LazyFragment<FramentTreeBodyBinding, MainViewModel>() {

    private val list = ObList<Tree>()
    private lateinit var adapter : TreeBodyAdapter

    override fun initViewModel(): MainViewModel = createViewModel()

    override fun getViewId(): Int = R.layout.frament_tree_body

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {


        viewModel.tree.observe(this, Observer {
            it?.apply {
                when(status){
                    Result.Success ->{
                        response?.apply {
                            list.addAll(this)
                        }
                        treeBody.showContent()
                    }

                }
            }
        })


    }

    override fun init() {
        treeBody.setDefaultLoadingColor(ContextCompat.getColor(context,R.color.colorAccent))

        initAdapter()

       router.toTarget("getTree")




    }

    override fun initListener() {

        adapter.setOnChipClickListener { id, title ->
            viewModel.treeNavigator set Pair(id,title)
        }

    }


    private fun initAdapter(){
        adapter = TreeBodyAdapter(context,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
    }



}