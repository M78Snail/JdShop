package com.example.duxiaoming.jdshop.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager


/**
 * Created by duxiaoming on 2017/7/28.
 * blog:m78snail.com
 * description:
 */
class ManifestUtil {
    companion object {

        fun getMetaDataValue(context: Context, name: String, def: String): String {

            val value = getMetaDataValue(context, name)
            return value ?: def

        }

        fun getMetaDataValue(context: Context, name: String): String? {

            var value: Any? = null

            val packageManager = context.packageManager
            val applicationInfo: ApplicationInfo?

            try {

                applicationInfo = packageManager.getApplicationInfo(context.packageName, 128)
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    value = applicationInfo.metaData.get(name)
                }

            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException("Could not read the name in the manifest file.", e)
            }

            if (value == null) {
                throw RuntimeException("The name '$name' is not defined in the manifest file's meta data.")
            }

            return value.toString()

        }
    }

}