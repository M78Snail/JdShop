package cniao5.com.cniao5shop.city.model;

import com.example.duxiaoming.jdshop.bean.city.CityModel

class ProvinceModel {
    var name: String? = null
    private var cityList: MutableList<CityModel>? = null

    constructor() {

    }

    constructor(name: String, cityList: MutableList<CityModel>) {
        this.name = name
        this.cityList = cityList
    }



    fun getCityList(): MutableList<CityModel>? {
        return cityList
    }

    fun setCityList(cityList: MutableList<CityModel>) {
        this.cityList = cityList
    }


}
