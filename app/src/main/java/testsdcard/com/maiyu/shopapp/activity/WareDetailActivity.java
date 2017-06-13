package testsdcard.com.maiyu.shopapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import dmax.dialog.SpotsDialog;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Favorites;
import testsdcard.com.maiyu.shopapp.bean.User;
import testsdcard.com.maiyu.shopapp.bean.Wares;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.utils.CartProvider;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**
 * Created by maiyu on 2017/3/19.
 *
 * 这里Web端的调用网址：http://112.124.22.238:8081/course_api/wares/detail.html
 * 可以去查看源代码
 */

public class WareDetailActivity extends AppCompatActivity implements View.OnClickListener {



    //toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar ;

    //定义webView
    @ViewInject(R.id.webview_ware)
    private WebView mWebView ;

    //自定义interface
    private AppInterface    mAppInterface ;         //自定义
    //商品对象Wares
    private Wares   mWare ;

    //窗口对象
    private SpotsDialog mDialog ;
    //CartProvider对象
    private CartProvider    mCartProvider ;
    //OkHtppHelper实例
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ware_detail);

        ViewUtils.inject(this);         //注解

        ShareSDK.initSDK(this);         //必须出示分享，结束时需停止，可在onDestroy()中实现


        //获取intent,并取得传入的数据
        Intent  intent  =   getIntent();
        //获取商品数据
        Serializable serial = intent.getSerializableExtra(Contants.WARE);
        if(serial == null){
            this.finish();      //若数据为空，则结束当前Activity
        }

        //创建加载网络时的窗口对象
        mDialog =   new SpotsDialog(this , ".....");
        mDialog.show();

        //取得当前Wares对象
        mWare   =   (Wares)serial;

        //初始化CartProvider对象
        mCartProvider   =   new CartProvider(this);


        intiToobar();       //初始化toolbar
        initWebView();      //初始化webView



    }


    /**
     *  初始化Toolbar
     */

    private void intiToobar() {
        //显示toolbar状态
        mToolbar.setNavigationOnClickListener(this);

        mToolbar.setRightButtonText("分享");
        mToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //分享功能的实现
                showShare();
            }
        });
    }


    /**
     * 分享功能
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("分享给...");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        //oks.setTitleUrl("http://sharesdk.cn");
        oks.setTitleUrl("http://blog.csdn.net/qq_28055429?viewmode=contents");

        // text是分享文本，所有平台都需要这个字段
        oks.setText(mWare.getName());
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(mWare.getImgUrl());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://blog.csdn.net/qq_28055429?viewmode=contents");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mWare.getName());
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://blog.csdn.net/qq_28055429?viewmode=contents");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    protected void onDestroy() {

        ShareSDK.stopSDK(this);     //因为有ShareSDK.initSDK(this);所以结束时必须ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    /**
     * 初始化WebView
     */
    private void initWebView(){

        //为webView加载HTML5页面
        mWebView.loadUrl(Contants.API.WARES_DETAIL);

        //(1)获取当前webView的WebSettings对象，
        WebSettings webSettings =   mWebView.getSettings();
        //设置WebSettings支持js
        webSettings.setJavaScriptEnabled(true);
        //设置不阻塞网络图片
        webSettings.setBlockNetworkImage(false);
        //设置使用缓存
        webSettings.setAppCacheEnabled(true);

        //初始化AppInterface对象
        mAppInterface    =   new AppInterface(this);

        //添加JS接口，注意接口名：appInterface必须与HTML5中调用的接口名一致
        mWebView.addJavascriptInterface(mAppInterface , "appInterface");
        //设置WebViewClient链接
        mWebView.setWebViewClient(new MyWebViewClient());

    }

    @Override
    public void onClick(View v) {
        this.finish();
    }


    /**
     * 定义类AppInterface,
     * 创建相关方法，用于与HTML5页面之间的交互；
     * 即Native与HTML5相关方法的互相调用
     * 注意方法名相对应：这里采用HTML5地址为：
     * http://112.124.22.238:8081/course_api/wares/detail.html
     */
    class  AppInterface{

        private Context mContext ;      //定义上下文对象

        //初始化
        public AppInterface(Context context){
            this.mContext   =   context ;
        }

        //HTML5页面触发购买方法时调用
        @JavascriptInterface
        public void buy(long id){
            mCartProvider.put(mWare);
            ToastUtils.show(mContext , "已经添加到购物车");

        }

        //收藏商品
        @JavascriptInterface
        public void addFavorites(long id){
            addToFavorite();
        }


        //加到购物车中
        @JavascriptInterface
        public void addToCart(long id){

            mCartProvider.put(mWare);
            ToastUtils.show(mContext , "已经添加到购物车");

        }

        //展示当前商品数据：记得只能在主线程中更新UI
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:showDetail("+ mWare.getId() + ")");
                }
            });

        }

    }

    /**
     * 收藏商品
     */
    private void addToFavorite() {

        //获取用户：判断用户是否已经登录
        User user = CniaoApplication.getInstance().getUser();
        if(user == null){
            startActivity(new Intent(this , LoginActivity.class));
        }

        //获取用户id
        Long userId = CniaoApplication.getInstance().getUser().getId();

        Map<String , Object> params = new HashMap<>();
        params.put("user_id" , userId);
        params.put("ware_id" , mWare.getId());


        //post请求
        mHttpHelper.post(Contants.API.FAVORITE_CREATE, params, new SpotsCallBack<List<Favorites>>(this) {


            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                ToastUtils.show(WareDetailActivity.this , "已经添加到收藏夹了");
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                Log.d("code: " , "" +  code);
            }
        });


    }


    /**
     * 创建类MyWebViewClient继承自WebViewClient
     * 重写与WebView连接时的相应触发的方法
     */
    class MyWebViewClient extends WebViewClient{


        //页面有链接点击时调用
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //自定义功能
            if(url.startsWith("nan://....")){
                //自定义功能，如打电话等
            }

            return super.shouldOverrideUrlLoading(view, url);
        }



        //页面加载完毕时调用（因为JS有些方法需要在页面加载完毕时才能调用，不然没有效果）
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //加载完毕时先关掉窗口对象
            if(mDialog != null && mDialog.isShowing()){
                mDialog.dismiss();
            }

            mAppInterface.showDetail();
        }
    }





}
