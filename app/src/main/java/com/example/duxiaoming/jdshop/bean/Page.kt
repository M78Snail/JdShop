package com.example.duxiaoming.jdshop.bean

/**
 * Created by [菜鸟窝](http://www.cniao5.com)
 * 一个专业的Android开发在线教育平台
 */
data class Page<T>(var currentPage: Int, var pageSize: Int, var totalPage: Int, var totalCount: Int, var list: MutableList<T>)
