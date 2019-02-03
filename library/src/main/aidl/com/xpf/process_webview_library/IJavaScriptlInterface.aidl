// IJavaScriptlInterface.aidl
package com.xpf.process_webview_library;

import com.xpf.process_webview_library.IJavaScriptCallBack;

// Declare any non-default types here with import statements

// javaScript 的分发到业务 controller 的实现
interface IJavaScriptlInterface {

    void handler(String data,IJavaScriptCallBack callback);
}
