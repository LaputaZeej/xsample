package com.laputa.zeej.compose

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.laputa.zeej.flow.LocationActivity
import com.laputa.zeej.ui.theme.XsampleTheme

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XsampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }

    companion object{
        fun ship(activity: Activity) {
            activity.startActivity(Intent(activity, ComposeActivity::class.java))
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Preview(name = "demo")
@Composable
fun DefaultPreview() {
    XsampleTheme {
        Greeting("Android")
    }
}