package testsdcard.com.maiyu.shopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.adapter.MyLocationAdapter;
import testsdcard.com.maiyu.shopapp.bean.MyLocation;
import testsdcard.com.maiyu.shopapp.decoration.DividerItemDecoration;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.msg.BaseRespMsg;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**我的收货地址类
 * Created by maiyu on 2017/4/10.
 */


public class AddMyLocationActivity extends BaseActivity {

    //定义CnToolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar cnToolbar;

    //定义RecyclerView
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    //创建OkHttpHelper实例
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    //定义MyLocationAdapter
    private MyLocationAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);

        //ViewUtils注解
        ViewUtils.inject(this);

        //初始化toolbars
        initToolbars();
        //初始化地址
        initAddress();
    }

    /**
     * 初始化CnToolbar：
     * 为CnToolbar设置监听
     */
    private void initToolbars() {
        //设置取消按钮监听：即左边图片按钮
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置添加按钮的监听
        cnToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddNewLocatioin();
            }
        });
    }

    /**
     * 添加新住址
     */
    private void toAddNewLocatioin() {

        //跳转到CreateNewLocationActivity
        Intent intent = new Intent(AddMyLocationActivity.this , CreateNewLocationActivity.class);
        startActivityForResult(intent , Contants.REQUEST_CODE);

    }


    /**
     * 从CreateNewLocationActivity
     * 返回来时的回调方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        initAddress();
    }

    /**
     * 显示收货地址
     */
    private void initAddress() {

        //创建Map集合
        Map<String , Object> params = new HashMap<>(1);
        //存放用户ID:user_id
        params.put("user_id" , CniaoApplication.getInstance().getUser().getId());

        //请求数据
        mHttpHelper.get(Contants.API.ADDRESS_LIST, params, new SpotsCallBack<List<MyLocation>>(this) {

            //请求成功回调此方法
            @Override
            public void onSuccess(Response response, List<MyLocation> myLocations) {

                //显示收货地址
                showLocation(myLocations);

            }

            //错误回调
            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 显示收货地址
     */
    private void showLocation(List<MyLocation> myLocations) {

        //地址排序
        Collections.sort(myLocations);


        //判断mAdapter是否为空
        if(mAdapter == null){
            //初始化MyLocationAdapter
            mAdapter = new MyLocationAdapter(this, myLocations, new MyLocationAdapter.MyLocationListener() {
                @Override
                public void setDefault(MyLocation location) {
                    //更新收货地址
                    updateMyLocation(location);
                }
            });
            //为mRecyclerView设置适配器和相关属性
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(AddMyLocationActivity.this));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        }else {

            //若mAdapter不为空则刷新收货地址
            mAdapter.refreshData(myLocations);
            //设置mRecyclerViedw的adapter
            mRecyclerView.setAdapter(mAdapter);
        }

    }


    /**
     * 更新收货地址
     * @param location
     */
    private void updateMyLocation(MyLocation location) {

        //创建Map集合
        Map<String,Object> params = new HashMap<>(1);
        //存入收货id
        params.put("id",location.getId());
        //存放收货人
        params.put("consignee",location.getConsignee());
        //存放收货人电话
        params.put("phone",location.getPhone());
        //存放收货地址
        params.put("addr",location.getAddr());
        //存放邮政编码
        params.put("zip_code",location.getZipCode());
        //存放是否设置为默认
        params.put("is_default",location.getDefault());

        //post方式
        mHttpHelper.post(Contants.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if(baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS){

                    initAddress();//重新初始化
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }


}
