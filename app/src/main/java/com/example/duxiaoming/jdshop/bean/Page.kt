package com.example.duxiaoming.jdshop.bean

/**
 * Created by [菜鸟窝](http://www.cniao5.com)
 * 一个专业的Android开发在线教育平台
 */
class Page<T> {


    var currentPage: Int = 0
    var pageSize: Int = 0
    var totalPage: Int = 0
    var totalCount: Int = 0

    var list: MutableList<T>? = null
}
