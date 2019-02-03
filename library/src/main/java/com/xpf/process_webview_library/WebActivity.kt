package com.xpf.process_webview_library

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.safframework.log.L

/**
 * Created by x-sir on 2019/2/2 :)
 * Function:
 */
class WebActivity : AppCompatActivity() {

    private var isServiceConnect: Boolean? = false
    private var jsbController: IJavaScriptlInterface? = null
    private lateinit var mWebView: NestedScrollWebView
    private var callback: IJavaScriptCallBack? = null

    private val serviceConn = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
            L.e(TAG, "onServiceDisconnected()")
            isServiceConnect = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            L.i(TAG, "onServiceConnected()")
            // 获取 aidl
            jsbController = IJavaScriptlInterface.Stub.asInterface(service)
            val url = intent.getStringExtra(ARouterConstants.PARAM.WEB_URL) ?: return
            // 添加 js 框架
            JavaScriptInterface.inject(this@WebActivity, url, mWebView, jsbController, callback)
            isServiceConnect = true
        }
    }

    /**
     * 绑定启动远程的 Service
     */
    private fun bindService() {
        val intent = Intent(this, JavaScriptService::class.java)
        bindService(intent, serviceConn, Context.BIND_AUTO_CREATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var extras: Bundle? = null
        try {
            extras = intent.extras
        } catch (e: Exception) {
            finish()
            return
        }

        if (extras == null) {
            finish()
            return
        }

        var url: String? = null
        try {
            url = extras.getString(Companion.HTTP_URL)
        } catch (e: Exception) {
            finish()
            return
        }

        if (TextUtils.isEmpty(url)) {
            throw NullPointerException("url is empty!")
        }

        L.i(TAG, "url===$url")

        super.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val layout = LinearLayout(applicationContext)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        layout.orientation = LinearLayout.VERTICAL

        setContentView(layout, params)

        // 表示 actionBar 不为 null 的条件下，才会去执行 let 函数体
        actionBar?.let {
            // 在函数体内使用 it 替代对象去访问其公有的属性和方法
            it.hide()
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayShowHomeEnabled(false)
        }

        bindService()

        mWebView = NestedScrollWebView(applicationContext)
        params.weight = 1f
        mWebView.setVisibility(View.VISIBLE)
        layout.addView(mWebView, params)

        mWebView.setVerticalScrollbarOverlay(true)
        mWebView.setWebViewClient(WebViewClient())
        mWebView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
        } else {
            finish()
        }
    }

    override fun finish() {
        super.finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    companion object {

        // 定义字符串常量，const 只能修饰 val，不能修饰 var
        private const val HTTP_URL: String = "url"
        val TAG = WebActivity::class.java.simpleName

        fun actionStart(context: Context, url: String) {
            val intent = Intent(context, WebActivity::class.java)
            val bundle = Bundle()
            bundle.putString(HTTP_URL, url)
            intent.putExtras(bundle)
            startActivity(context, intent, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mWebView.removeAllViews()
        try {
            mWebView.destroy()
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}