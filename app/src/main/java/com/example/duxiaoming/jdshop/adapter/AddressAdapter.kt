package com.example.duxiaoming.jdshop.adapter

import android.content.Context
import com.example.duxiaoming.jdshop.R
import com.example.duxiaoming.jdshop.bean.Address


/**
 * Created by duxiaoming on 2017/8/1.
 * blog:m78snail.com
 * description:
 */
class AddressAdapter(context: Context, datas: MutableList<Address>, var lisneter: AddressLisneter) : SimpleAdapter<Address>(context, R.layout.template_address, datas) {


    override fun convert(viewHoder: BaseViewHolder, item: Address) {
        viewHoder.getTextView(R.id.txt_name)?.text = item.consignee
        viewHoder.getTextView(R.id.txt_phone)?.text = replacePhoneNum(item.phone)
        viewHoder.getTextView(R.id.txt_address)?.text = item.addr

        val checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt)

        val isDefault = item.isDefault
        checkBox?.isChecked = isDefault


        if (isDefault) {
            checkBox?.text = "默认地址"
        } else {

            checkBox?.isClickable = true

            checkBox?.setOnCheckedChangeListener({ _, _ ->
                item.isDefault = true
                lisneter.setDefault(item)
            })


        }
    }

    fun replacePhoneNum(phone: String): String {

        return phone.substring(0, phone.length - phone.substring(3).length) + "****" + phone.substring(7)
    }


    interface AddressLisneter {


        fun setDefault(address: Address)

    }

}