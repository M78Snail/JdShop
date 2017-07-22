package com.example.duxiaoming.jdshop.utils

import android.content.Context
import android.widget.Toast

/**
 * ToastUtils


 */
object ToastUtils {

    fun show(context: Context, resId: Int) {
        show(context, context.resources.getText(resId), Toast.LENGTH_LONG)
    }

    fun show(context: Context, resId: Int, duration: Int) {
        show(context, context.resources.getText(resId), duration)
    }

    @JvmOverloads fun show(context: Context, text: CharSequence, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, text, duration).show()
    }

    fun show(context: Context, resId: Int, vararg args: Any) {
        show(context, String.format(context.resources.getString(resId), *args), Toast.LENGTH_LONG)
    }

    fun show(context: Context, format: String, vararg args: Any) {
        show(context, String.format(format, *args), Toast.LENGTH_LONG)
    }

    fun show(context: Context, resId: Int, duration: Int, vararg args: Any) {
        show(context, String.format(context.resources.getString(resId), *args), duration)
    }

    fun show(context: Context, format: String, duration: Int, vararg args: Any) {
        show(context, String.format(format, *args), duration)
    }
}
