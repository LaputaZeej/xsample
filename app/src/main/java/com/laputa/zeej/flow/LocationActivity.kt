package com.laputa.zeej.flow

import android.app.Activity
import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import com.laputa.zeej.databinding.ActivityLocationBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.properties.Delegates

/**
 *
 * https://blog.csdn.net/m0_57546986/article/details/118716227
 *
 * https://www.toutiao.com/a7001366218468901379/?channel=&source=search_tab
 */

class LocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MainScope().launch {
            //
        }

// [0] éåįžå­
        lifecycleScope.launchWhenResumed {
            LocationDataSource.locationFlow(this@LocationActivity)
                .collect {
                refreshInfo("[location]$it")
            }
        }

// [1]
//        lifecycleScope.launch {
//            TestDataSource.testFlow(this@LocationActivity)
//                .collect {
//                    refreshInfo("[onCreate]$it")
//                }
//        }

// [3]
//        TestDataSource.testFlow(this).autoCancel(this){
//            refreshInfo("[autoCancel]$it")
//        }

// [4]
//        addRepeatingJob(Lifecycle.State.STARTED){
//            TestDataSource.testFlow(this@LocationActivity).collect {
//                refreshInfo("[addRepeatingJob]$it")
//            }
//        }

        // [5]
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
//                TestDataSource.testFlow(this@LocationActivity).collect {
//                    refreshInfo("[5][repeatOnLifecycle]$it")
//                }
//            }
//        }

        // ã6ã
//        TestDataSource.testFlow(this)
//            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//            .onEach {
//                refreshInfo("[6][flowWithLifecycle]$it")
//            }.launchIn(lifecycleScope)

        // [7] įŧæååđķå
        // https://kotlinlang.org/docs/coroutines-basics.html#structured-concurrency
        // https://developer.android.google.cn/kotlin/coroutines
//        val job = lifecycleScope.launch {
//            async {
//                doSomeSuspendInitWork()
//            }
//
//            i("...................doSomeSuspendInitWork end")
//            addRepeatingJob(Lifecycle.State.STARTED) {
//                TestDataSource.testFlow(this@LocationActivity).collect {
//                    refreshInfo("[7][addRepeatingJob]$it")
//                }
//            } // åąéĐé·éąïžåžåŊäšäļäļŠæ§äļįŽĶåįŧæååđķåïž
//        }
//
//        lifecycleScope.launch {
//            delay(3000)
//            job.cancel()
//            i("kill job!")
//        }

    }


    private suspend fun doSomeSuspendInitWork() {
        var i = 0
        while (i<99){
            delay(1000)
            i++
            i("...................$i")
        }
        i("................... end")

    }


// [2]
//    private var job:Job by Delegates.notNull<Job>()
//
//    override fun onStart() {
//        super.onStart()
//        job = lifecycleScope.launch {
//            TestDataSource.testFlow(this@LocationActivity)
//                .collect {
//                    refreshInfo("[onStart]$it")
//                }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        job.cancel()
//    }

    private fun refreshInfo(info: String) {
        i(info)
        binding.tvInfo.text = info
    }

    companion object {
        fun ship(activity: Activity) {
            activity.startActivity(Intent(activity, LocationActivity::class.java))
        }
    }
}