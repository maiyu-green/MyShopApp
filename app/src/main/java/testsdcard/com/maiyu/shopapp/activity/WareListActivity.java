package testsdcard.com.maiyu.shopapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

import testsdcard.com.maiyu.shopapp.LayoutManager.NpaGridLayoutManager;
import testsdcard.com.maiyu.shopapp.LayoutManager.NpaLinearLayoutManager;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.adapter.BaseAdapter;
import testsdcard.com.maiyu.shopapp.adapter.HWAdapter;
import testsdcard.com.maiyu.shopapp.bean.Page;
import testsdcard.com.maiyu.shopapp.bean.Wares;
import testsdcard.com.maiyu.shopapp.decoration.DividerItemDecoration;
import testsdcard.com.maiyu.shopapp.utils.Pager;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**商品列表Activity:当在homeFragment选中时会跳转到此Activity
 * Created by maiyu on 2017/3/13.
 */

public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares> ,
    TabLayout.OnTabSelectedListener ,View.OnClickListener{

    public static final int TAG_DEFAULT=0;          //默认排序：0
    public static final int TAG_SALE=1;             //销量排序：1
    public static final int TAG_PRICE=2;            //价格排序：2

    public static final int ACTION_LIST=1;          //list显示商品：1（即一行放1个商品）
    public static final int ACTION_GIRD=2;          //网格显示商品：2（即一行可放多个商品）

    //使用注解模式

    //tab_layout
    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.toolbar)
    private CnToolbar cnToolbar;

   @ViewInject(R.id.txt_summary)
    private TextView mTxtSumary ;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecycler_wares;




    private int orderBy = 0 ;               //请求参数
    private long campaignId = 0 ;           //请求参数
    private HWAdapter mWaresAdapter ;       //HWAdapter适配器
    private Pager pager ;                   //Pager对象



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warelist);     //设置布局
        ViewUtils.inject(this);                         //注解

       initToolBar();       //初始化toolBar

        //获取参数
       campaignId  =   getIntent().getLongExtra(Contants.COMPAINGAIN_ID , 0);

        initTab();      //初始化Tab
        getData();      //获取数据

    }


    /**
     * 初始化ToolBar
     */
    private void initToolBar(){
        //为CnToolbar的返回键设置监听：finish this activity
        cnToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });

        //设置CnToolBar的右边按钮
        cnToolbar.setRightButtonIcon(R.mipmap.icon_grid_32);
        cnToolbar.getRightButton().setTag(ACTION_LIST);

         cnToolbar.setRightButtonOnClickListener(this);
    }

    /**
     * 初始化Tab
     */
    private void initTab(){
        //创建TabLayout.Tab,并添加到TabLayout中，
        //这里创建3个Tab

        TabLayout.Tab   tab =   mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);
        mTabLayout.addTab(tab);

        tab =   mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);
        mTabLayout.addTab(tab);

        tab =   mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);
        mTabLayout.addTab(tab);

        //为TabLayout设置监听
        mTabLayout.setOnTabSelectedListener(this);

    }

    /**
     * 获取数据
     */
    private void getData() {

        //请求数据，返回Pager对象
        pager   =   Pager.newBuilder()
                //设置url
                .setUrl(Contants.API.WARES_CAMPAIGN_LIST)
                //设置campaignId
                .putParam("campaignId" , campaignId)
                //设置参数
                .putParam("orderBy" , orderBy)
                //设置刷新布局
                .setRefreshLayout(mRefreshLayout)
                //设置加载更多
                .setLoadMore(true)
                //设置页面监听
                .setOnPageListener(this)
                //builder
                .builder(this , new TypeToken<Page<Wares>>(){}.getType());

        pager.request();

    }


    /**
     * 加载数据
     * @param datas
     * @param totalPage
     * @param totalCount
     */
    @Override
    public void load(List<Wares> datas, int totalPage, int totalCount) {
        //设置文本
        mTxtSumary.setText("共有" + totalCount + "件商品");


        if(mWaresAdapter == null){
            //初始化适配器
            mWaresAdapter   =   new HWAdapter(this , datas);

            mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void OnClick(View view, int position) {
                    //得到当前Wares对象
                    Wares ware = mWaresAdapter.getItem(position);
                    //创建intent
                    Intent intent = new Intent(WareListActivity.this, WareDetailActivity.class);
                    //这里存放Serializable对象，Wares继承自Serializable
                    intent.putExtra(Contants.WARE , ware);
                    //启动intent
                    startActivity(intent);
                }
            });


            //为mRecycler_wares设置适配器，并设置相应布局
            mRecycler_wares.setAdapter(mWaresAdapter);
            //mRecycler_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecycler_wares.setLayoutManager(new NpaLinearLayoutManager(getApplicationContext()));
            //设置itemDecoration
            mRecycler_wares.addItemDecoration(new DividerItemDecoration(
                    this , DividerItemDecoration.VERTICAL_LIST));
            //设置item动画
            mRecycler_wares.setItemAnimator(new DefaultItemAnimator());
        }else {
            mWaresAdapter.refreshData(datas);
        }
    }

    /**
     * 加载更多
     * @param datas
     * @param totalPage
     * @param totalCount
     */
    @Override
    public void loadMore(List<Wares> datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    /**
     * 向上刷新
     * @param datas
     * @param totalPage
     * @param totalCount
     */
    @Override
    public void refresh(List<Wares> datas, int totalPage, int totalCount) {
        //为adapter刷新数据
        mWaresAdapter.refreshData(datas);
        //recyclerView上滑到上面
        mRecycler_wares.scrollToPosition(0);
    }

    /**
     * Tab的点击事件
     * @param tab
     */
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        //重新请求数据
        orderBy =   (int)tab.getTag();
        pager.putParam("orderBy" , orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * Toolbar的右边按钮选则布局监听
     * @param v
     */
    @Override
    public void onClick(View v) {

        int action = (int)v.getTag();

        //List来显示布局
        if(action == ACTION_LIST){

            //设置cnToolbar的相关属性
            cnToolbar.setRightButtonIcon(R.drawable.icon_list_32);
            cnToolbar.getRightButton().setTag(ACTION_GIRD);

            //为商品的adapter刷新布局
            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
            //mRecycler_wares.setLayoutManager(new GridLayoutManager(this ,3));
            mRecycler_wares.setLayoutManager(new NpaGridLayoutManager(getApplicationContext() ,2));
        }else if(action == ACTION_GIRD){    //网格显示商品

            cnToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
            cnToolbar.getRightButton().setTag(ACTION_LIST);

            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
            //mRecycler_wares.setLayoutManager(new LinearLayoutManager(this ));
           mRecycler_wares.setLayoutManager(new NpaLinearLayoutManager(getApplicationContext()));
        }
    }







}
