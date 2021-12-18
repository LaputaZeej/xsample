package com.laputa.zeej.gsy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.laputa.zeej.R
import com.laputa.zeej.databinding.ActivityGsyBinding
import com.laputa.zeej.std_0010_kotlin.function.f
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class Gsy2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityGsyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_gsy)
        binding = ActivityGsyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)

        initPlayer()
    }

    private fun initPlayer() {

        binding.detailPlayer.titleTextView.visibility = View.GONE
        binding.detailPlayer.backButton.visibility = View.GONE
        GSYVideoOptionBuilder().setThumbImageView(ImageView(this).apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageResource(R.mipmap.ic_launcher)
        })
            .setIsTouchWiget(true)
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setAutoFullWithSize(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setUrl(PATH)
            .setCacheWithPlay(false)
            .setVideoTitle("sssss")
            .setNeedOrientationUtils(false)
            .setVideoAllCallBack(object : GSYSampleCallBack() {
                override fun onPrepared(url: String?, vararg objects: Any?) {
                    super.onPrepared(url, *objects)
                    isPlay = true
                }

                override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                    super.onQuitFullscreen(url, *objects)
                }
            })
            .setLockClickListener{
                v,lock->
            }
            .build(binding.detailPlayer)
        binding.detailPlayer.fullscreenButton.setOnClickListener {
            binding.detailPlayer.startWindowFullscreen(this,true,true)
        }

        binding.detailPlayer.startPlayLogic()

        Handler().postDelayed({
            binding.detailPlayer.setUp(PATH_01,true,"112131")
            binding.detailPlayer.startPlayLogic()
        },5000)

    }

    private var isPause = false
    private var isPlay = false


    override fun onPause() {
        super.onPause()
        binding.detailPlayer.onVideoPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        binding.detailPlayer.onVideoResume(false)
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isPlay){
            binding.detailPlayer.currentPlayer.release()
        }
    }

    override fun onBackPressed() {
        if(GSYVideoManager.backFromWindowFull(this))return
        super.onBackPressed()
    }




    companion object {
        private const val PATH =
            "https://laike-files-test.yunext.com/lexy/api/common/file?fileName=b2602b9e-5b16-4a49-9575-6f9e9a4acedd.mp4"

        private const val PATH_01 =
            "http://alvideo.ippzone.com/zyvd/98/90/b753-55fe-11e9-b0d8-00163e0c0248"

        fun ship(activity: Activity) {

            activity.startActivity(Intent(activity, Gsy2Activity::class.java))
        }


    }
}