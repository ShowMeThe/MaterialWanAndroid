package com.show.wanandroid.ui.tree.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import com.show.wanandroid.R
import com.show.wanandroid.colors
import com.show.wanandroid.databinding.FramentTreeBodyBinding
import com.show.wanandroid.entity.Tree
import com.show.wanandroid.ui.main.vm.MainViewModel
import com.show.wanandroid.ui.tree.adapter.TreeBodyAdapter
import com.showmethe.skinlib.SkinManager
import kotlinx.android.synthetic.main.frament_tree_body.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import showmethe.github.core.base.BaseFragment
import showmethe.github.core.base.LazyFragment
import showmethe.github.core.divider.RecycleViewDivider
import showmethe.github.core.http.coroutines.Result
import showmethe.github.core.util.extras.ObList
import showmethe.github.core.util.extras.set
import showmethe.github.core.util.extras.valueIsNull
import java.util.concurrent.ThreadLocalRandom

/**
 *  com.show.wanandroid.ui.tree.fragment
 *  2020/3/21
 *  10:57
 */
class TreeBodyFragment : LazyFragment<FramentTreeBodyBinding, MainViewModel>() {

    private val sp = ArrayMap<Int,ArrayList<Chip>>()
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
                            treeBody.showContent()
                            if(list.isEmpty()){
                                list.addAll(this)
                                for((index,tree) in list.withIndex()){
                                    addInGroup(index,tree)
                                }
                            }
                        }
                    }
                }
            }
        })
    }


    override fun init(savedInstanceState: Bundle?) {
        treeBody.setDefaultLoadingColor(ContextCompat.getColor(context,R.color.colorAccent))
        SkinManager.getInstant().autoTheme(SkinManager.currentStyle,binding)

        initAdapter()

        if(viewModel.tree.valueIsNull()){
            router.toTarget("getTree")
        }

    }

    override fun initListener() {


    }


    private fun initAdapter(){
        adapter = TreeBodyAdapter(context,sp,list)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
    }


    /**
     * 改用预先设置到数组，而非在Adapter添加
     */
    private fun addInGroup(position:Int,parent : Tree){
        val chips = ArrayList<Chip>()
        parent.children.forEach { bean ->
            val chip = View.inflate(context,R.layout.chip_tree_layout,null) as Chip
            chip.text =  bean.name
            chip.setTextColor(Color.WHITE)
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(colors[ThreadLocalRandom.current()
                .nextInt(0, colors.size)]))
            chip.setOnClickListener {
                 viewModel.treeNavigator set Pair(bean.id,bean.name)
            }
            chips.add(chip)
        }
        sp[position] = chips
    }


}