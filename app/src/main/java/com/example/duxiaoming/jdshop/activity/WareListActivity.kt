package com.example.duxiaoming.jdshop.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.cjj.MaterialRefreshLayout
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.HWAdatper
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Page
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.utils.Pager
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.google.gson.reflect.TypeToken


class WareListActivity : BaseActivity(), Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener, View.OnClickListener {

    companion object {
        val TAG_DEFAULT = 0
        val TAG_SALE = 1
        val TAG_PRICE = 2

        val ACTION_LIST = 1
        val ACTION_GIRD = 2

    }

    private var mTablayout: TabLayout? = null

    private var mTxtSummary: TextView? = null

    private var mRecyclerview_wares: RecyclerView? = null

    private var mRefreshLayout: MaterialRefreshLayout? = null

    private var mToolbar: JDToolBar? = null

    private var orderBy = 0

    private var campaignId: Long = 0

    private var mWaresAdapter: HWAdatper? = null

    private var pager: Pager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_warelist)

        initView()
        campaignId = intent.getLongExtra(Contants.COMPAINGAIN_ID, 0)
        initToolBar()
        initTab()
        getData()

    }

    private fun initView() {
        mTablayout = findViewById(R.id.tab_layout) as TabLayout
        mTxtSummary = findViewById(R.id.txt_summary) as TextView
        mRecyclerview_wares = findViewById(R.id.recycler_view) as RecyclerView
        mRefreshLayout = findViewById(R.id.refresh_layout) as MaterialRefreshLayout
        mToolbar = findViewById(R.id.toolbar) as JDToolBar
    }

    private fun initToolBar() {
        mToolbar?.setNavigationOnClickListener {
            finish()
        }

        mToolbar?.setRightButtonIcon(R.drawable.icon_grid_32)
        mToolbar?.getRightButton()?.tag = ACTION_LIST
        mToolbar?.setRightButtonOnClickListener(this)
    }

    private fun initTab() {


        val tab1: TabLayout.Tab = mTablayout?.newTab() as TabLayout.Tab
        tab1.text = "默认"
        tab1.tag = TAG_DEFAULT
        mTablayout?.addTab(tab1)

        val tab2: TabLayout.Tab = mTablayout?.newTab() as TabLayout.Tab

        tab2.text = "价格"
        tab2.tag = TAG_PRICE
        mTablayout?.addTab(tab2)

        val tab3: TabLayout.Tab = mTablayout?.newTab() as TabLayout.Tab

        tab3.text = "销量"
        tab3.tag = TAG_SALE
        mTablayout?.addTab(tab3)



        mTablayout?.setOnTabSelectedListener(this)


    }

    private fun getData() {
        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                ?.putParam("campaignId", campaignId)
                ?.putParam("orderBy", orderBy)
                ?.setRefreshLayout(mRefreshLayout!!)
                ?.setLoadMore(true)
                ?.setOnPageListener(this)
                ?.build(this, object : TypeToken<Page<Wares>>() {}.type)
        pager?.request()
    }


    override fun load(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mTxtSummary?.text = "共有" + totalCount + "件商品"

        mWaresAdapter = HWAdatper(this, datas)
        mRecyclerview_wares?.adapter = mWaresAdapter
        mRecyclerview_wares?.layoutManager = LinearLayoutManager(this)
        mRecyclerview_wares?.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST))
        mRecyclerview_wares?.itemAnimator = DefaultItemAnimator()

    }

    override fun refresh(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mWaresAdapter!!.clear()
        mWaresAdapter!!.addData(datas)
        mRecyclerview_wares!!.scrollToPosition(0)
    }

    override fun loadMore(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mWaresAdapter!!.addData(mWaresAdapter!!.getDatas()!!.size, datas)
        mWaresAdapter!!.getDatas()!!.size.let { mRecyclerview_wares?.scrollToPosition(it) }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        orderBy = tab?.tag as Int
        pager?.putParam("orderBy", orderBy)
        pager?.request()
    }

    override fun onClick(v: View) {
        val action = v.tag as Int

        if (ACTION_LIST === action) {

            mToolbar?.setRightButtonIcon(R.drawable.icon_list_32)
            mToolbar?.getRightButton()?.tag = ACTION_GIRD

            mRecyclerview_wares!!.layoutManager = GridLayoutManager(this, 2)

            mWaresAdapter!!.resetLayout(R.layout.template_grid_wares)
            mRecyclerview_wares?.adapter = mWaresAdapter


        } else if (ACTION_GIRD === action) {


            mToolbar?.setRightButtonIcon(R.drawable.icon_grid_32)
            mToolbar?.getRightButton()?.tag = ACTION_LIST

            mRecyclerview_wares?.layoutManager = LinearLayoutManager(this)

            mWaresAdapter?.resetLayout(R.layout.template_hot_wares)
            mRecyclerview_wares?.adapter = mWaresAdapter


        }

    }

}
