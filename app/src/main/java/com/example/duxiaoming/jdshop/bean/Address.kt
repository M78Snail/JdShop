package com.example.duxiaoming.jdshop.bean

import java.io.Serializable

/**
 * Created by duxiaoming on 2017/8/1.
 * blog:m78snail.com
 * description:
 */
data class Address(var id: Long, var consignee: String, var phone: String, var addr: String, var zipCode: String, var isDefault: Boolean) : Serializable, Comparable<Address> {


    override fun compareTo(another: Address): Int {

        return another.isDefault.compareTo(this.isDefault)

    }

}