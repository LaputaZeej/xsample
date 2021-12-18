package com.laputa.zeej.std_0004_thread

import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.random.Random

class SemaphoreDemo(private val semaphore: Semaphore,val index:Int) : Thread() {

    override fun run() {
        super.run()
        try {
            if(semaphore.availablePermits()>0){
                println("has")
            }else{
                println("[${index}]排队....")
            }
            semaphore.acquire()
            println("[$index]doing.....")
            sleep(Random.nextLong(5000))
            println("[$index]complete!")
            semaphore.release()
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }
}

fun main() {
    println("----------------------start")
    val pool = Executors.newCachedThreadPool()
    val semaphore = Semaphore(2)
    (0..10).forEach {
        try {
            pool.submit(  SemaphoreDemo(semaphore,it))
        }catch (e:Throwable){
            e.printStackTrace()
        }
    }
    Thread.sleep(1*1000L)
    pool.shutdown()
    semaphore.acquireUninterruptibly(2)
    semaphore.release(2)
    println("----------------------end")
}