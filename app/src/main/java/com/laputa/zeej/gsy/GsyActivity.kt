package com.laputa.zeej.gsy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.laputa.zeej.R
import com.laputa.zeej.databinding.ActivityGsyBinding
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

class GsyActivity :GSYBaseActivityDetail<StandardGSYVideoPlayer>() {

    lateinit var binding: ActivityGsyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_gsy)
         binding = ActivityGsyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
        initVideoBuilderMode()
    }

    companion object{
        private const val PATH = "https://laike-files-test.yunext.com/lexy/api/common/file?fileName=b2602b9e-5b16-4a49-9575-6f9e9a4acedd.mp4"

        private const val PATH_01 ="http://alvideo.ippzone.com/zyvd/98/90/b753-55fe-11e9-b0d8-00163e0c0248"

        fun ship(activity: Activity){

            activity.startActivity(Intent(activity,GsyActivity::class.java))
        }


    }

    override fun getGSYVideoPlayer(): StandardGSYVideoPlayer {
      return binding.detailPlayer
    }

    override fun getGSYVideoOptionBuilder(): GSYVideoOptionBuilder {

        return GSYVideoOptionBuilder()
            .setThumbImageView(ImageView(this))
            .setUrl(PATH)
            .setCacheWithPlay(false)
            .setVideoTitle("12222")
            .setRotateViewAuto(false)
            .setLockLand(false)
            .setShowFullAnimation(false)
            .setNeedLockFull(true)
            .setSeekRatio(1f)
    }

    override fun clickForFullScreen() {

    }

    override fun getDetailOrientationRotateAuto(): Boolean {
        return true
    }
}