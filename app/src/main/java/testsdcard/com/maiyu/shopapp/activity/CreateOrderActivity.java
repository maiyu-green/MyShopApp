package testsdcard.com.maiyu.shopapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.Pingpp;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.LayoutManager.FullyLinearLayoutManager;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.adapter.OrderAdapter;
import testsdcard.com.maiyu.shopapp.bean.Charge;
import testsdcard.com.maiyu.shopapp.bean.ShopingCart;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.msg.BaseRespMsg;
import testsdcard.com.maiyu.shopapp.msg.CreateOrderRespMsg;
import testsdcard.com.maiyu.shopapp.utils.CartProvider;
import testsdcard.com.maiyu.shopapp.utils.JSONUtil;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**订单Activity：继承自BaseActivity
 * Created by maiyu on 2017/4/4.
 */

public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    //定义url
    private static String YOUR_URL ="http://218.244.151.190/demo/charge";
    public static final String URL = YOUR_URL;

    //定义flag标志
    private static int flag = 1;

    //订单列表
    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    //recyclerView
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    //支付宝支付
    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    //微信支付
    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    //银联支付
    @ViewInject(R.id.rl_yinlian)
    private RelativeLayout mLayoutYL;


    //支付宝支付的radioButton
    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    //微信支付的radioButton
    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;

    //银联支付的radioButton
    @ViewInject(R.id.rb_yinlian)
    private RadioButton mRbYL;

    //提交订单按钮（去支付）
    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    //显示总价
    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;


    //定义CartProvider对象
    private CartProvider cartProvider;

    //定义OrderAdapter对象
    private OrderAdapter mAdapter;


    //创建OkHttpHelper实例
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    //用于显示订单数量
    private String orderNum;
    //支付方式
    private String payChannel = CHANNEL_ALIPAY;
    //总价格
    private float amount;


    //创建hashmap:存放3中支付方式
    private HashMap<String,RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);
        ViewUtils.inject(this);

        //显示数据
        showData();

        //初始化
        init();

    }


    /**
     * 初始化
     */
    private void init(){



        //初始化支付方式的hashmap集合
        channels.put(CHANNEL_ALIPAY,mRbAlipay);
        channels.put(CHANNEL_WECHAT,mRbWechat);
        channels.put(CHANNEL_UPACP,mRbYL);

        //为支付方式的布局设置监听
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutYL.setOnClickListener(this);



        //获取总价格
        amount = mAdapter.getTotalPrice();
        //设置应显示的总价格
        mTxtTotal.setText("应付款： ￥"+amount);
    }


    /**
     * 显示数据
     */
    public void showData(){

        //获取CartProvider对象
        cartProvider = new CartProvider(this);

        //初始化mAdapter
        mAdapter = new OrderAdapter(this,cartProvider.getAll());

        //创建FullyLinearLayoutManager对象
        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        //设置格式方向
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        //为recyclerView设置布局
        mRecyclerView.setLayoutManager(layoutManager);

        //为recyclerView设置适配器
        mRecyclerView.setAdapter(mAdapter);

    }


    /**
     * 支付方式监听
     * @param v
     */
    @Override
    public void onClick(View v) {

        //选择支付方式
        selectPayChannle(v.getTag().toString());
    }


    /**
     * 选择支付
     * @param paychannel
     */
    public void selectPayChannle(String paychannel){


        //遍历channels集合
        for (Map.Entry<String,RadioButton> entry:channels.entrySet()){

            //定义当前选择的支付方式
            payChannel = paychannel;
            //获取RadioButton
            RadioButton rb = entry.getValue();

            //判断是否符合当前选择的字符方式
            if(entry.getKey().equals(paychannel)){

                boolean isCheck = rb.isChecked();
                //微信，支付宝，银联支付
                if(!isCheck && entry.getKey().equals(CHANNEL_WECHAT) )
                    flag = 1;
                if(!isCheck && entry.getKey().equals(CHANNEL_ALIPAY) )
                    flag = 2;
                if(!isCheck && entry.getKey().equals(CHANNEL_UPACP) )
                    flag = 3;
                rb.setChecked(!isCheck);

            }
            else
                rb.setChecked(false);
        }


    }


    /**
     * 创建订单（去支付）
     * @param view
     */
    @OnClick(R.id.btn_createOrder)
    public void createNewOrder(View view){

        //postNewOrder();
        //查询支付方式并去支付
        queryPay();
    }


    /**
     *查询支付方式并去支付
     */
    private void queryPay(){


       // new PaymentTask().execute(new PaymentRequest(CHANNEL_ALIPAY, 2));

        switch (flag){
            //微信支付：模拟支付1
            case  1:
                amount = 1;
                new PaymentTask().execute(new PaymentRequest(CHANNEL_WECHAT, (int)amount));
                break;
            //支付宝支付：模拟支付总价2
            case  2:
                amount = 2;
                new PaymentTask().execute(new PaymentRequest(CHANNEL_ALIPAY, (int)amount));
                break;
            //银联支付：模拟支付3
            case  3:
                amount = 3;
                new PaymentTask().execute(new PaymentRequest(CHANNEL_UPACP, (int)amount));
                break;
            default:
                break;
        }

    }


    /**
     * 向服务端发送数据
     */
    private void postNewOrder(){


        //获取购物车数据
        final List<ShopingCart> carts = mAdapter.getDatas();

        //商品数据
        List<WareItem> items = new ArrayList<>(carts.size());

        //遍历购物车
        for (ShopingCart c:carts ) {

            //获取没种商品的的id,价格，
            WareItem item = new WareItem(c.getId(),c.getPrice().intValue());
            //添加到items
            items.add(item);

        }

        //转化为string
        String item_json = JSONUtil.toJSON(items);

        Map<String,Object> params = new HashMap<>(5);
        params.put("user_id",CniaoApplication.getInstance().getUser().getId()+"");
        params.put("item_json",item_json);
        params.put("pay_channel",payChannel);
        params.put("amount",(int)amount+"");
        params.put("addr_id",1+"");


        mBtnCreateOrder.setEnabled(false);

        //okHttpHelper请求
        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {



                //设置已经选中支付按钮
                mBtnCreateOrder.setEnabled(true);
                //订单数量
                orderNum = respMsg.getData().getOrderNum();
                //charge
                Charge charge = respMsg.getData().getCharge();

                //打开支付界面
                openPaymentActivity(JSONUtil.toJSON(charge));

            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);
            }
        });



    }


    /**
     *
     * @param charge
     */
    private void openPaymentActivity(String charge){

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                //获取支付结果
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);

            }
        }
    }


    /**
     * 改变订单状态监听
     * @param status
     */
    private void changeOrderStatus(final int status){

        //创建Map
        Map<String,Object> params = new HashMap<>(5);
        //存放订单数量
        params.put("order_num",orderNum);
        //存放订单状态
        params.put("status",status+"");


        //okHttpHelper.post()请求
        okHttpHelper.post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                //跳转到支付结果activity
                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });

    }


    /**
     * 跳转到支付结果Activity
     * @param status
     */
    private void toPayResultActivity(int status){

        Intent intent = new Intent(this,PayResultActivity.class);
        intent.putExtra("status",status);

        startActivity(intent);
        this.finish();

    }


    /**
     * 商品数据bean类
     */
    class WareItem {
        //商品id
        private  Long ware_id;
        //商品价格
        private  int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


    /**
     * 支付类paymentTask
     */
    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {


        /**
         * 执行前
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * 执行中
         * @param pr
         * @return
         */
        @Override
        protected String doInBackground(PaymentRequest... pr) {

            //获取第一个参数
            PaymentRequest paymentRequest = pr[0];
            //
            String data = null;
            //
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if(null == data){
                showMsg("请求出错", "请检查URL", "URL无法获取charge");
                return;
            }
            Log.d("charge", data);
           //Pingpp.createPayment(CreateOrderActivity.this, data);
            //QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
            //建议填写规则:qwallet + APP_ID
            Pingpp.createPayment(CreateOrderActivity.this, data, "qwalletXXXXXXX");
        }

    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */

    public void showMsg(String title, String msg1, String msg2) {

        //
        String str = title;
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        //创建AlertDialog窗口来显示
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrderActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }


    /**
     * postJson请求去支付
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private static String postJson(String url, String json) throws IOException {
        //创建MediaType
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        //创建RequestBody
        RequestBody body = RequestBody.create(type, json);
        //创建request请求
        Request request = new Request.Builder().url(url).post(body).build();

        //OkHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //调用okHttpClient的newCall(Request).execute()方法来获取Response对象
        Response response = client.newCall(request).execute();

        //返回请求结果
        return response.body().string();
    }


    /**
     * 创建支付请求类
     */
    class PaymentRequest {
        //定义支付方式
        String channel;
        //支付价格
        int amount;

        public PaymentRequest(String channel, int amount) {
            this.channel = channel;
            this.amount = amount;
        }
    }




}


