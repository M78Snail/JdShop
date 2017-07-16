package com.example.duxiaoming.jdshop

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.duxiaoming.jdshop.bean.Tab
import com.example.duxiaoming.jdshop.fragment.*
import com.example.duxiaoming.jdshop.widget.FragmentTabHost

class MainActivity : AppCompatActivity() {

    private var mInflater: LayoutInflater? = null
    private var mTabhost: FragmentTabHost? = null
    private var mTabs = ArrayList<Tab>(5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTab()
    }

    private fun initTab() {
        val tab_home = Tab(HomeFragment::class.java, R.string.home, R.drawable.selector_icon_home)
        val tab_hot = Tab(HotFragment::class.java, R.string.hot, R.drawable.selector_icon_hot)
        val tab_category = Tab(CategoryFragment::class.java, R.string.catagory, R.drawable.selector_icon_category)
        val tab_cart = Tab(CartFragment::class.java, R.string.cart, R.drawable.selector_icon_cart)
        val tab_mine = Tab(MineFragment::class.java, R.string.mine, R.drawable.selector_icon_mine)
        mTabs.add(tab_home)
        mTabs.add(tab_hot)
        mTabs.add(tab_category)
        mTabs.add(tab_cart)
        mTabs.add(tab_mine)

        mInflater = LayoutInflater.from(this)
        mTabhost = findViewById(android.R.id.tabhost) as FragmentTabHost?
        mTabhost?.setup(this, supportFragmentManager, R.id.realtabcontent)

        for (tab in mTabs) {
            val tabSpec = mTabhost!!.newTabSpec(getString(tab.title))
            tabSpec.setIndicator(buildIndicator(tab))
            mTabhost!!.addTab(tabSpec, tab.fragment, null)
        }
        mTabhost!!.tabWidget.showDividers = LinearLayout.SHOW_DIVIDER_NONE
        mTabhost!!.currentTab = 0

    }

    private fun buildIndicator(tab: Tab): View {
        val view = mInflater!!.inflate(R.layout.tab_indicator, null)
        view.findViewById(R.id.icon_tab).setBackgroundResource(tab.icon)
        (view.findViewById(R.id.txt_indicator) as TextView).setText(tab.title)
        return view
    }
}
