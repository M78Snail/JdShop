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
import com.cjj.MaterialRefreshListener
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.HWAdatper
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Page
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Response


class HotFragment : Fragment() {

    private var httpHelper = OkHttpHelper.mInstance
    private var curPage = 1
    private var pageSize = 10
    private var totalPage: Int = 0

    private var mAdapter: HWAdatper? = null
    private var mRecycleView: RecyclerView? = null

    private var datas: MutableList<Wares>? = null

    private var state = STATE_NORMAL

    private var mRefreshLayout: MaterialRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_hot, container, false)
        mRecycleView = view.findViewById(R.id.recyclerview) as RecyclerView?

        mRefreshLayout = view.findViewById(R.id.refresh_view) as MaterialRefreshLayout?
        getData()
        initRefreshLayout()
        return view

    }

    private fun getData() {
        val url: String = Contants.API.WARES_HOT + "?curPage=" + curPage + "&pageSize=" + pageSize
        httpHelper!!.get(url, object : SpotsCallBack<Page<Wares>>(context) {
            override fun onSuccess(response: Response, t: Page<Wares>) {
                datas = t.list
                curPage = t.currentPage
                totalPage = t.totalPage
                showData()
            }

            override fun onError(response: Response, code: Int, e: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun initRefreshLayout() {
        mRefreshLayout!!.setLoadMore(true)
        mRefreshLayout!!.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout?) {
                refreshData()
            }

            override fun onRefreshLoadMore(materialRefreshLayout: MaterialRefreshLayout?) {
                if (curPage < totalPage || curPage == totalPage) {
                    loadMoreData()
                } else {
                    mRefreshLayout!!.finishRefreshLoadMore()
                }
            }
        })
    }

    private fun showData() {
        when (state) {
            STATE_NORMAL -> {
                mAdapter = HWAdatper(context, datas!!)
                mRecycleView?.itemAnimator = DefaultItemAnimator()
                mRecycleView?.adapter = mAdapter
                mRecycleView?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))
                mRecycleView?.layoutManager = LinearLayoutManager(this.activity)
            }
            STATE_REFRESH -> {
                mAdapter!!.clear()
                mAdapter!!.addData(datas!!)
                mRecycleView!!.scrollToPosition(0)
                mRefreshLayout!!.finishRefresh()
            }
            STATE_MORE -> {
                mAdapter!!.addData(mAdapter!!.getDatas()!!.size, datas)
                mRecycleView!!.scrollToPosition(mAdapter!!.getDatas()!!.size)
                mRefreshLayout!!.finishRefreshLoadMore()
            }
        }
    }

    private fun refreshData() {
        curPage = 1
        state = STATE_REFRESH
        getData()
    }

    private fun loadMoreData() {
        curPage = ++curPage
        state = STATE_MORE
        getData()
    }

    companion object {
        val STATE_NORMAL = 0
        val STATE_REFRESH = 1
        val STATE_MORE = 2
    }
}
