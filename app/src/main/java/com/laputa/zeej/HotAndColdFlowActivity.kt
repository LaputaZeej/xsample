package com.laputa.zeej

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class HotAndColdFlowActivity : AppCompatActivity() {
    private val viewModel: HotAndColdFlowViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_and_cold_flow)
        lifecycleScope.launchWhenResumed {
            viewModel.start()
        }
    }

    companion object{
        fun skip(activity: AppCompatActivity){
            activity.startActivity(Intent(activity,HotAndColdFlowActivity::class.java))
        }
    }
}

class HotAndColdFlowViewModel : ViewModel() {

    fun start() {
        val coldFlow = createFlow()
//        val hotFlow = createFlow()
//            .stateIn(viewModelScope, SharingStarted.Eagerly, 0)
        test(viewModelScope, coldFlow)
    }

    private fun test(
        coroutineScope: CoroutineScope,
        coldFlow: Flow<Int>
    ) {
        val job1 = coroutineScope.launch(Dispatchers.IO) {
            coldFlow.collect {
                log("1->$it")
            }
        }

        val job2 = coroutineScope.launch(Dispatchers.IO) {
            coldFlow.collect {
                log("2->$it")
            }
        }

        coroutineScope.launch {
            delay(5000)
            job2.cancel()
            log("job2 cancel")
            delay(3000)
            val job3 = launch {
                coldFlow.collect {
                    log("3->$it")
                }
            }
            log("job3 start")
            delay(5000)
            job1.cancel()
            job3.cancel()
            log("job1 job3 cancel")
            val job4 = launch {
                coldFlow.collect {
                    log("4->$it")
                }
            }
            log("job4 start")
            delay(2000)
            job4.cancel()
            log("job4 cancel")
            delay(2000)
            //coroutineScope.cancel()
            log("coroutineScope cancel")
        }
    }

    private fun createFlow(): Flow<Int> = flow<Int> {
        var index = 0
        while (true) {
            delay(1000)
            log("working $index ......")
            emit(index++)
        }
    }
    
    private fun log(msg:String){
        Log.i("ccccccc","$msg")
    }
}