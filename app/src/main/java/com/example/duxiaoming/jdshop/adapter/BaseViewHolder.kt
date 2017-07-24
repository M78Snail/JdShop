package com.example.duxiaoming.jdshop.adapter

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


open class BaseViewHolder(itemView: View, private val mOnItemClickListener: BaseAdapter.OnItemClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {


    private val views: SparseArray<View>

    init {
        itemView.setOnClickListener(this)
        this.views = SparseArray<View>()
    }

    fun getTextView(viewId: Int): TextView? {
        return retrieveView(viewId)
    }

    fun getButton(viewId: Int): Button? {
        return retrieveView(viewId)
    }

    fun getImageView(viewId: Int): ImageView? {
        return retrieveView(viewId)
    }

    fun getView(viewId: Int): View? {
        return retrieveView(viewId)
    }


    protected fun <T : View> retrieveView(viewId: Int): T? {


        var view: View? = views.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            views.put(viewId, view)
        }
        if (view == null) {
            return null
        }
        return view as T
    }


    override fun onClick(v: View) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener!!.onItemClick(v, layoutPosition)
        }
    }
}
