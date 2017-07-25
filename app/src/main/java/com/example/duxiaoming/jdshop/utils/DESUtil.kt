package com.example.duxiaoming.jdshop.utils

import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec


object DESUtil {

    val ALGORITHM_DES = "DES/CBC/PKCS5Padding"

    /**
     * DES算法，加密

     * @param data
     * *         待加密字符串
     * *
     * @param key
     * *            加密私钥，长度不能够小于8位
     * *
     * @return 加密后的字节数组，一般结合Base64编码使用

     * *
     * @throws Exception
     */
    fun encode(key: String, data: String?): String? {
        if (data == null)
            return null
        try {
            val dks = DESKeySpec(key.toByteArray())
            val keyFactory = SecretKeyFactory.getInstance("DES")
            // key的长度不能够小于8位字节
            val secretKey = keyFactory.generateSecret(dks)
            val cipher = Cipher.getInstance(ALGORITHM_DES)
            val iv = IvParameterSpec("12345678".toByteArray())
            val paramSpec = iv
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec)
            val bytes = cipher.doFinal(data.toByteArray())
            return byte2String(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            return data
        }

    }


    /**
     * 二行制转字符串

     * @param b
     * *
     * @return
     */
    private fun byte2String(b: ByteArray?): String {
        val hs = StringBuilder()
        var stmp: String
        var n = 0
        while (b != null && n < b.size) {
            stmp = Integer.toHexString(b[n].toInt() and 0XFF)
            if (stmp.length == 1)
                hs.append('0')
            hs.append(stmp)
            n++
        }
        return hs.toString().toUpperCase(Locale.CHINA)
    }

    /**
     * 二进制转化成16进制

     * @param b
     * *
     * @return
     */
    private fun byte2hex(b: ByteArray): ByteArray {
        if (b.size % 2 != 0)
            throw IllegalArgumentException()
        val b2 = ByteArray(b.size / 2)
        var n = 0
        while (n < b.size) {
            val item = String(b, n, 2)
            b2[n / 2] = Integer.parseInt(item, 16).toByte()
            n += 2
        }
        return b2
    }
}
