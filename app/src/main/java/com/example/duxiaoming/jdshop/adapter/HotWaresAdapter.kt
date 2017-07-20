package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Wares
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by duxiaoming on 2017/7/19.
 * blog:m78snail.com
 * description:
 */
class HotWaresAdapter(val mDatas: MutableList<Wares>, private val mContext: Context) : RecyclerView.Adapter<HotWaresAdapter.ViewHolder>() {


    private var mInflater: LayoutInflater? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wares = mDatas[position]
        holder.draweeView.setImageURI(Uri.parse(wares.imgUrl))
        holder.textTitle.text = wares.name
        holder.textPrice.text = "￥${wares.price}"


    }

    override fun getItemCount(): Int {

        return mDatas.size
    }

    fun getData(position: Int): Wares {

        return mDatas[position]
    }

    fun getDatas(): List<Wares> {

        return mDatas
    }

    fun clearData() {

        mDatas.clear()
        notifyItemRangeRemoved(0, mDatas.size)
    }

    fun addData(datas: List<Wares>) {

        addData(0, datas)
    }

    fun addData(position: Int, datas: List<Wares>?) {

        if (datas != null && datas.isNotEmpty()) {

            mDatas.addAll(datas)
            notifyItemRangeChanged(position, mDatas.size)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        mInflater = LayoutInflater.from(parent!!.context)
        return ViewHolder(mInflater!!.inflate(R.layout.template_hot_wares, null))

    }

    override fun getItemViewType(position: Int): Int {
        if (position % 2 == 0) {
            return VIEW_TYPE_R
        } else return VIEW_TYPE_L
    }

    companion object {
        var VIEW_TYPE_L: Int = 0
        var VIEW_TYPE_R: Int = 1
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var draweeView: SimpleDraweeView
        var textTitle: TextView
        var textPrice: TextView

        init {
            draweeView = itemView.findViewById(R.id.drawee_view) as SimpleDraweeView
            textTitle = itemView.findViewById(R.id.text_title) as TextView
            textPrice = itemView.findViewById(R.id.text_price) as TextView

        }
    }


}