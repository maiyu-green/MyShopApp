package testsdcard.com.maiyu.shopapp.http;




import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import testsdcard.com.maiyu.shopapp.activity.CniaoApplication;

/**OkHttpClient类的封装
 * Created by maiyu on 2017/2/25.
 */

/**
 * 这个类用来辅助OKHttp
 */
public class OkHttpHelper {

    public static final int TOKEN_MISSING = 401 ; //
    public static final int TOKEN_ERROR = 402 ;
    public static final int TOKEN_EXPIRE = 403 ;


    private static OkHttpHelper mOkHttpHelperInstance;      //定义OkHttpHelper对象
    private static OkHttpClient mClientInstance;            //定义OkHttpClient对象
    private Handler mHandler;                               //定义Handler对象
    private Gson mGson;                                     //定义Gson对象

    /**
     * 单例模式，私有构造函数，构造函数里面进行一些初始化
     */
    private OkHttpHelper() {
        //初始化OkHttpClient对象
        mClientInstance = new OkHttpClient();

        //设置连接超时，读取超时，写入超时
        mClientInstance.setConnectTimeout(10, TimeUnit.SECONDS);
        mClientInstance.setReadTimeout(10, TimeUnit.SECONDS);
        mClientInstance.setWriteTimeout(30, TimeUnit.SECONDS);
        //初始化Gson对象
        mGson = new Gson();

        //初始化Handler对象：传入Looper.getMainLooper
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 初始化okHttpHelper实例
     */
    static {
        mOkHttpHelperInstance   =   new OkHttpHelper();
    }

    /**
     * 获取实例
     * @return
     */
    public static OkHttpHelper getInstance() {
        return mOkHttpHelperInstance;
    }

    /**
     * 封装一个request方法：以供get和post调用
     */
    public void request(final Request request, final BaseCallback callback) {

        //在请求之前所做的事，比如弹出对话框等
        callback.onBeforeRequest(request);
        //调用OkHttpClient的newCall(request).enqueue方法，去重写
        mClientInstance.newCall(request).enqueue(new Callback() {

            //请求失败
            @Override
            public void onFailure(Request request, IOException e) {
                //返回失败的处理
                callbackFailure(request, callback , e);
            }


            //请求成功
            @Override
            public void onResponse(Response response) throws IOException {

                //回调请求成功
                callbackResponse(callback , response);

                if (response.isSuccessful()) {
                    //返回成功回调:这里可以添加我们需要常用数据类型，如字符串
                    String resString = response.body().string();

                    if (callback.mType == String.class) {
                        //如果我们需要返回String类型
                        callbackSuccess(response, resString, callback);
                    } else {
                        //如果返回的是其他类型，则利用Gson去解析
                        try {
                            //获取Object对象o
                            Object o = mGson.fromJson(resString, callback.mType);
                            callbackSuccess(response, o, callback);
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            //callbackError(response, callback, e);
                            //请求失败的回调
                            callback.onError(response , response.code() , e);
                        }
                    }

                }
                //Token验证失败的回调方法
                else if(response.code() == TOKEN_ERROR ||response.code() == TOKEN_EXPIRE
                        ||response.code() == TOKEN_MISSING ){
                    callbackToKenError(callback , response);

                }else {
                    //返回错误
                    callbackError(response, callback, null);
                }
            }
        });
    }

    /**
     * token验证失败时的回调方法
     * @param callback
     * @param response
     */
    private void callbackToKenError(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //回调Callback的onTokenError()方法
                callback.onTokenError(response , response.code());
            }
        });

    }

    /**
     * 在主线程中执行的回调
     *
     * @param response
     * @param o
     * @param callback
     */
    private void callbackSuccess(final Response response, final Object o, final BaseCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //回到Callback的onSuccess()方法
                callback.onSuccess(response, o);
            }
        });
    }

    /**
     * 在主线程中执行的回调
     * @param response
     * @param callback
     * @param e
     */
    private void callbackError(final Response response, final BaseCallback callback, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

    /**
     * 在主线程中执行的回调
     * @param request
     * @param callback
     * @param e
     */
    private void callbackFailure(final Request request, final BaseCallback callback, final Exception e) {
        //发送一个Handler对象
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }

    /**
     * get请求方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, Map<String , Object>  param ,BaseCallback callback) {
        //get请求，不需要.post(body)---null
        Request request = buildGetRequest(url, param);
        request(request, callback);
    }
    public void get(String url ,BaseCallback callback) {
        //get请求，不需要.post(body)---null
//        Request request = buildRequest(url, null, HttpMethodType.GET);
//        request(request, callback);
         get(url , null , callback);
    }


    /**
     * post请求方法
     * @param url
     * @param params ：body组合
     * @param callback
     */
    public void post(String url, Map<String, Object> params, BaseCallback callback) {
        //根据url,body数据，和post标志发送请求得到，Request对象
        Request request = buildPostRequest(url, params);
        request(request, callback);         //request请求
    }

    /**
     * 请求成功的回调
     * @param callback
     * @param response
     */
    private void callbackResponse(final BaseCallback callback , final Response response){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //回调Callback的onResponse()方法
                callback.onResponse(response);
            }
        });
    }


    /**
     * post请求
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, Map<String, Object> params){
        return  buildRequest(url , params , HttpMethodType.POST );
    }

    /**
     * get请求
     * @param url
     * @param params
     * @return
     */
    private Request buildGetRequest(String url, Map<String, Object> params){
        return buildRequest(url , params , HttpMethodType.GET);
    }

    /**
     * 根据type：判断是get还是post请求
     * 构建请求对象
     * @param url
     * @param params
     * @param type
     * @return
     */
    private Request buildRequest(String url, Map<String, Object> params, HttpMethodType type) {
        //创建Request.Builder对象
        Request.Builder builder = new Request.Builder();
        //为builder添加url
        builder.url(url);
        if (type == HttpMethodType.GET) {
            url =   buildUrlParams(url , params);
            builder.url(url);
            builder.get();          //get请求
        } else if (type == HttpMethodType.POST) {
            //post请求需要先post(body)
            RequestBody body = builderFormData(params);
            builder.post(body);
        }
        return builder.build(); //返回builder.build
    }


    /**
     * 添加url参数
     * @param url
     * @param params
     * @return
     */
    private   String buildUrlParams(String url ,Map<String,Object> params) {

        if(params == null)
            params = new HashMap<>(1);

        //获取token
        String token = CniaoApplication.getInstance().getToken();
        //非空则添加token
        if(!TextUtils.isEmpty(token))
            params.put("token",token);


        //创建StringBuffer对象
        StringBuffer sb = new StringBuffer();
        //
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }

        return url;
    }


    /**
     * post的添加body
     * @param params
     * @return
     */
    private RequestBody builderFormData(Map<String,Object> params){


        FormEncodingBuilder builder = new FormEncodingBuilder();

        if(params !=null){



            for (Map.Entry<String,Object> entry :params.entrySet() ){

                builder.add(entry.getKey(),entry.getValue() == null?"":entry.getValue().toString());
            }

            String token = CniaoApplication.getInstance().getToken();
            if(!TextUtils.isEmpty(token))
                builder.add("token", token);
        }

        return  builder.build();

    }



    /**
     * 这个枚举用于指明是哪一种提交方式
     */
    enum HttpMethodType {
        GET,
        POST
    }

}

