package com.laputa.zeej.tsc

import android.graphics.Bitmap
import android.graphics.Matrix


fun checkBitmap(bitmap: Bitmap, dstWidth: Int, dstHeight: Int): Bitmap {
    try {
        val width = bitmap.width
        val height = bitmap.height
        BLog.i("checkBitmap", "w*h = $width*$height")

//        if (width * 1f / height < dstWidth * 1f / dstHeight) {
            val ratio = dstHeight * 1f / height // 这里只考虑按宽度比例进行缩放
//        val ratio = dstHeight * 1f / dstWidth
        BLog.i("checkBitmap", "ratio : $ratio")
//            val w = width * ratio
//            val h = height * ratio
//            BLog.i("checkBitmap", "w*h end = $w*$h")
        val matrix = Matrix()
        matrix.postScale(ratio, ratio)
        return Bitmap.createBitmap(bitmap, 0, 0, width.toInt(), height.toInt(), matrix, true)
            .apply {
                BLog.i("checkBitmap", "w*h over = ${this.width}*${this.height}")
            }
//        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return bitmap
}

fun rotationBimap(bitmap: Bitmap): Bitmap {
    // 说明：不旋转
//    try {
//        val matrix = Matrix()
//        val width = bitmap.width
//        val height = bitmap.height
//        matrix.postRotate(90f, width / 2f, width / 2f)
//        return Bitmap.createBitmap(bitmap, 0, 0, width.toInt(), height.toInt(), matrix, true)
//            .apply {
//                BLog.i("checkBitmap", "w*h over = ${this.width}*${this.height}")
//            }
//    } catch (e: Throwable) {
//        e.printStackTrace()
//    }
    return bitmap
}

//fun createQrCode(context: Context, msg: String, size: Int = App.INSTANCE.dp2px(220f)): Bitmap {
//    return QRAndBRCodeUtil.createQRImage(
//        msg,
//        size, size, null/*BitmapFactory.decodeResource(context.resources, R.drawable.applogo_qrcode)*/
//    )
//}