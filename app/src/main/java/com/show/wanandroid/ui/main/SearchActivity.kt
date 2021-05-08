package com.show.wanandroid.ui.main

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import com.show.kcore.base.BaseActivity
import com.show.kcore.extras.keyborad.hideSoftKeyboard
import com.show.kcore.extras.status.statusBar
import com.show.slideback.annotation.SlideBackBinder
import com.show.slideback.annotation.SlideBackPreview
import com.show.wanandroid.R
import com.show.wanandroid.databinding.ActivitySearchBinding
import com.show.wanandroid.ui.main.vm.SearchViewModel
import java.lang.Exception

@SlideBackPreview
@SlideBackBinder
class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>() {


    override fun getViewId(): Int = R.layout.activity_search

    override fun onBundle(bundle: Bundle) {
    }

    override fun observerUI() {
    }

    override fun init(savedInstanceState: Bundle?) {
        statusBar { uiFullScreen(true) }

        binding {

            main = this@SearchActivity
            executePendingBindings()


            setEditTextCursor()


        }
    }

    override fun initListener() {
        binding {

            edSearch.setOnKeyListener { v, keyCode, event ->
                if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SEARCH)
                    && event.action == KeyEvent.ACTION_DOWN) {
                    onSearch()
                    hideSoftKeyboard()
                    return@setOnKeyListener true
                }
                false
            }


        }
    }


    fun onSearch() {
        val search = binding.edSearch.text?.toString()
        if(!search.isNullOrBlank()){
            startActivity<SearchResultActivity>(Bundle().apply {
                putString("title",search)
            },true)
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