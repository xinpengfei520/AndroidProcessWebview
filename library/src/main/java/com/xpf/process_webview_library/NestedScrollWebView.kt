package com.xpf.process_webview_library

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView

import com.github.lzyzsd.jsbridge.BridgeWebView

/**
 * Created by x-sir on 2019/2/2 :)
 * Function:
 */
class NestedScrollWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                    defStyle: Int = 0) : BridgeWebView(context, attrs, defStyle) {

    init {
        initData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initData() {
        val settings = settings
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        settings.javaScriptEnabled = true
        settings.savePassword = false
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = false
        settings.domStorageEnabled = true

        // 启用 WebView 调试模式,注意，请勿在实际 App 中打开！
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
    }
}
