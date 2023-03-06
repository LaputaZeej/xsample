package com.laputa.zeej.tsc

import android.content.Context
import android.graphics.Bitmap
import com.example.tscdll.TSCActivity
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStream
import java.io.InputStreamReader
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface PrintRepository {
    fun init()
    suspend fun connect(mac: String): Boolean

    suspend fun printOver(): Boolean
    fun cancel()

    //    @Deprecated("delete")
//    suspend fun print(context: Context, parent: View, msg: String)
//
//    @Deprecated("delete")
//    suspend fun print(context: Context, codeData: List<CodeData>): Boolean
//
//    @Deprecated("delete")
//    suspend fun test(context: Context, text: String): Boolean
    suspend fun print(context: Context, source: Bitmap, count: Int): Boolean
}

class PrintRepositoryImpl01 : PrintRepository {
    private var mMac: String? = null
    private val tsc: TSC = TSC()

    override fun init() {

    }

    override suspend fun connect(mac: String): Boolean {
        this.mMac = mac
        return suspendCancellableCoroutine {
            try {
                val openport = tsc.openport(mac)
                li("openport:$openport")
                // val setup = tscActivity.setup(DEFAULT_W, DEFAULT_H, 3, 4, 0, 2, 0)
                // li("setup:$setup")
                val clearbuffer = tsc.clearbuffer()
                li("clearbuffer:$clearbuffer")
                Thread.sleep(DELAY)
                it.resume(true)
            } catch (e: Throwable) {
                e.printStackTrace()
                it.resumeWithException(e)
            }
        }
    }

    override suspend fun printOver(): Boolean {
        return suspendCancellableCoroutine {
            try {
                val mac = mMac
                if (mac.isNullOrBlank()) {
                    it.resume(false)
                    return@suspendCancellableCoroutine
                }
                val r = awaitReadyBeforePrint(mac)
                val sound = tsc.sendcommand("SOUND 5,200$END")
                li("SOUND 5,200 -> $sound")
                it.resume(r)
            } catch (e: Throwable) {
                it.resumeWithException(e)
            }
        }
    }

    private fun doPrint(command: Array<String>) {
        try {
            li("========= print start =========")
            li("command size = ${command.size}")
//            tscActivity.clearbuffer()
//            li("clearbuffer ok")
            command.forEach {
                li("$it  ........ ")
                val result = tsc.sendcommand(it + "\r\n")
                li("$it  ----> $result")
                Thread.sleep(DELAY)
            }
//            val printlabel = tscActivity.printlabel(1, 1)
//            li("printlabel : $printlabel")
        } catch (e: Throwable) {
            e.printStackTrace()
            le("print error = ${e.localizedMessage}")
        } finally {
            li("closeport end!")
        }
    }


//    override suspend fun test(context: Context, text: String): Boolean {
//        return suspendCancellableCoroutine {
//            try {
//                val setup = tscActivity.setup(DEFAULT_W, DEFAULT_H, 3, 8, 0, 3, 0)
//                val wDot = defaultWidthDot()
//                val hDot = defaultHeightDot()
//                li("setup:$setup mm[$DEFAULT_W,$DEFAULT_H],dot[$wDot,$hDot]")
//                tscActivity.clearbuffer()
//                Thread.sleep(DELAY)
//                val command01 = tscActivity.sendcommand("DIRECTION 0,0$END")
//                li("sendcommand DIRECTION :$command01")
//                Thread.sleep(DELAY)
//                // val bitmap = createQrCode(context, text)
//
//                val bitmap = BitmapFactory.decodeStream(context.assets.open("img.png"))
//                val width = bitmap.width
//                val height = bitmap.height
//                val x = (wDot - width) / 2
//                val y = (hDot - height) / 2
//                tscActivity.sendbitmap(x, y, bitmap)
//                Thread.sleep(DELAY * 2)
//                val status = tscActivity.status()
//                li("Status = $status")
//                Thread.sleep(DELAY)
//                tscActivity.printlabel(1, 1)
//                it.resume(true)
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                it.resumeWithException(e)
//            }
//        }
//    }

    override suspend fun print(context: Context, source: Bitmap, count: Int): Boolean {
        return suspendCancellableCoroutine {
            try {
                li("----------print-------------")
                val mac = mMac
                if (mac.isNullOrBlank()) {
                    it.resume(false)
                    return@suspendCancellableCoroutine
                }
                awaitReadyBeforePrint(mac)
                // density 15
                // speed
                val setup = tsc.setup(DEFAULT_W, DEFAULT_H, 3, mDensity, 0, 2, 0)
                val wDot = defaultWidthDot()
                val hDot = defaultHeightDot()
                val checkBitmap = checkBitmap(source, wDot, hDot)
                li("setup:$setup mm[$DEFAULT_W,$DEFAULT_H],dot[$wDot,$hDot],bitmap[${checkBitmap.width},${checkBitmap.height}]")

//                val matrix = Matrix()
//                matrix.setScale(0.1f, 0.1f)
//                val bitmap = Bitmap.createBitmap(
//                    checkBitmap, 0, 0, checkBitmap.getWidth(),
//                    checkBitmap.getHeight(), matrix, true
//                )
                val bitmap = checkBitmap
                val width = bitmap.width
                val height = bitmap.height
                li("setup:$setup mm[$DEFAULT_W,$DEFAULT_H],dot[$wDot,$hDot],bmpMatrixJpg[${bitmap.width},${bitmap.height}]")


                tsc.clearbuffer()
                Thread.sleep(DELAY)
                val command01 = tsc.sendcommand("DIRECTION 0,0$END")
                li("sendcommand DIRECTION :$command01")
                Thread.sleep(DELAY)
                //                val sound = tscActivity.sendcommand("SOUND 5,200$END")
                val sound = tsc.sendcommand("BEEP$END")
                li("BEEP :$sound")
                Thread.sleep(DELAY)
//                (0..hDot / 20).forEach { index ->
//                    tscActivity.bar(
//                        "0",
//                        "0",
//                        wDot.toString(),
//                        "2"
//                    )
//                    Thread.sleep(DELAY)
//                }
//                (0..wDot / 20).forEach { index ->
//                    tscActivity.bar(
//                        "0",
//                        "0",
//                        "2",
//                        hDot.toString(),
//                    )
//                    Thread.sleep(DELAY)
//                }

                val x = (wDot - width) / 2
//                val x = 0 // todo
                val y = (hDot - height) / 2
                tsc.sendbitmap(x, y, bitmap)
                Thread.sleep(DELAY)
                tsc.printlabel(1, count)
                li("printlabel : $count")
                val awaitReady = awaitReadyAfterPrint()
                it.resume(awaitReady)
//                var index = 0
//                it.resume(true)
//
//                while (true) {
//                    Thread.sleep(1000)
//                    val status = tscActivity.status()
//                    li("Status = $status")
//                    when (status) {
//                        "Ready" -> {
//                            index++
//                            if (index == 3) {
//                                it.resume(true)
//                                break
//                            }
//                        }
//                        else -> {
//                        }
//                    }
//                }
            } catch (e: Throwable) {
                e.printStackTrace()
                tsc.clearbuffer()
                it.resumeWithException(e)
            }
        }
    }

    private fun awaitReadyBeforePrint(mac: String): Boolean {
        li("awaitReadyBeforePrint:$mac")

        var index = 0
        var errorIndex = 0
        var reconnectIndex = 0
        while (true) {
            if (errorIndex > 3) {
                li("重连")
                tsc.closeport(DELAY.toInt())
                tsc.openport(mac, DELAY.toInt())
                errorIndex = 0
                reconnectIndex++
            }
            Thread.sleep(OPT_DELAY)
//            val printerstatus = tsc.printerstatus(100)
//
//            li("printerstatus = $printerstatus")
//            val printer_completestatus = tsc.printer_completestatus(100)
//            li("printer_completestatus =$printer_completestatus")
            val status = tsc.status()
            li("Status = [$status]")
            when (status) {
                TSCStatus.S00.text -> {
                    index++
                    if (index == 3) {
                        return true
                    }
                }
                TSCStatus.ERROR.text, "" -> {
                    errorIndex++
                    if (reconnectIndex >= 3) {
                        throw PrintException("请检查打印机")
                    }
                }
                TSCStatus.S06.text -> {
                    index++
                    if (index == 6) {
                        throw PrintException("请检查打印机")
                    }
                }
                TSCStatus.S05.text -> {
                    index++
                    if (index == 6) {
                        throw PrintException("请检查打印机")
                    }
                }
                else -> {
                }
            }

        }
    }

    private fun awaitReadyAfterPrint(): Boolean {
        li("awaitReadyAfterPrint")
        var index = 0
        while (index < 10) {
            Thread.sleep(OPT_DELAY)
            val status = tsc.status()
            li("Status = $status")
            when (status) {
                TSCStatus.S00.text -> {
                    index++
                    if (index >= 3) {

//                        val complete = tsc.printer_completestatus(100)
//                        li("complete = $complete")
                        return true
                    }
                }
                else -> {
                }
            }
        }
        throw PrintException("请检查打印机")
    }

    suspend fun test2(): Boolean {
        return suspendCancellableCoroutine {
            try {
                val setup = tsc.setup(DEFAULT_W, DEFAULT_H, 3, 3, 0, 3, 0)
                val wDot = defaultWidthDot()
                val hDot = defaultHeightDot()
                li("setup:$setup mm[$DEFAULT_W,$DEFAULT_H],dot[$wDot,$hDot]")
                tsc.clearbuffer()
                Thread.sleep(DELAY)
                val command01 = tsc.sendcommand("DIRECTION 0,0$END")
                li("sendcommand DIRECTION :$command01")
                Thread.sleep(DELAY)
                val text = "扫一扫 关注设备信息"
                val qrcode = "(00)0 8456460 000019706 4"
                val barcode = "(00)0 8456460 000019706 4"
                val size = 420
                val offset = 60
                val offset_y = size / 4
                val padding = 20 * 2
                val barHeight = size / 3
                val dot01 = (wDot - size) / 2 to offset * 2
                val dot02 = (wDot - size) / 2 to dot01.second + offset
                val dot03 = (wDot - size) / 2 to dot02.second + size + offset
                val dot04 = (wDot - size) / 2 to dot03.second + offset
                val fontSize = "4"

                // 画框框
                tsc.bar(
                    padding.toString(), padding.toString(),
                    "1",
                    (hDot - 2 * padding).toString()
                )
                tsc.bar(
                    padding.toString(), padding.toString(),
                    (wDot - 2 * padding).toString(), "1"
                )

                tsc.bar(
                    (wDot - padding).toString(), padding.toString(),
                    "1",
                    (hDot - 2 * padding).toString()
                )

                tsc.bar(
                    padding.toString(), (hDot - padding).toString(),
                    (wDot - 2 * padding).toString(), "1"
                )

                // 画扫一扫
                // tscActivity.extPrintFont(dot01.first, dot01.second, "4", 0, 0, 0, 2, "sao yi sao")
                tsc.printerfont(dot01.first, dot01.second, fontSize, 0, 0, 0, text)
                Thread.sleep(DELAY)
//                tscActivity.bar(
//                    perWidth.toString(),
//                    per.toString(),
//                    (9 * perWidth).toString(),
//                    "1"
//                )
//                tscActivity.bar(
//                    perWidth.toString(),
//                    per.toString(),
//                    "1",
//                    (9 * per).toString()
//                )
//                tscActivity.bar(
//                    (perWidth).toString(),
//                    (9 * per).toString(),
//                    (9 * perWidth).toString(),
//                    "1"
//                )
//                tscActivity.bar(
//                    (9 * perWidth).toString(),
//                    (per).toString(),
//                    "1",
//                    (9 * per).toString()
//                )
//                (0..10).forEach { index ->
//                    tscActivity.printerfont(0, index * per, "3", 0, 0, 0, "y$index")
//                    Thread.sleep(DELAY)
//                }
//
//                (0..10).forEach { index ->
//                    tscActivity.printerfont(index * perWidth, 9 * per, "3", 0, 0, 0, "x$index")
//                    Thread.sleep(DELAY)
//                }


//                (0..hDot / 10).forEach { index ->
//                    tscActivity.bar(
//                        "0",
//                        (index * 10).toString(),
//                        (9 * perWidth / 2).toString(),
//                        "1"
//                    )
//                    Thread.sleep(DELAY)
//                }

                // 画二维码
                tsc.qrcode(
                    dot02.first,
                    dot02.second,
                    "L",
                    "20",
                    "A",
                    "0",
                    "M1",
                    "S7",
                    qrcode
                )
                Thread.sleep(DELAY)

                // 画二维码文字
                tsc.printerfont(dot03.first, dot03.second, fontSize, 0, 0, 0, qrcode)
                Thread.sleep(DELAY)
                // 画条形码
                tsc.barcode(
                    dot04.first,
                    dot04.second,
                    "128",
                    barHeight,
                    2,
                    0,
                    3,
                    3,
                    barcode
                )
                Thread.sleep(DELAY)

                val status = tsc.status()
                li("Status = $status")
                Thread.sleep(DELAY)
                tsc.printlabel(1, 1)
                it.resume(true)
            } catch (e: Throwable) {
                e.printStackTrace()
                it.resumeWithException(e)
            }
        }

    }

    private suspend fun test1(context: Context, text: String): Boolean {
        return suspendCancellableCoroutine {
            try {
                // val setup = tscActivity.setup(DEFAULT_W, DEFAULT_H, 3, 4, 0, 2, 0)
                // li("setup:$setup")
//            val openFile = openFile(context, "barcode.tspl")
                val openFile = openFile(context, "qrcode.tspl")
                doPrint(openFile.toTypedArray())
                it.resume(true)
            } catch (e: Throwable) {
                e.printStackTrace()
                it.resumeWithException(e)
            }
        }

    }


    override fun cancel() {
        val clearbuffer = tsc.clearbuffer()
        li("clearbuffer:$clearbuffer")
        val closeport = tsc.closeport(500)
        li("closeport:$closeport")
    }

    suspend fun test01(context: Context, opt: Array<() -> String>) {


    }

//    override suspend fun print(context: Context, parent: View, msg: String) {
//        withContext(sPool.asCoroutineDispatcher()) {
//            try {
//
//                li("========= print start =========")
//                li("msg = $msg")
//                val clearbuffer = tscActivity.clearbuffer()
//                li("clearbuffer:$clearbuffer")
//                Thread.sleep(100)
//
//                var mag = ByteArray(1024)
//                mag = msg.toByteArray()
//                val command01 = tscActivity.sendcommand("DIRECTION 1")
//
//                li("sendcommand DIRECTION 1:$command01")
//                val command02 = tscActivity.sendcommand("CLS")
//                li("sendcommand CLS 1:$command02")
//                val barcode = tscActivity.barcode(100, 100, "128", 20, 2, 0, 3, 3, "1111122222333")
//                li("barcode:$barcode")
//                val printerfont = tscActivity.printerfont(40, 300, "4", 0, 2, 2, "xpl")
//                li("printerfont:$printerfont")
//                var bitmap: Bitmap? = null
//                bitmap = createQrCode(context, msg)
////                val file = saveQRCodeView(context, parent as ViewGroup,"xpl1213456")
////                if (file != null && file.exists()) {
////                    bitmap = createQrCode(context, msg)
//////                    BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
//////                BitmapFactory.decodeStream(context.assets.open("b.jpg")
//////                    bitmap = BitmapFactory.decodeFile(file.absolutePath)
////                }
//                if (bitmap != null) {
////                    val sendbitmap = tscActivity.sendbitmap(100, 100, bitmap)
////                    li("sendbitmap:$sendbitmap")
//                }
////                val command03 =
////                    tscActivity.sendcommand("QRCODE " + 0 + "," + 0 + ",H,5,A,0,M2,S7,\"哈哈哈\"")
////                li("sendcommand QRCODE:$command03")
//                val command04 = tscActivity.sendcommand("\n")
//                li("sendcommand \\n:$command04")
//                val command05 = tscActivity.sendcommand("TEXT 400,400,\"FONT001\",0,2,2,2,\"")
//                li("sendcommand TEXT:$command05")
//                val command06 = tscActivity.sendcommand(mag)
//                li("sendcommand $msg:$command06")
//                val command07 = tscActivity.sendcommand("\"\n")
//                li("sendcommand \\n :$command07")
//                val printlabel = tscActivity.printlabel(1, 1)
//                li("printlabel:$printlabel ")
//                li("end! ")
//
//                suspend fun printCode(id: String): Boolean {
//                    return commonRepository.codePrint(id)
//                }
//
//                val id = ""
//                var index = 0
//                var codePrint: Boolean
//                do {
//                    codePrint = printCode(id)
//                    delay(2000)
//                    index++
//                } while (!codePrint && index <= 3)
//                true
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                le("print error = ${e.localizedMessage}")
//                false
//            } finally {
////                val clearbuffer = tscActivity.clearbuffer()
////                li("clearbuffer:$clearbuffer")
////            val closeport = tscActivity.closeport(500)
//            }
//        }
//    }

    /**
     * 打印
     */
//    override suspend fun print(context: Context, codeData: List<CodeData>): Boolean {
//        return withContext(sPool.asCoroutineDispatcher()) {
//            //
//            codeData.forEach {
//
//            }
//
//            true
//        }
//    }

    companion object {
        private const val TAG = "_blue_print_"
        private fun li(msg: String) {
            BLog.i(TAG, msg)
        }

        private fun le(msg: String) {
            BLog.e(TAG, msg)
        }

        private var mDensity = 15
        val printDensity: Int
            get() = mDensity

        fun refreshDensity() {
//            mDensity++
//            if (mDensity > 15) {
//                mDensity = 6
//            }
        }

        val sPool = Executors.newSingleThreadExecutor()
        private const val DELAY = 50L
        private const val OPT_DELAY = 200L


        //        private const val DEFAULT_W = 100
//        private const val DEFAULT_H = 80
        private const val DPI = 300
        private const val DEFAULT_W = 50
        private const val DEFAULT_H = 60
//        private const val DPI = 200

        // 200dpi 1mm = 8 dot
        // 300dpi 1mm = 12 dot
        private fun Int.mm2dot(dpi: Int = DPI): Int {
            return when (dpi) {
                200 -> (this * 8)
                else -> this * 12
            }
        }

        private fun defaultWidthDot() = DEFAULT_W.mm2dot()
        private fun defaultHeightDot() = DEFAULT_H.mm2dot()

        private fun Int.dot2mm(dpi: Int = DPI): Int {
            return when (dpi) {
                200 -> (this / 8)
                else -> this / 12
            }
        }


        internal const val END = "\r\n"


        private fun Closeable?.closeSafely() {
            try {
                this?.close()
            } catch (ignore: Throwable) {

            }
        }

        private fun openFile(context: Context, name: String): List<String> {
            val list: MutableList<String> = mutableListOf()
            var inputStream: InputStream? = null
            var bufferedReader: BufferedReader? = null
            try {
                inputStream = context.assets.open(name)
                bufferedReader = BufferedReader(InputStreamReader(inputStream))
                var text: String?
                do {
                    text = bufferedReader.readLine()
                    if (text != null && text.isNotEmpty()) {
                        list.add(text)
                    }

                } while (text != null && text.isNotEmpty())

            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                inputStream?.closeSafely()
                bufferedReader.closeSafely()
            }
            return list

        }
    }
}

fun TSCActivity.extPrintFont(
    x: Int,
    y: Int,
    font: String = "0",
    rotation: Int = 0,
    x_multi: Int = 0,
    y_multi: Int = 0,
    alignment: Int = 2,
    content: String,
): String {
    val command =
        "TEXT $x,$y,\"$font\",$rotation,$x_multi,$y_multi,$alignment,\"$content\"${PrintRepositoryImpl01.END}"
    return sendcommand(command)
}



