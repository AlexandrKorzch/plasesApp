package net.caffee.places.util

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun md5(s: String): String {
    try {
        val mdEnc = MessageDigest.getInstance("MD5")
        mdEnc.update(s.toByteArray(), 0, s.length)
        val sBuilder = StringBuilder(BigInteger(1, mdEnc.digest()).toString(16))
        while (sBuilder.length < 32) {
            sBuilder.insert(0, "0")
        }
        return sBuilder.toString()
    } catch (e1: NoSuchAlgorithmException) {
        e1.printStackTrace()
    }

    return "Md5AlgorithmException"
}
