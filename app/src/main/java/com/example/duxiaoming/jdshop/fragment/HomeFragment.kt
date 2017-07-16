package com.example.duxiaoming.jdshop.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.duxiaoming.jdshop.R


class HomeFragment : Fragment() {

    private var sliderShow: SliderLayout? = null
    private var indicator: PagerIndicator? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_home, container, false)

        sliderShow = view.findViewById(R.id.slider) as SliderLayout
        indicator = view.findViewById(R.id.custom_indicator) as PagerIndicator?
        initSlider()

        return view
    }

    private fun initSlider() {
        val imageUrls = ArrayList<String>(3)
        val descriptions = ArrayList<String>(3)
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t2416/102/20949846/13425/a3027ebc/55e6d1b9Ne6fd6d8f.jpg")
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1507/64/486775407/55927/d72d78cb/558d2fbaNb3c2f349.jpg")
        imageUrls.add("http://m.360buyimg.com/mobilecms/s300x98_jfs/t1363/77/1381395719/60705/ce91ad5c/55dd271aN49efd216.jpg")
        descriptions.add("新品推荐")
        descriptions.add("时尚男装")
        descriptions.add("家电秒杀")
        for (i in 0..imageUrls.size - 1) {
            val tsv = TextSliderView(activity)
            tsv.image(imageUrls[i]).description(descriptions[i])
            sliderShow!!.addSlider(tsv)
        }
        sliderShow!!.setCustomAnimation(DescriptionAnimation())
        sliderShow!!.setPresetTransformer(SliderLayout.Transformer.Accordion)
        sliderShow!!.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);

        sliderShow!!.setDuration(3000)
        sliderShow!!.setCustomIndicator(indicator)
    }
}
