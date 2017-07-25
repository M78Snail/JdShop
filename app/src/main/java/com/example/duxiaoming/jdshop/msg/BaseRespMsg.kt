package com.example.duxiaoming.jdshop.msg

import java.io.Serializable


open class BaseRespMsg : Serializable {

    var status = STATUS_SUCCESS
    var message: String = ""

    companion object {

        val STATUS_SUCCESS = 1
        val STATUS_ERROR = 0
        val MSG_SUCCESS = "success"
    }
}
