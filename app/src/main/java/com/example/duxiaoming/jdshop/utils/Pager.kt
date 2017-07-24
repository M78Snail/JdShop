package com.example.duxiaoming.jdshop.utils

import android.content.Context
import android.widget.Toast
import com.cjj.MaterialRefreshLayout
import com.cjj.MaterialRefreshListener
import com.example.duxiaoming.jdshop.bean.Page
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Response
import java.lang.reflect.Type


/**
 * Created by duxiaoming on 2017/7/23.
 * blog:m78snail.com
 * description: 下拉刷新，上拉加载
 */
class Pager {

    private val httpHelper: OkHttpHelper

    init {
        httpHelper = OkHttpHelper.mInstance as OkHttpHelper
        initRefreshLayout()
    }

    companion object {
        private var builder: Builder? = null
        private val STATE_NORMAL = 0
        private val STATE_REFREH = 1
        private val STATE_MORE = 2
        private var state = STATE_NORMAL

        fun newBuilder(): Builder {

            builder = Builder()
            return builder as Builder
        }

    }

    private fun initRefreshLayout() {
        builder?.mRefreshLayout?.setLoadMore(builder?.canLoadMore as Boolean)
        builder?.mRefreshLayout?.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout) {
                builder?.canLoadMore?.let { (builder?.mRefreshLayout as MaterialRefreshLayout).setLoadMore(it) }
                refresh()
            }

            override fun onRefreshLoadMore(materialRefreshLayout: MaterialRefreshLayout) {
                val a: Int = builder?.totalPage as Int
                val b: Int = builder?.pageIndex as Int
                if (a > b)
                    loadMore()
                else {
                    Toast.makeText(builder?.mContext, "无更多数据", Toast.LENGTH_LONG).show()
                    materialRefreshLayout.finishRefreshLoadMore()
                    materialRefreshLayout.setLoadMore(false)
                }
            }

        })
    }

    fun request() {

        requestData()
    }


    /**
     * 刷新数据
     */
    private fun refresh() {

        state = STATE_REFREH
        builder?.pageIndex = 1
        requestData()
    }

    /**
     * 隐藏数据
     */
    private fun loadMore() {

        state = STATE_MORE
        builder?.pageIndex = (builder?.pageIndex)?.inc() as Int
        requestData()
    }

    /**
     * 请求数据
     */
    private fun requestData() {

        val url = buildUrl()

        httpHelper.get(url, RequestCallBack<Any>(builder?.mContext as Context))

    }

    private fun buildUrl(): String {
        return builder?.mUrl + "?" + buildUrlParams()
    }

    private fun buildUrlParams(): String {
        val map = builder?.params

        builder?.pageIndex?.let { map?.put("curPage", it) }
        builder?.pageSize?.let { map?.put("pageSize", it) }

        val sb = StringBuffer()
        if (map != null) {
            for ((key, value) in map) {
                sb.append(key + "=" + value)
                sb.append("&")
            }
        }
        var s = sb.toString()
        if (s.endsWith("&")) {
            s = s.substring(0, s.length - 1)
        }
        return s


    }

    /**
     * 显示数据
     */
    private fun <T> showData(datas: MutableList<T>, totalPage: Int, totalCount: Int) {

        val listener: OnPageListener<T> = builder?.onPageListener as OnPageListener<T>


        if (datas.isEmpty()) {
            Toast.makeText(builder?.mContext, "加载不到数据", Toast.LENGTH_LONG).show()
            return
        }

        if (state === STATE_NORMAL) {

            listener.load(datas, totalPage, totalCount)
        } else if (STATE_REFREH === state) {

            builder?.mRefreshLayout?.finishRefresh()


            listener.refresh(datas, totalPage, totalCount)

        } else if (STATE_MORE === state) {

            builder?.mRefreshLayout?.finishRefreshLoadMore()
            listener.loadMore(datas, totalPage, totalCount)

        }
    }

    class Builder {
        var totalPage: Int = 1
        var pageIndex: Int = 1
        var pageSize: Int = 10


        var mRefreshLayout: MaterialRefreshLayout? = null

        var canLoadMore: Boolean = true


        var mUrl: String? = null
        var mType: Type? = null
        var mContext: Context? = null

        var onPageListener: OnPageListener<*>? = null


        val params = HashMap<String, Any>(5)

        fun setUrl(url: String): Builder? {

            builder?.mUrl = url

            return builder
        }

        fun setPageSize(pageSize: Int): Builder? {
            this.pageSize = pageSize
            return builder
        }

        fun putParam(key: String, value: Any): Builder? {
            params.put(key, value)
            return builder
        }

        fun setLoadMore(loadMore: Boolean): Builder? {
            this.canLoadMore = loadMore
            return builder
        }

        fun setRefreshLayout(refreshLayout: MaterialRefreshLayout): Builder? {

            this.mRefreshLayout = refreshLayout
            return builder
        }


        fun <T> setOnPageListener(onPageListener: OnPageListener<T>): Builder? {
            this.onPageListener = onPageListener
            return builder
        }


        fun build(context: Context, type: Type): Pager {


            this.mType = type
            this.mContext = context

            valid()
            return Pager()

        }


        private fun valid() {


            if (this.mContext == null)
                throw RuntimeException("content can't be null")

            if (this.mUrl == null || "" == this.mUrl)
                throw RuntimeException("url can't be  null")

            if (this.mRefreshLayout == null)
                throw RuntimeException("MaterialRefreshLayout can't be  null")
        }


    }

    internal inner class RequestCallBack<T>(context: Context) : SpotsCallBack<Page<T>>(context) {

        init {
            super.mType = builder?.mType as Type
        }

        override fun onSuccess(response: Response, t: Page<T>) {
            builder?.pageIndex = t.currentPage
            builder?.pageSize = t.pageSize
            if (t.totalCount.mod(t.pageSize) == 0) {
                builder?.totalPage = t.totalCount / t.pageSize

            } else {

                builder?.totalPage = t.totalCount / t.pageSize + 1
            }


            showData(t.list, t.totalPage, t.totalCount)
        }

        override fun onError(response: Response, code: Int, e: Exception) {
            dismissDialog()
            Toast.makeText(builder?.mContext, "请求出错：" + e.message, Toast.LENGTH_LONG).show()

            if (STATE_REFREH == state) {
                builder?.mRefreshLayout?.finishRefresh()
            } else if (STATE_MORE == state) {

                builder?.mRefreshLayout?.finishRefreshLoadMore()
            }

        }

    }

    interface OnPageListener<T> {


        fun load(datas: MutableList<T>, totalPage: Int, totalCount: Int)

        fun refresh(datas: MutableList<T>, totalPage: Int, totalCount: Int)

        fun loadMore(datas: MutableList<T>, totalPage: Int, totalCount: Int)


    }

}