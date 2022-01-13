package com.laputa.zeej

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.TransferListener
import com.laputa.zeej.compose.ComposeActivity
import com.laputa.zeej.databinding.ActivityMainBinding
import com.laputa.zeej.flow.LocationActivity
import com.laputa.zeej.gsy.GSYExoHttpDataSourceFactory
import com.laputa.zeej.gsy.Gsy2Activity
import com.laputa.zeej.gsy.GsyActivity
import com.laputa.zeej.std_0006_android.binder.case01.GradeActivity
import com.laputa.zeej.std_0006_android.binder.case02.ProxyGradeActivity
import com.laputa.zeej.std_0006_android.binder.case03.AIDLGradeActivity
import kotlinx.coroutines.delay
import tv.danmaku.ijk.media.exo2.ExoMediaSourceInterceptListener
import tv.danmaku.ijk.media.exo2.ExoSourceManager
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
        binding.sampleText.text = stringFromJNI()
        // gsy
        binding.sampleText.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0x01)
            Gsy2Activity.ship(this)
        }

        // flow
        binding.actionFlow.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),0x02)
            LocationActivity.ship(this)
        }

        binding.actionCompose.setOnClickListener {
            ComposeActivity.ship(this)
        }

        binding.actionBinder.setOnClickListener {
            GradeActivity.ship(this)
        }
        binding.actionBinderProxy.setOnClickListener {
            ProxyGradeActivity.ship(this)
        }

        binding.actionBinderAidl.setOnClickListener {
            AIDLGradeActivity.ship(this)
        }
        initGsy()

        // TextView为match_parent
        // 不报错
//        val t1 = "123"
//        // 报错 因为超过1行 要重新计算高度 触发了检查
//        val t = "123assssssss123assssssss123assssssss123assssssss123assssssss123assssssss123assssssss123assssssss123assssssss123assssssss"
//        Thread{
//            Thread.sleep(1000) // 在wrap_content情况下，延迟一定报错。
//            binding.tvInfo.setText(t1)
//        }.start()

    }

    /**
     * A native method that is implemented by the 'zeej' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'zeej' library on application startup.
        init {
            System.loadLibrary("zeej")
        }
    }

    private fun initGsy(){
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }*/
        //LeakCanary.install(this);

        //GSYVideoType.enableMediaCodec();
        //GSYVideoType.enableMediaCodecTexture();

        //PlayerFactory.setPlayManager(Exo2PlayerManager.class);//EXO模式
        //ExoSourceManager.setSkipSSLChain(true);


        //PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式
        //PlayerFactory.setPlayManager(IjkPlayerManager.class);//ijk模式

        //CacheFactory.setCacheManager(ExoPlayerCacheManager.class);//exo缓存模式，支持m3u8，只支持exo
        //CacheFactory.setCacheManager(ProxyCacheManager.class);//代理缓存模式，支持所有模式，不支持m3u8等

        //GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);
        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        //GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

        //GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_CUSTOM);
        //GSYVideoType.setScreenScaleRatio(9.0f/16);

        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
        //GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);

        //IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);

        //GSYVideoType.setRenderType(GSYVideoType.SUFRACE);
        ExoSourceManager.setExoMediaSourceInterceptListener(object :
            ExoMediaSourceInterceptListener {
            override fun getMediaSource(
                dataSource: String,
                preview: Boolean,
                cacheEnable: Boolean,
                isLooping: Boolean,
                cacheDir: File
            ): MediaSource? {
                //如果返回 null，就使用默认的
                return null
            }

            /**
             * 通过自定义的 HttpDataSource ，可以设置自签证书或者忽略证书
             * demo 里的 GSYExoHttpDataSourceFactory 使用的是忽略证书
             */
            override fun getHttpDataSourceFactory(
                userAgent: String,
                listener: TransferListener?,
                connectTimeoutMillis: Int,
                readTimeoutMillis: Int,
                mapHeadData: Map<String, String>,
                allowCrossProtocolRedirects: Boolean
            ): DataSource.Factory {
                //如果返回 null，就使用默认的
                val factory = GSYExoHttpDataSourceFactory(
                    userAgent, listener,
                    connectTimeoutMillis,
                    readTimeoutMillis, allowCrossProtocolRedirects
                )
                factory.setDefaultRequestProperties(mapHeadData)
                return factory
            }
        })
    }
}