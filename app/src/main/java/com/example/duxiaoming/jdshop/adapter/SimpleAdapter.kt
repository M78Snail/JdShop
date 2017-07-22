package com.example.duxiaoming.jdshop.adapter

import android.content.Context


abstract class SimpleAdapter<T> : BaseAdapter<T, BaseViewHolder> {

    constructor(context: Context, layoutResId: Int) : super(context, layoutResId)

    constructor(context: Context, layoutResId: Int, datas: MutableList<T>?) : super(context, layoutResId, datas)


}
