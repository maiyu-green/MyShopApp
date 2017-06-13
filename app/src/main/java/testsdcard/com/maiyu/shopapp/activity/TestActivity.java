package testsdcard.com.maiyu.shopapp.activity;


import android.os.Bundle;
import testsdcard.com.maiyu.shopapp.R;
import java.io.IOException;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 *
 * ping++ sdk 示例程序，仅供开发者参考。
 * 【说明文档】https://github.com/PingPlusPlus/pingpp-android/blob/master/docs/ping%2B%2B安卓SDK使用文档.md
 *
 * 【注意】运行该示例，需要用户填写一个YOUR_URL。
 *
 * ping++ sdk 使用流程如下：
 * 1）客户端已经有订单号、订单金额、支付渠道
 * 2）客户端请求服务端获得charge。服务端生成charge的方式参考ping++ 官方文档，地址 https://pingxx.com/guidance/server/import
 * 3）收到服务端的charge，调用ping++ sdk 。
 * 4）onActivityResult 中获得支付结构。
 * 5）如果支付成功。服务端会收到ping++ 异步通知，支付成功依据服务端异步通知为准。
 */
public class TestActivity extends Activity {



}