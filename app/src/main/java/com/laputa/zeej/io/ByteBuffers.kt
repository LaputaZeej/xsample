package com.laputa.zeej.io

import com.laputa.zeej.std_0010_kotlin.function.simple
import java.nio.ByteBuffer
import java.nio.ByteOrder

private fun ByteBuffer.print(tag:String=""){
    println("[$tag] ${this.toString()} remaining = ${this.remaining()}")
}

fun main() {
    //  https://blog.csdn.net/mrliuzhao/article/details/89453082

    // https://cloud.tencent.com/developer/article/1853890?from=15425
    println("----------a--------")
//    t1()
//    t2()
//    t3()
//    t4()
//    t5()
    t6()
    println("----------z--------")
}

// 创建
internal fun t1(){
    val b1 = ByteBuffer.allocate(16)
    b1.print()
    val b2 = ByteBuffer.wrap(ByteArray(16),2,8)
    b2.print()
    val b3 = ByteBuffer.allocateDirect(16)
    b3.print()
}

// 写
internal fun t2(){
    val b1 = ByteBuffer.allocate(16)
    b1.print("before")
    b1.put((0xff).toByte())
    b1.print("put 0xff")
    b1.putInt(123)
    b1.print("put 123")
    val remaining = b1.remaining()
    b1.put(ByteArray(remaining))
    b1.print("put ByteArray")

    val b2 = ByteBuffer.wrap(ByteArray(16),2,8)
    b2.print("b2")
    val remaining2 = b2.remaining()
    b2.put(ByteArray(remaining2))
    b2.print("put ByteArray")
}

// 写
internal fun t3(){
    val b1 = ByteBuffer.wrap(ByteArray(16),2,8)
    b1.print("b1")
    b1.putInt(122)
    b1.print("put")
    b1.rewind()
    b1.print("rewind:字节数组的完整拷贝")

    // 注意是flip方法才将position复位为0，同时也将limit的位置放置在了position之前所在的位置上。此时position到limit之间，就是之前写入的数据了，因此就可以调用各种get方法读取数据了。所以flip方法就是为了从写模式转换到读模式
    val b2 = ByteBuffer.wrap(ByteArray(16),2,8)
    b2.print("b2")
    b2.flip() // 写出模式
    // 相当于
    // buffer.limit(buffer.position());
    // buffer.position(0);
    b2.print("flip:position置0，limit移置position")

}

// 读
internal fun t4(){
    val b1 = ByteBuffer.wrap(ByteArray(16),2,8)
    b1.print("b1")
    b1.putInt(123)
    b1.putInt(456)
    b1.print("put")
    val s1 = b1.get(2)
    println("s1=$s1")
    b1.print("get")
    val s2 = b1.getInt(2)
    println("s2=$s2")
    b1.print("getInt")
    val s3 = b1.getLong(0)
    println("s3 = $s3")
    b1.print("getLong")

    b1.rewind()
    val s4 = b1.getInt(2)
    println("s4=$s4")

    b1.rewind()
    val dst = ByteArray(8)
    println("dst size = ${dst.size}")
    b1.get(dst,0,dst.size)
    println("dst size = ${dst.size}")
    b1.print("getByteArray")
    println(dst.joinToString { it.toString(10) })


}

// 大小端
// 大端 0x 00 00 22 90
// 小端 0x 22 90 00 00
internal fun t5(){
    val order = ByteOrder.nativeOrder()
    println("order = $order")
    val byteBuffer = ByteBuffer.wrap(byteArrayOf(0x22,(0x90).toByte(),0x00,0x00))
    println("order = ${byteBuffer.order()}")
    //byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
    println("order = ${byteBuffer.order()}")
    byteBuffer.print("byteBuffer")
    val s1 = byteBuffer.getInt(0)
    println("s1 = ${s1.toString(16)}")

    val s2 = Integer.reverseBytes(s1)
    println("s2=${s2.toString(16)}")
}

// 继续写入数据
internal fun t6(){
    // compact() 切换读取模式
    // position -> unRead pos +1
    // limit -> capacity
    // 00 01 02 03 04 A1 A2 A3 A4 00 00 00 00 00 00 00 //  capacity:16 limit = 9 position:5
    // A1 A2 A3 A4 04 A1 A2 A3 A4 00 00 00 00 00 00 00 //  capacity:16 limit = 16 position:4

    val bytes = byteArrayOf(0x00,0x01,0x02,0x03,0x04,0xA1.toByte(),0xA2.toByte(),0xA3.toByte(),0xA4.toByte(),0x00,0x00,0x00,0x00,0x00,0x00,0x00)
    val byteBuffer = ByteBuffer.wrap(bytes,5,4)
    byteBuffer.print("compact before")
    byteBuffer.compact()
    byteBuffer.print("compact after")
}