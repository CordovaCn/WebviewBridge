package com.vrseen.webviewbridge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import jsbridge.core.JsCallback;

/**
 * Created by zhengxiaoyong on 16/4/19.
 */
public class BridgePlug  {

    public static void showToast(WebView webView, JSONObject data, JsCallback callback) {
        Toast.makeText(webView.getContext(), data.toString(), Toast.LENGTH_SHORT).show();
        JSONObject result = new JSONObject();
        try {
            result.put("result", "appName");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, "1234");
    }

    public static void getIMSI(final WebView webView, JSONObject data, final JsCallback callback) {
        TelephonyManager telephonyManager = ((TelephonyManager) webView.getContext().getSystemService(Context.TELEPHONY_SERVICE));
        String imsi = telephonyManager.getSubscriberId();
        if (TextUtils.isEmpty(imsi)) {
            imsi = telephonyManager.getDeviceId();
        }
        JSONObject result = new JSONObject();
        try {
            result.put("imsi", imsi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, "123");
    }

    public static void getAppName(final WebView webView, JSONObject data, final JsCallback callback) {
        String appName;
        try {
            PackageManager packageManager = webView.getContext().getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(webView.getContext().getApplicationContext().getPackageName(), 0);
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
        } catch (Exception e) {
            e.printStackTrace();
            appName = "";
        }
        JSONObject result = new JSONObject();
        try {
            result.put("result", appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, null);
    }

    public static void  jumpActivity(final WebView webView, JSONObject data, final JsCallback callback) {

        try {
            Activity activity = (Activity) webView.getContext();
            Intent intent = new Intent(activity,LoginActivity.class);
            intent.putExtra("ifAppViewCreate", "PX002");
            // 打开新的Activity
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, null, null);
    }

    public static void getOsSdk(WebView webView, JSONObject data, JsCallback callback) {
        JSONObject result = new JSONObject();
        try {
            result.put("os_sdk", Build.VERSION.SDK_INT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsCallback.invokeJsCallback(callback, true, result, null);
    }

    public static void finish(WebView webView, JSONObject data, JsCallback callback) {
        if (webView.getContext() instanceof Activity) {
            ((Activity) webView.getContext()).finish();
        }
    }

    public static void delayExecuteTask(WebView webView, JSONObject data, final JsCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                JSONObject result = new JSONObject();
                try {
                    result.put("result", "延迟3s执行native方法");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsCallback.invokeJsCallback(callback, true, result, null);
            }
        }, 3000);
    }


}