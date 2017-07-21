package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cjj.MaterialRefreshLayout
import com.cjj.MaterialRefreshListener
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.BaseAdapter
import com.example.duxiaoming.jdshop.adapter.CategoryAdapter
import com.example.duxiaoming.jdshop.adapter.WaresAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerGridItemDecoration
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Banner
import com.example.duxiaoming.jdshop.bean.Category
import com.example.duxiaoming.jdshop.bean.Page
import com.example.duxiaoming.jdshop.bean.Wares
import com.example.duxiaoming.jdshop.http.BaseCallback
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response

class CategoryFragment : Fragment() {


    private var mHttpHelper = OkHttpHelper.mInstance
    private var mCategoryAdapter: CategoryAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mRecyclerviewWares: RecyclerView? = null
    private var sliderShow: SliderLayout? = null
    private var mBanner: List<Banner>? = null

    private var mRefreshLayout: MaterialRefreshLayout? = null


    private var currPage = 1
    private var totalPage = 1
    private val pageSize = 10
    private var category_id: Long = 0


    private val STATE_NORMAL = 0
    private val STATE_REFREH = 1
    private val STATE_MORE = 2

    private var state = STATE_NORMAL

    private var mWaresAdatper: WaresAdapter? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_category, container, false)
        init(view)
        initRefreshLayout()
        requestCategoryData()
        requestImages()
        return view
    }

    fun init(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_category) as RecyclerView?
        mRecyclerviewWares = view.findViewById(R.id.recyclerview_wares) as RecyclerView
        mRefreshLayout = view.findViewById(R.id.refresh_layout) as MaterialRefreshLayout
        sliderShow = view.findViewById(R.id.slider) as SliderLayout

    }

    fun requestCategoryData() {
        mHttpHelper!!.get(Contants.API.CATEGORY_LIST, object : BaseCallback<MutableList<Category>>(context) {
            override fun onBeforeRequest(request: Request) {
            }

            override fun onFailure(request: Request, e: Exception) {
            }

            override fun onResponse(response: Response) {
            }

            override fun onSuccess(response: Response, t: MutableList<Category>) {
                showCategoryData(t)
                if (t.isNotEmpty()) {
                    category_id = t[0].id
                    requestWares(category_id)
                }

            }

            override fun onError(response: Response, code: Int, e: Exception) {
            }


        })
    }

    private fun showCategoryData(categories: MutableList<Category>) {
        mCategoryAdapter = CategoryAdapter(context, categories)
        mCategoryAdapter!!.setOnItemClickListener(object : BaseAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val category: Category = mCategoryAdapter!!.getItem(position)!!
                category_id = category.id
                currPage = 1
                state = STATE_NORMAL

                requestWares(category_id)
            }

        })
        mRecyclerView!!.setAdapter(mCategoryAdapter)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))

    }


    private fun initRefreshLayout() {
        mRefreshLayout!!.setLoadMore(true)
        mRefreshLayout!!.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout?) {
                refreshData()
            }

            override fun onRefreshLoadMore(materialRefreshLayout: MaterialRefreshLayout?) {
                if (currPage < totalPage || currPage == totalPage) {
                    loadMoreData()
                } else {
                    mRefreshLayout!!.finishRefreshLoadMore()
                }
            }
        })
    }

    private fun refreshData() {

        currPage = 1

        state = STATE_REFREH
        requestWares(category_id)

    }

    private fun loadMoreData() {

        currPage = ++currPage
        state = STATE_MORE
        requestWares(category_id)

    }


    private fun requestImages() {
        val url: String = Contants.API.BANNER + "?type=1"

        mHttpHelper!!.get(url, object : SpotsCallBack<List<Banner>>(context) {
            override fun onSuccess(response: Response, t: List<Banner>) {
                mBanner = t
                initSlider()
            }

            override fun onError(response: Response, code: Int, e: Exception) {
            }


        })
    }

    private fun initSlider() {

        for (banner in mBanner!!) {
            val tsv = DefaultSliderView(context)
            tsv.image(banner.imgUrl)
            tsv.description(banner.name)
            tsv.scaleType = BaseSliderView.ScaleType.Fit
            sliderShow!!.addSlider(tsv)

        }

        sliderShow!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderShow!!.setCustomAnimation(DescriptionAnimation())
        sliderShow!!.setPresetTransformer(SliderLayout.Transformer.RotateDown)

        sliderShow!!.setDuration(3000)

    }

    private fun requestWares(categoryId: Long) {
        val url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + currPage + "&pageSize=" + pageSize

        mHttpHelper!!.get(url, object : SpotsCallBack<Page<Wares>>(context) {
            override fun onSuccess(response: Response, waresPage: Page<Wares>) {
                currPage = waresPage.currentPage
                totalPage = waresPage.totalPage
                showWaresData(waresPage.list)
            }

            override fun onError(response: Response, code: Int, e: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun showWaresData(wares: MutableList<Wares>) {
        when (state) {

            STATE_NORMAL ->

                if (mWaresAdatper == null) {
                    mWaresAdatper = WaresAdapter(context, wares)


                    mRecyclerviewWares!!.adapter = mWaresAdatper

                    mRecyclerviewWares!!.layoutManager = GridLayoutManager(context, 2)
                    mRecyclerviewWares!!.itemAnimator = DefaultItemAnimator()
                    mRecyclerviewWares!!.addItemDecoration(DividerGridItemDecoration(context));
                } else {
                    mWaresAdatper!!.clear()
                    mWaresAdatper!!.addData(wares)
                }

            STATE_REFREH -> {
                mWaresAdatper!!.clear()
                mWaresAdatper!!.addData(wares)

                mRecyclerviewWares!!.scrollToPosition(0)
                mRefreshLayout!!.finishRefresh()
            }

            STATE_MORE -> {
                mWaresAdatper!!.addData(mWaresAdatper!!.getDatas()!!.size, wares)
                mRecyclerviewWares!!.scrollToPosition(mWaresAdatper!!.getDatas()!!.size)
                mRefreshLayout!!.finishRefreshLoadMore()
            }
        }

    }
}



