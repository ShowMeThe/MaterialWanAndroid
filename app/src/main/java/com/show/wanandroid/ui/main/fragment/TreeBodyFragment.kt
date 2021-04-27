package com.show.wanandroid.ui.main.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.ArrayMap
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.show.kcore.base.BaseFragment
import com.show.kcore.extras.gobal.ioDispatcher
import com.show.kcore.extras.gobal.mainDispatcher
import com.show.kcore.extras.gobal.read
import com.show.wanandroid.R
import com.show.wanandroid.bean.Children
import com.show.wanandroid.bean.Tree
import com.show.wanandroid.colors
import com.show.wanandroid.databinding.FragmentTreeBodyBinding
import com.show.wanandroid.getShareViewModel
import com.show.wanandroid.ui.main.adapter.TreeBodyAdapter
import com.show.wanandroid.ui.main.vm.TreeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.ThreadLocalRandom
import kotlin.coroutines.resume

class TreeBodyFragment : BaseFragment<FragmentTreeBodyBinding, TreeViewModel>() {


    private val list by lazy { ObservableArrayList<Tree>() }
    private val chipSave by lazy { ArrayMap<Int, ArrayList<Chip>>() }
    val adapter by lazy { TreeBodyAdapter(requireContext(), chipSave, list) }
    val layoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )
    }

    override fun getViewId(): Int = R.layout.fragment_tree_body

    override fun onBundle(bundle: Bundle) {

    }

    override fun observerUI() {
        viewModel.trees.read(this) {
            it?.data?.apply {
                ioDispatcher {
                    val result = suspendCancellableCoroutine<Boolean> { continuation ->
                        forEachIndexed { index, tree ->
                            addInGroup(index,tree)
                        }
                        continuation.resume(true)
                    }
                    if(result){
                        withContext(Dispatchers.Main){
                            binding.smart.showContent()
                            list.clear()
                            list.addAll(this@apply)
                        }
                    }
                }
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        binding {
            main = this@TreeBodyFragment
            executePendingBindings()


            smart.showLoading()


        }

        viewModel.getTree()
    }

    override fun initListener() {

    }

    private fun addInGroup(position:Int,parent : Tree){
        val chips = ArrayList<Chip>()
        parent.children.forEach { bean ->
            val chip = View.inflate(context,R.layout.chip_tree_layout,null) as Chip
            chip.text =  bean.name
            chip.setTextColor(Color.WHITE)
            chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor(colors[ThreadLocalRandom.current()
                .nextInt(0, colors.size)]))
            chip.setOnClickListener {
                viewModel.navigator.value = bean.id to bean.name
            }
            chips.add(chip)
        }
        chipSave[position] = chips
    }

}