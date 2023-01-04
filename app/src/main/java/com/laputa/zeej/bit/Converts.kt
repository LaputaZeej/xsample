package com.laputa.zeej.bit

import java.nio.ByteBuffer

fun Long.toByteArray(): ByteArray {
    val result = ByteArray(Long.SIZE_BYTES)
    // 0x 00 00 00 00 00 00 04 d2
    result[0] = (this ushr 56).toByte()
    result[1] = (this ushr 48).toByte()
    result[2] = (this ushr 40).toByte()
    result[3] = (this ushr 32).toByte()
    result[4] = (this ushr 24).toByte()
    result[5] = (this ushr 16).toByte()
    result[6] = ((this ushr 8) and 0xFF).toByte()
    result[7] = (this ushr 0 and 0xff).toByte()
//    val s = (this ushr 0 and 0xff)
//    println(" s = ${s.toString(16)}")
//    val b = s.toByte()
//    println(" b = ${b.toString(16)}")
    return result
}

fun ByteArray.toLong(debug: Boolean = false): Long {
    val now = this
    val size = Long.SIZE_BYTES
    val temp = ByteArray(size)
    // 05 06 07 08 09       8-5
    // 00 00 00 05 06 07 08 09
    val diff = size - now.size
    println("diff = $diff")
    temp.forEachIndexed { index, _ ->
        if (index < diff) {
            temp[index] = 0
        } else {
            temp[index] = now[index - diff]
        }
    }
//    val v0 = (temp[0].toInt() and 0xff).toLong() shl 56
//    val v1 = (temp[1].toInt()  and 0xff).toLong() shl 48
//    val v2 = (temp[2].toInt()  and 0xff).toLong() shl 40
//    val v3 = (temp[3].toInt()  and 0xff).toLong() shl 32
//    val v4 = (temp[4].toInt()  and 0xff).toLong() shl 24
//    val v5 = (temp[5].toInt()  and 0xff).toLong() shl 16
//    val v6 = (temp[6].toInt()  and 0xff).toLong() shl 8
//    val v7 = (temp[7].toInt()  and 0xff).toLong() shl 0
//    return v0 + v1 + v2 + v3 + v4 + v5 + v6 + v7

    fun ch(byte: Byte) = byte.toInt().apply {
        //println("   ch = ${this.toString(16)}")
    } and 0xff

//    var total = 0L
//    temp.forEachIndexed {
//        index,byte->
//        val m:Int   = size - index - 1
//        val next:Int = ch(byte)
//        total+=next.toLong() shl m*8
//    }
//    return total

    return temp.foldIndexed(0L) { index, acc, byte ->
        val m: Int = size - index - 1
        val next = ch(byte)
        val result: Long = if (debug) {
            (next shl m * 8).toLong() // 1
        } else {
            (next.toLong() shl m * 8) // 2
        }
        //println("   [$index]next = ${next.toString(16)} ,m=$m result=$result")
        acc + result
    }
}

fun main() {
    // 0x 00 00 00 00 00 00 04 d2
    val a = 1234L
    println("a = ${a.toString(16)}")
    val b = a.toByteArray()
    println("b = ${b.joinToString { it.toString(16) }}")
    println("0xd2 = ${0xd2.toString(16)}")
    val c = b.toLong()
    println("c = ${c.toString(16)}")


    fun test(value: ByteArray, debug: Boolean, tag: String) {
        println("---------------------------$tag")
        val f: Long = value.toLong(debug)
        println("f = ${f.toString(16)}")
        val g = f.toByteArray().toLong()
        println("g = ${g.toString(16)}")
    }

    // test(byteArrayOf(0x05,0x06,0x07,0x08),true)
//    test(byteArrayOf(0x05,0x06,0x07,0x08),false)

    val e = byteArrayOf(0x1f, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07)
    test(e, true, "e")
    test(e, false, "ee")


    // ...ByteBuffer
    println("=======================")
    println(e.toLongWithByteBuffer().toString(16))
}


fun ByteArray.toLongWithByteBuffer(): Long {
    return ByteBuffer.wrap(this).getLong()
}