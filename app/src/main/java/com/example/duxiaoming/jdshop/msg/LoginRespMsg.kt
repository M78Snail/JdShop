package com.example.duxiaoming.jdshop.msg


class LoginRespMsg<T> : BaseRespMsg() {


    var token: String? = null

    var data: T? = null
}
