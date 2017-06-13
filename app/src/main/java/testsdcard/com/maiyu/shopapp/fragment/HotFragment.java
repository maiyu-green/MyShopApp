package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.WareDetailActivity;
import testsdcard.com.maiyu.shopapp.adapter.BaseAdapter;
import testsdcard.com.maiyu.shopapp.adapter.HWAdapter;
import testsdcard.com.maiyu.shopapp.bean.Page;
import testsdcard.com.maiyu.shopapp.bean.Wares;
import testsdcard.com.maiyu.shopapp.decoration.DividerItemDecoration;
import testsdcard.com.maiyu.shopapp.utils.Pager;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**热卖fragment
 * Created by maiyu on 2017/2/23.
 */
public class HotFragment extends Fragment implements Pager.OnPageListener<Wares>{


    private static final String TAG = "HotFragment" ;

    private MaterialRefreshLayout mRefreshLayout;       //定义MaterialRefreshLayout
    private RecyclerView mRecyclerView ;                //定义RecyclerView
    private HWAdapter hwAdapter ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           //获取布局
            View   view =   inflater.inflate(R.layout.fragment_hot , container , false);

            initView(view);     //初始化视图
           // init();

        Pager pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_HOT)
                .setLoadMore(true)
                .setOnPageListener(this)
                .setPageSize(20)
                .setRefreshLayout(mRefreshLayout)
                .builder(getContext() , new TypeToken<Page<Wares>>(){}.getType());
        pager.request();
            return view ;
    }


    /*
    初始化视图，
     */
    private void initView(View view) {
        mRefreshLayout   =   (MaterialRefreshLayout)view.findViewById(R.id.refresh);
        mRecyclerView    =   (RecyclerView)view.findViewById(R.id.recycleView);
    }


//    private void init(){
//
//    }

    @Override
    public void load(final List<Wares> datas, int totalPage, int totalCount) {
        hwAdapter    =   new HWAdapter(getContext() ,datas);


        //为当前adapter添加监听器，跳转到WareDetailActivity(HTML5页面)
        hwAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                //得到当前Wares对象
                Wares ware = hwAdapter.getItem(position);
                //创建intent
                Intent intent = new Intent(getActivity() , WareDetailActivity.class);
                //这里存放Serializable对象，Wares继承自Serializable
                intent.putExtra(Contants.WARE , ware);
                //启动intent
                startActivity(intent);
            }
        });


        mRecyclerView.setAdapter(hwAdapter);
        //为RecyclerView设置布局管理器，动画，子项间隔
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerView.addItemDecoration(new DividerItemDecoration());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        //得到当前页面最下面位置
        int position = hwAdapter.getDatas().size();
        //从position位置开始加载数据
        hwAdapter.addData(position , datas);

        //为view设置当前滑动位置:为新加载完毕后的最下面
        mRecyclerView.scrollToPosition(hwAdapter.getDatas().size());
    }

    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        //先清理数据
        hwAdapter.clearData();
        //然后重写加载数据
        hwAdapter.addData(datas);

        //设置位置
        mRecyclerView.scrollToPosition(0);
    }
}






