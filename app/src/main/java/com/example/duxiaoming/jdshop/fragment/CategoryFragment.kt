package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.CategoryAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Banner
import com.example.duxiaoming.jdshop.bean.Category
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Response


class CategoryFragment : Fragment() {


    private var mHttpHelper = OkHttpHelper.mInstance
    private var mCategoryAdapter: CategoryAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var sliderShow: SliderLayout? = null
    private var mBanner: List<Banner>? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_category, container, false)
        init(view)
        requestCategoryData()
        requestImages()
        return view
    }

    fun init(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview_category) as RecyclerView?
        sliderShow = view.findViewById(R.id.slider) as SliderLayout

    }

    fun requestCategoryData() {
        mHttpHelper!!.get(Contants.API.CATEGORY_LIST, object : SpotsCallBack<MutableList<Category>>(context) {
            override fun onSuccess(response: Response, t: MutableList<Category>) {
                showCategoryData(t)
            }

            override fun onError(response: Response, code: Int, e: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        })
    }

    private fun showCategoryData(categories: MutableList<Category>) {
        mCategoryAdapter = CategoryAdapter(context, categories)
        mRecyclerView!!.setAdapter(mCategoryAdapter)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST))

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
}



