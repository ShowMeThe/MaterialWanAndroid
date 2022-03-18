package com.show.wanandroid.ui.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import com.google.android.material.chip.Chip
import com.show.kcore.base.BaseActivity
import com.show.kcore.base.startActivity
import com.show.kcore.extras.gobal.read
import com.show.kcore.extras.keyborad.hideSoftKeyboard
import com.show.kcore.extras.status.statusBar
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.R
import com.show.wanandroid.bean.KeyWord
import com.show.wanandroid.colors
import com.show.wanandroid.databinding.ActivitySearchBinding
import com.show.wanandroid.ui.main.vm.SearchViewModel
import java.util.concurrent.ThreadLocalRandom

@SlideBackPreview
@SlideBackBinder
class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {


    override fun getViewId(): Int = R.layout.activity_search

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {


        viewModel.hotKey
            .asLiveData()
            .read(this) {
                it?.data?.apply {
                    addGroup(this)
                }
            }

    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar { uiFullScreen(true) }

        binding {

            main = this@SearchActivity
            executePendingBindings()


            setEditTextCursor()

            viewModel.getHotKey()

        }
    }

    override fun initListener() {
        binding {

            edSearch.setOnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
                    && event.action == KeyEvent.ACTION_DOWN
                ) {
                    onSearch()
                    hideSoftKeyboard()
                    return@setOnKeyListener true
                }
                false
            }


        }
    }


    private fun addGroup(array: List<KeyWord>) {
        binding {
            group.removeAllViews()
            array.forEach { text ->
                val chip =
                    View.inflate(this@SearchActivity, R.layout.chip_tree_layout, null) as Chip
                chip.text = text.name
                chip.setTextColor(Color.WHITE)
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    Color.parseColor(
                        colors[ThreadLocalRandom.current()
                            .nextInt(0, colors.size)]
                    )
                )
                group.addView(chip)
                chip.setOnClickListener {
                    binding.edSearch.setText(text.name)
                    onSearch()
                }
            }
        }
    }

    fun onSearch() {
        val search = binding.edSearch.text?.toString()
        if (!search.isNullOrBlank()) {
            startActivity<SearchResultActivity>(Bundle().apply {
                putString("title", search)
            }, true)
        }
    }


    private fun setEditTextCursor() {
        binding {
            val cursor =
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.shape_blue_cursor)
                    ?.apply {
                        setTint(Color.WHITE)
                        setTintMode(PorterDuff.Mode.SRC_IN)
                    }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                edSearch.textCursorDrawable = cursor
            } else {
                try {
                    val field =
                        android.widget.TextView::class.java.getDeclaredField("mCursorDrawableRes")
                    field.isAccessible = true
                    field.set(edSearch, cursor)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}