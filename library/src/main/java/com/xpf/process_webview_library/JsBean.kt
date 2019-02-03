package com.xpf.process_webview_library

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity

/**
 * Created by x-sir on 2019/2/2 :)
 * Function:
 */
data class JsBean(val functionName: String, val url: String?, val result: (context: Context?, data: String, data2: String, callBack: IJavaScriptCallBack?) -> String?)

/**
 * object 声明的类中的所有方法都是静态方法
 */
@SuppressLint("StaticFieldLeak")
object JavaScriptInterface {

    val set: MutableSet<JsBean> = HashSet()
    // jsbridge 接收的 scheme 名
    val SCHEME_NAME = "eating"
    var context: Context? = null

    fun register(bean: JsBean) {
        set.add(bean)
    }

    fun unregister(bean: JsBean) {
        set.remove(bean)
    }

    /**
     * WebActivity 跨进程回调触发
     */
    fun filterData(data: String, callBack: IJavaScriptCallBack?) {
        val uri = Uri.parse(data)
        if (uri.scheme == SCHEME_NAME && set.size > 0) {
            set.filter { uri.authority == it.functionName }
                    // 这里获取到最顶层的 activity，因为 aidl 无法传递 context 对象
                    .mapNotNull { it.result(ActivityManager.current(), uri.pathSegments[0], "", callBack) }
        }
    }

    /**
     * WebActivity 跨进程专用回调
     */
    fun inject(activity: AppCompatActivity, url: String, webView: NestedScrollWebView, jsbController: IJavaScriptlInterface?, callBack: IJavaScriptCallBack?) {
        // web 进程内调用
        webView.registerHandler("sendAction") { data, function ->
            val uri = Uri.parse(data)
            if (uri.scheme == SCHEME_NAME && set.size > 0) {
                set.filter { uri.authority == it.functionName }
                        .mapNotNull { it.result(activity, uri.pathSegments[0], "", callBack) }
                        .forEach { function.onCallBack(it) }
            }
            // aidl 分发到不同业务
            jsbController?.handler(data, callBack)
        }
    }
}