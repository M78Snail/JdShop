package com.example.duxiaoming.jdshop.bean

import java.io.Serializable

/**
 * Created by [菜鸟窝](http://www.cniao5.com)
 * 一个专业的Android开发在线教育平台
 */
class ShoppingCart : Wares(), Serializable {


    var count: Int = 0
    var isChecked = true


}
