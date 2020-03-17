package com.show.wanandroid.ui.article.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.show.wanandroid.entity.TabItem


class TabArticleAdapter(var list : List<Fragment>, var titles:ArrayList<TabItem>, fm: FragmentManager, behavior: Int) : FragmentPagerAdapter(fm, behavior) {

    override fun getPageTitle(position: Int): CharSequence? = titles[position].title
    override fun getCount(): Int = list.size
    override fun getItem(position: Int): Fragment = list[position]
}