package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjj.MaterialRefreshLayout
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.HWAdatper
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Page
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.utils.Pager
import com.google.gson.reflect.TypeToken


class HotFragment : Fragment(), Pager.OnPageListener<Wares> {


    private var mAdapter: HWAdatper? = null
    private var mRecycleView: RecyclerView? = null

    private var datas: MutableList<Wares>? = null


    private var mRefreshLayout: MaterialRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_hot, container, false)
        mRecycleView = view.findViewById(R.id.recyclerview) as RecyclerView?

        mRefreshLayout = view.findViewById(R.id.refresh_view) as MaterialRefreshLayout?
        initPager()
        return view

    }

    private fun initPager() {
        val pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                ?.setLoadMore(true)
                ?.setOnPageListener(this)
                ?.setPageSize(10)
                ?.setRefreshLayout(mRefreshLayout!!)
                ?.build(context, object : TypeToken<Page<Wares>>() {

                }.type)


        pager?.request()

    }


    override fun load(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mAdapter = HWAdatper(context, datas)
        mRecycleView?.itemAnimator = DefaultItemAnimator()
        mRecycleView?.adapter = mAdapter
        mRecycleView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))
        mRecycleView?.layoutManager = LinearLayoutManager(this.activity)
    }

    override fun loadMore(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mAdapter!!.addData(mAdapter!!.getDatas()!!.size, datas)
        mRecycleView!!.scrollToPosition(mAdapter!!.getDatas()!!.size)
        mRefreshLayout!!.finishRefreshLoadMore()
    }

    override fun refresh(datas: MutableList<Wares>, totalPage: Int, totalCount: Int) {
        mAdapter!!.clear()
        mAdapter!!.addData(datas)
        mRecycleView!!.scrollToPosition(0)
    }

}
