package com.example.duxiaoming.jdshop.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import cniao5.com.cniao5shop.city.model.ProvinceModel
import com.bigkoo.pickerview.OptionsPickerView
import com.example.duxiaoming.jdshop.Contants
import com.example.duxiaoming.jdshop.JDApplication
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.city.XmlParserHandler
import com.example.duxiaoming.jdshop.http.OkHttpHelper
import com.example.duxiaoming.jdshop.http.SpotsCallBack
import com.example.duxiaoming.jdshop.msg.BaseRespMsg
import com.example.duxiaoming.jdshop.widget.ClearEditText
import com.example.duxiaoming.jdshop.widget.JDToolBar
import com.squareup.okhttp.Response
import javax.xml.parsers.SAXParserFactory


/**
 * Created by duxiaoming on 2017/8/1.
 * blog:m78snail.com
 * description:
 */
class AddressAddActivity : BaseActivity() {

    private var mCityPikerView: OptionsPickerView<String>? = null

    private var mTxtAddress: TextView? = null

    private var mEditConsignee: ClearEditText? = null

    private var mEditPhone: ClearEditText? = null

    private var mEditAddr: ClearEditText? = null

    private var mToolBar: JDToolBar? = null


    private var mProvinces: MutableList<ProvinceModel>? = null

    private val mCities = listOf<MutableList<String>>()
    private val mDistricts = listOf<MutableList<MutableList<String>>>()


    private val mHttpHelper = OkHttpHelper.mInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_add)
        initView()
        initPicker()

    }

    private fun initView() {
        mTxtAddress = findViewById(R.id.txt_address) as TextView?
        mEditConsignee = findViewById(R.id.edittxt_consignee) as ClearEditText?
        mEditPhone = findViewById(R.id.edittxt_phone) as ClearEditText?
        mEditAddr = findViewById(R.id.edittxt_add) as ClearEditText?
        mToolBar = findViewById(R.id.toolbar) as JDToolBar?
        findViewById(R.id.ll_city_picker).setOnClickListener {
            mCityPikerView?.show()
        }


    }

    private fun initToolbar() {

        mToolBar?.setNavigationOnClickListener { finish() }

        mToolBar?.setRightButtonOnClickListener(View.OnClickListener { createAddress() })

    }

    private fun initPicker() {

        initProvinceDatas()
        val builder = OptionsPickerView.Builder(this, OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, v ->
            //返回的分别是三个级别的选中位置
            val addresss = mProvinces!![options1].name + "  " + mCities[options1][options2] + "  " + mDistricts[options1][options2][options3]
            mTxtAddress?.text = addresss
        })

        mCityPikerView = builder.setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.GREEN)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("省", "市", "区")
                .setBackgroundId(0x66000000) //设置外部遮罩颜色
                .build() as OptionsPickerView<String>?



        mCityPikerView?.setPicker(convertList(mProvinces!!), mCities, mDistricts)


    }

    private fun convertList(mList: MutableList<ProvinceModel>): java.util.ArrayList<String> {
        val mDatas = java.util.ArrayList<String>(mList.size)
        mList.mapTo(mDatas) { it.name as String }
        return mDatas
    }

    fun initProvinceDatas() {

        val asset = assets
        try {
            val input = asset.open("province_data.xml")
            // 创建一个解析xml的工厂对象
            val spf = SAXParserFactory.newInstance()
            // 解析xml
            val parser = spf.newSAXParser()
            val handler = XmlParserHandler()
            parser.parse(input, handler)
            input.close()
            // 获取解析出来的数据
            mProvinces = handler.getDataList()

        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {

        }

        if (mProvinces != null) {

            mProvinces?.forEach { p ->

                val cities = p.getCityList()

                val cityStrs = ArrayList<String>(cities?.size as Int) //城市List


                for (c in cities) {

                    cityStrs.add(c.name) // 把城市名称放入 cityStrs


                    val dts = ArrayList<ArrayList<String>>() // 地区 List

                    val districts = c.districtList
                    val size: Int = districts!!.size
                    val districtStrs = ArrayList<String>(size)



                    for (d in districts) {
                        districtStrs.add(d.name) // 把城市名称放入 districtStrs
                    }
                    dts.add(districtStrs)


                    mDistricts.plus(dts)
                }

                mCities.plus(cityStrs) // 组装城市数据
            }
        }

    }

    fun createAddress() {


        val consignee = mEditConsignee?.text.toString()
        val phone = mEditPhone?.text.toString()
        val address = mTxtAddress?.text.toString() + mEditAddr?.text.toString()


        val params = HashMap<String, String>(1)
        params.put("user_id", JDApplication.mInstance?.getUser()?.id.toString())
        params.put("consignee", consignee)
        params.put("phone", phone)
        params.put("addr", address)
        params.put("zip_code", "000000")

        mHttpHelper?.post(Contants.API.ADDRESS_CREATE, params, object : SpotsCallBack<BaseRespMsg>(this@AddressAddActivity) {

            override fun onSuccess(response: Response, t: BaseRespMsg) {
                if (t.status == BaseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK)
                    finish()

                }
            }

            override fun onError(response: Response, code: Int, e: Exception) {

            }
        })

    }


}