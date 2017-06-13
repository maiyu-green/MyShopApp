package testsdcard.com.maiyu.shopapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.adapter.BaseAdapter;
import testsdcard.com.maiyu.shopapp.adapter.FavoriteAdapter;
import testsdcard.com.maiyu.shopapp.bean.Favorites;
import testsdcard.com.maiyu.shopapp.decoration.CardViewtemDecortion;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**我的收藏Activity
 * Created by maiyu on 2017/6/12.
 */

public class MyFavoriteActivity extends Activity {

    //定义toolbar
    @ViewInject(R.id.toolbar)
    private CnToolbar mToolbar;

    //定义recyclerView
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    //定义适配器
    private FavoriteAdapter mAdapter;

    //创建OkHttpHelper实例
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

        //初始化注解
        ViewUtils.inject(this);

        //初始化toolbar
        initToolBars();
        //获取收藏商品
        getFavorites();
    }


    /**
     * 初始化toolbar
     */
    private void initToolBars() {
        //设置左边按钮监听
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 获取收藏商品
     */
    private void getFavorites() {

        //获取用户id
        Long userId = CniaoApplication.getInstance().getUser().getId();
        Map<String , Object> params = new HashMap<>();
        params.put("user_id", userId);

        //get请求
        okHttpHelper.get(Contants.API.FAVORITE_LIST, params, new SpotsCallBack<List<Favorites>>(this) {


            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                //展示商品数据
                showFavorites(favorites);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 展示商品数据
     * @param favorites
     */
    private void showFavorites(List<Favorites> favorites) {

        //初始化收藏adapter
        mAdapter = new FavoriteAdapter(this , favorites);

        //为recyclerView设置adapter
        mRecyclerView.setAdapter(mAdapter);
        //为recyclerView设置布局
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //为recyclerView设置间距
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());

        //为adapter的每一项设置监听
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }
        });
    }
}
