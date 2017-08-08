package com.example.duxiaoming.jdshop

object Contants {

    val COMPAINGAIN_ID = "compaigin_id"
    val WARE = "ware"

    val USER_JSON = "user_json"
    val TOKEN = "token"

    val DES_KEY = "Cniao5_123456"

    val REQUEST_CODE = 0
    val REQUEST_CODE_PAYMENT = 1


    object API {


        val BASE_URL = "http://112.124.22.238:8081/course_api/"

        val CAMPAIGN_HOME = BASE_URL + "campaign/recommend"

        val BANNER = BASE_URL + "banner/query"


        val WARES_HOT = BASE_URL + "wares/hot"
        val WARES_LIST = BASE_URL + "wares/list"
        val WARES_CAMPAIN_LIST = BASE_URL + "wares/campaign/list"
        val WARES_DETAIL = BASE_URL + "wares/detail.html"

        val CATEGORY_LIST = BASE_URL + "category/list"

        val LOGIN = BASE_URL + "auth/login"
        val REG = BASE_URL + "auth/reg"

        val USER_DETAIL = BASE_URL + "user/get?id=1"

        val ORDER_CREATE = BASE_URL + "/order/create"
        val ORDER_COMPLEPE = BASE_URL + "/order/complete"
        val ORDER_LIST = BASE_URL + "order/list"

        val ADDRESS_LIST = BASE_URL + "addr/list"
        val ADDRESS_CREATE = BASE_URL + "addr/create"
        val ADDRESS_UPDATE = BASE_URL + "addr/update"

        val FAVORITE_LIST = BASE_URL + "favorite/list"
        val FAVORITE_CREATE = BASE_URL + "favorite/create"


    }
}