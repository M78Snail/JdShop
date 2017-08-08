package com.example.duxiaoming.jdshop.bean.city

import cniao5.com.cniao5shop.city.model.ProvinceModel
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * Created by duxiaoming on 2017/8/1.
 * blog:m78snail.com
 * description:
 */
class XmlParserHandler : DefaultHandler() {
    /**
     * 存储所有的解析对象
     */
    private var provinceList = ArrayList<ProvinceModel>()
    private var provinceModel = ProvinceModel()
    var cityModel = CityModel()
    var districtModel = DistrictModel()

    fun getDataList(): ArrayList<ProvinceModel> {
        return provinceList
    }

    override fun startDocument() {
        super.startDocument()
    }


    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
// 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = ProvinceModel()
            if (attributes != null) {
                provinceModel.name = attributes.getValue(0)
            }
            provinceModel.setCityList(mutableListOf<CityModel>())
        } else if (qName.equals("city")) {
            cityModel = CityModel()
            if (attributes != null) {
                cityModel.name = attributes.getValue(0)
            }
            cityModel.districtList = mutableListOf<DistrictModel>()
        } else if (qName.equals("district")) {
            districtModel = DistrictModel()
            if (attributes != null) {
                districtModel.name = attributes.getValue(0)
            }
            if (attributes != null) {
                districtModel.zipcode = attributes.getValue(1)
            }
        }
    }


    override fun endElement(uri: String?, localName: String?, qName: String?) {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.districtList?.add(districtModel)
        } else if (qName.equals("city")) {
            provinceModel.getCityList()?.add(cityModel)
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel)
        }
    }


    override fun characters(ch: CharArray?, start: Int, length: Int) {
        super.characters(ch, start, length)
    }

}