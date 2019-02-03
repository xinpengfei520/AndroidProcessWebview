package com.xpf.process_webview_library

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Created by x-sir on 2019/2/2 :)
 * Function:
 */
class JavaScriptService : Service() {

    companion object {
        val TAG = JavaScriptService::class.java.simpleName
    }

    private val mBinder = object : IJavaScriptlInterface.Stub() {
        override fun handler(data: String?, callBack: IJavaScriptCallBack?) {
            if (data != null) {
                // jsBridge 分发给不同业务
                JavaScriptInterface.filterData(data, callBack)
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }
}