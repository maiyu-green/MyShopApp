package testsdcard.com.maiyu.shopapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.adapter.BaseAdapter;
import testsdcard.com.maiyu.shopapp.adapter.MyOrderAdapter;
import testsdcard.com.maiyu.shopapp.bean.Order;
import testsdcard.com.maiyu.shopapp.decoration.CardViewtemDecortion;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;


/**
 * 我的订单activity
 */
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {



    public static final int STATUS_ALL=1000;
    public static final int STATUS_SUCCESS=1; //支付成功的订单
    public static final int STATUS_PAY_FAIL=-2; //支付失败的订单
    public static final int STATUS_PAY_WAIT=0; //：待支付的订单
    private int status = STATUS_ALL;


    //toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;


    //tabLayout
    @ViewInject(R.id.tab_layout)
    private TabLayout mTablayout;


    //recyclerView
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview;


    //myOrderAdapter
    private MyOrderAdapter mAdapter;



    //创建OkHttpHelper实例
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ViewUtils.inject(this);


        //初始化
        initToolBar();
        initTab();



        //获取订单
        getOrders();
    }


    /**
     * 初始化toolbar
     */
    private void initToolBar(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化tab
     */
    private void initTab(){


        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        mTablayout.addTab(tab);


        tab= mTablayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);

    }


    /**
     * 获取订单
     */
    private void getOrders(){


        //获取用户id
        Long userId = CniaoApplication.getInstance().getUser().getId();

        Map<String, Object> params = new HashMap<>();

        params.put("user_id",userId);
        params.put("status",status);


        okHttpHelper.get(Contants.API.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                //显示订单
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

                LogUtils.d("code:"+code);
            }
        });
    }


    /**
     * 显示订单
     * @param orders
     */
    private void showOrders(List<Order> orders){

        if(mAdapter ==null) {
            mAdapter = new MyOrderAdapter(this,orders);
            mRecyclerview.setAdapter(mAdapter);
            mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview.addItemDecoration(new CardViewtemDecortion());

            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    //跳转到DetailActivity
                    toDetailActivity(position);
                }


            });
        }
        else{
            mAdapter.refreshData(orders);
            mRecyclerview.setAdapter(mAdapter);
        }
    }


    /**
     * 跳转到OrderDetailActivity
     * @param position
     */
    private void toDetailActivity(int position){

        Intent intent = new Intent(this,OrderDetailActivity.class);

        Order order = mAdapter.getItem(position);
        intent.putExtra("order",order);
        startActivity(intent,true);
    }



    @Override
    public void onTabSelected(TabLayout.Tab tab) {


        status = (int) tab.getTag();
        getOrders();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

