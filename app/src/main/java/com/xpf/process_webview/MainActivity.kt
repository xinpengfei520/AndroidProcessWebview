package com.xpf.process_webview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.xpf.process_webview_library.WebActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var url: String = "http://www.taobao.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                WebActivity.actionStart(this@MainActivity, url)
            }
        })
    }
}
