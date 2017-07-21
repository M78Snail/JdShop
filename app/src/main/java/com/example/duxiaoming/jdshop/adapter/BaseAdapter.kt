package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * Created by duxiaoming on 2017/7/20.
 * blog:m78snail.com
 * description:
 */
abstract class BaseAdapter<T, H : BaseViewHolder> @JvmOverloads constructor(protected var context: Context, protected var layoutResId: Int, protected var mData: MutableList<T>? = null) : RecyclerView.Adapter<BaseViewHolder>() {


    private var mOnItemClickListener: OnItemClickListener? = null


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutResId, parent, false)
        val vh = BaseViewHolder(view, mOnItemClickListener)
        return vh
    }

    override fun onBindViewHolder(holder: BaseViewHolder?, position: Int) {
        val item = getItem(position)
        if (item != null) {
            convert(holder as H, item)
        }

    }

    protected abstract fun convert(viewHoder: H, item: T)


    override fun getItemCount(): Int {
        return mData!!.size
    }

    fun getItem(position: Int): T? {
        if (mData != null) {
            if (position >= mData!!.size) return null
        }
        return mData!!.get(position)
    }

    fun clear() {

        mData!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 从列表中删除某项
     * @param t
     */
    fun removeItem(t: T) {

        val position = mData!!.indexOf(t)
        mData!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun getDatas(): MutableList<T>? {

        return mData
    }


    fun addData(datas: MutableList<T>) {
        mData!!.addAll(datas)
        notifyDataSetChanged()
    }

    fun addData(position: Int, list: MutableList<T>?) {

        if (list != null && list.isNotEmpty()) {

            mData!!.addAll(position,list)
            notifyDataSetChanged()
        }
    }

    fun refreshData(list: List<T>?) {

        if (list != null && list.isNotEmpty()) {

            clear()
            val size = list.size
            for (i in 0..size - 1) {
                mData!!.add(i, list[i])
                notifyItemInserted(i)
            }

        }
    }

    fun loadMoreData(list: List<T>?) {

        if (list != null && list.isNotEmpty()) {

            val size = list.size
            val begin = mData!!.size
            for (i in 0..size - 1) {
                mData!!.add(list[i])
                notifyItemInserted(i + begin)
            }

        }

    }


}