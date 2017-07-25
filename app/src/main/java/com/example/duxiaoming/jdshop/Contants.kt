package com.example.duxiaoming.jdshop

object Contants {

    val COMPAINGAIN_ID = "compaigin_id"
    val WARE = "ware"

    val USER_JSON = "user_json"
    val TOKEN = "token"

    val DES_KEY = "Cniao5_123456"

    val REQUEST_CODE = 0


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

        val USER_DETAIL = BASE_URL + "user/get?id=1"


    }
}