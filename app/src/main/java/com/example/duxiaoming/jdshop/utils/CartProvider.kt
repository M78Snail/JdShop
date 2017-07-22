package com.example.duxiaoming.jdshop.utils

import android.content.Context
import android.util.SparseArray
import com.example.duxiaoming.jdshop.bean.ShoppingCart
import com.example.duxiaoming.jdshop.bean.Wares
import com.google.gson.reflect.TypeToken


/**
 * Created by duxiaoming on 2017/7/21.
 * blog:m78snail.com
 * description:
 */
class CartProvider(var mContext: Context) {

    private var datas: SparseArray<ShoppingCart> = SparseArray(10)

    companion object {
        val CART_JSON = "cart_json"
    }

    fun put(cart: ShoppingCart) {

        var temp: ShoppingCart? = datas.get(cart.id!!.toInt())
        if (temp != null) {
            temp.count = temp.count + 1
        } else {
            temp = cart
            temp.count = 1
        }

        datas.put(cart.id!!.toInt(), temp)
        commit()

    }

    fun put(wares: Wares) {

        val cart = convertData(wares)
        put(cart)

    }

    fun update(cart: ShoppingCart) {
        datas.put(cart.id!!.toInt(), cart)
        commit()

    }

    fun delete(cart: ShoppingCart) {
        datas.delete(cart.id!!.toInt())
        commit()
    }

    fun getAll(): MutableList<ShoppingCart> {
        return getDataFromLocal()
    }

    private fun commit() {
        val carts: MutableList<ShoppingCart> = sparseToList()
        PreferencesUtils.putString(mContext, CART_JSON, JSONUtil.toJSON(carts))
    }

    private fun sparseToList(): MutableList<ShoppingCart> {
        val size = datas.size()

        val list: MutableList<ShoppingCart> = mutableListOf()
        for (i in 0..size - 1) {

            val add = list.add(datas.valueAt(i))
        }
        return list


    }

    private fun listToSparse() {
        val carts: MutableList<ShoppingCart> = getDataFromLocal()

        for (cart in carts) {

            datas.put(cart.id!!.toInt(), cart)
        }

    }

    private fun getDataFromLocal(): MutableList<ShoppingCart> {
        val json = PreferencesUtils.getString(mContext, CART_JSON, "")

        return JSONUtil.fromJson(json, object : TypeToken<MutableList<ShoppingCart>>() {}.type)
    }

    fun convertData(item: Wares): ShoppingCart {

        val cart = ShoppingCart()

        cart.id = item.id
        cart.description = item.description
        cart.imgUrl = item.imgUrl
        cart.name = item.name
        cart.price = item.price

        return cart
    }


}