package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.adapter.HomeCatgoryAdapter
import com.example.duxiaoming.jdshop.adapter.decoration.DividerItemDecoration
import com.example.duxiaoming.jdshop.bean.Banner
import com.example.duxiaoming.jdshop.bean.HomeCampaign
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.squareup.okhttp.Response


class HomeFragment : Fragment() {

    private var sliderShow: SliderLayout? = null

    private var mRecyclerView: RecyclerView? = null

    private var mAdatper: HomeCatgoryAdapter? = null
    private var mBanner: List<Banner>? = null
    private var httpHelper: OkHttpHelper = OkHttpHelper.mInstance!!

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        sliderShow = view.findViewById(R.id.slider) as SliderLayout

        requestImages()
        initRecyclerView(view)
        return view
    }

    private fun requestImages() {
        val url: String = "http://112.124.22.238:8081/course_api/banner/query?type=1"

        httpHelper.get(url, object : SpotsCallBack<List<Banner>>(context) {
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
            val tsv = TextSliderView(activity)
            tsv.image(banner.imgUrl)
            tsv.description(banner.name)
            tsv.scaleType = BaseSliderView.ScaleType.Fit
            sliderShow!!.addSlider(tsv)

        }

        sliderShow!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        sliderShow!!.setCustomAnimation(DescriptionAnimation())
        sliderShow!!.setDuration(3000)

    }

    private fun initRecyclerView(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerview) as RecyclerView?

        httpHelper.get(Contants.API.CAMPAIGN_HOME, object : SpotsCallBack<List<HomeCampaign>>(context) {
            override fun onSuccess(response: Response, t: List<HomeCampaign>) {
                initData(t)
            }

            override fun onError(response: Response, code: Int, e: Exception) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_LONG).show()
            }

        })

    }

    private fun initData(homeCampaigns: List<HomeCampaign>) {
        mAdatper = HomeCatgoryAdapter(homeCampaigns, context)
        mAdatper!!.setOnCampaignClickListener(object : HomeCatgoryAdapter.OnCampaignClickListener {
            override fun onClick(view: View, campaign: HomeCampaign) {
                Toast.makeText(context, "title=" + campaign.title, Toast.LENGTH_LONG).show()
            }

        })


        mRecyclerView?.adapter = mAdatper
        mRecyclerView?.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST))
        mRecyclerView?.layoutManager = LinearLayoutManager(this.activity)
    }

    override fun onDestroy() {
        super.onDestroy()

        sliderShow?.stopAutoCycle()
    }

}
