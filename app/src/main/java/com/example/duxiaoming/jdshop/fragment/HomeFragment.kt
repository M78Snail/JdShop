package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.DividerItemDecortion
import com.example.duxiaoming.jdshop.adapter.HomeCatgoryAdapter
import com.example.duxiaoming.jdshop.bean.Banner
import com.example.duxiaoming.jdshop.bean.HomeCategory
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Response


class HomeFragment : Fragment() {

    private var sliderShow: SliderLayout? = null
    private var indicator: PagerIndicator? = null

    private var mRecyclerView: RecyclerView? = null

    private var mAdatper: HomeCatgoryAdapter? = null
    private var mBanner: List<Banner>? = null
    private var httpHelper: OkHttpHelper = OkHttpHelper.mInstance!!

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        sliderShow = view.findViewById(R.id.slider) as SliderLayout
        indicator = view.findViewById(R.id.custom_indicator) as PagerIndicator?

        requestImages()
        initRecyclerView(view)
        return view
    }

    private fun requestImages() {
        val url: String = "http://112.124.22.238:8081/course_api/banner/query?type=1"

        httpHelper.get(url, object : SpotsCallBack<List<Banner>>(context) {
            override fun onSuccess(response: Response, t: List<Banner>) {
                Log.d("TAG>>>>>>>>>.", t.size.toString())
                mBanner = t
                initSlider()
            }

            override fun onError(response: Response, code: Int, e: Exception) {
            }


        })
    }

    private fun initSlider() {

        for (banner in mBanner!!) {
            val tsv = TextSliderView(activity)
            tsv.image(banner.imgUrl)
            tsv.description(banner.name)
            tsv.scaleType = BaseSliderView.ScaleType.Fit
            sliderShow!!.addSlider(tsv)

        }

        sliderShow!!.setCustomAnimation(DescriptionAnimation())
        sliderShow!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        sliderShow!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)

        sliderShow!!.setDuration(3000)
        sliderShow!!.setCustomIndicator(indicator)
    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview) as RecyclerView?

        val datas = ArrayList<HomeCategory>(15)

        var category = HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2)
        datas.add(category)

        category = HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2)
        datas.add(category)
        category = HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2)
        datas.add(category)

        category = HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2)
        datas.add(category)

        category = HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2)
        datas.add(category)


        mAdatper = HomeCatgoryAdapter(datas)

        mRecyclerView?.adapter = mAdatper
        mRecyclerView?.addItemDecoration(DividerItemDecortion())
        mRecyclerView?.layoutManager = LinearLayoutManager(this.activity)


    }

    override fun onDestroy() {
        super.onDestroy()

        sliderShow?.stopAutoCycle()
    }

}
