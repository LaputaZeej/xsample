package com.laputa.zeej.std_0005_architecture

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import com.laputa.zeej.R

// mvc
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initView()
    }

    private fun initView() {
        findViewById<View>(R.id.action_load).setOnClickListener {
            doAction("kotlin")
        }
    }

    abstract fun doAction(userId: String)

    protected fun showLoading(show: Boolean, msg: String = "") {
        findViewById<TextView>(R.id.action_load).let {
            if (show) {
                it.text = msg.ifEmpty { "查询中" }
                //it.isEnabled = false
            } else {
                it.text = "查询"
                //it.isEnabled = true
            }
        }
    }

    protected fun showUserInfo(msg: String) {
        findViewById<TextView>(R.id.tv_user_info)?.text = msg
    }

    protected fun showBookInfo(msg: String) {
        findViewById<TextView>(R.id.tv_book_info)?.text = msg
    }

    protected fun showAddressInfo(msg: String) {
        findViewById<TextView>(R.id.tv_address_info)?.text = msg
    }
}

inline fun <reified T> Activity.skip(bundle: Bundle = bundleOf()) {
    this.startActivity(Intent(this, T::class.java).apply {
        putExtras(bundle)
    })
}