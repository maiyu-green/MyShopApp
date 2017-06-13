package testsdcard.com.maiyu.shopapp.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Tab;
import testsdcard.com.maiyu.shopapp.fragment.CartFragment;
import testsdcard.com.maiyu.shopapp.fragment.CategoryFragment;
import testsdcard.com.maiyu.shopapp.fragment.HomeFragment;
import testsdcard.com.maiyu.shopapp.fragment.HotFragment;
import testsdcard.com.maiyu.shopapp.fragment.MineFragment;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;
import testsdcard.com.maiyu.shopapp.widget.FragmentTabHost;

/**
 * 主体类
 * 商品商城app
 * （1）利用TabHost+Activity实现底部布局开销较大
 * （2）利用RadioButton(RadioGroup)+Fragment实现底部布局较为复杂
 * （3）采用FragmentTabHost+Fragment实现底部布局：实现简单，开销资源少
 */
public class MainActivity extends BaseActivity {


    //定义LayoutInfalter
    private LayoutInflater  mInfalter ;
    //自定义FragmentTabHost对象
    private FragmentTabHost mTabHost ;
    //创建List<Tab>集合：存放底部的5个tab
    private List<Tab> mTabs = new ArrayList<>(5);
    //自定义CnToolbar类
    private CnToolbar toolbar ;

    //定义购物车fragment
    private CartFragment    cartFragment ;

    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化界面
        initTab();

        //初始化toolbars
        //intiToobals();


    }


    /**
     * 初始化Toolbars
     */
    private void intiToobals() {

        toolbar   =  (CnToolbar)this.findViewById(R.id.toolbar);
    }

    /**
     * 初始化界面
     */
    private void initTab() {

        //分别创建5个Tab对象：Tab传入3个参数(int title , Class fragment , int icon)
        Tab tab_home  =  new Tab(R.string.home ,  HomeFragment.class ,R.drawable.selector_icon_home );
        Tab tab_hot  =  new Tab(R.string.hot ,  HotFragment.class ,R.drawable.selector_icon_hot );
        Tab tab_category  =  new Tab(R.string.category ,  CategoryFragment.class ,R.drawable.selector_icon_category);
        Tab tab_cart  =  new Tab(R.string.cart ,  CartFragment.class ,R.drawable.selector_icon_cart );
        Tab tab_mine  =  new Tab(R.string.mine ,  MineFragment.class ,R.drawable.selector_icon_mine );

        //把这5个tab对象添加到List集合中
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        //获取布局的inflater对象
        mInfalter   =   LayoutInflater.from(this);
        //绑定mTabHost的id
        mTabHost    =   (FragmentTabHost)this.findViewById(android.R.id.tabhost);
        //调用setup()方法，设置FragmentManager，以及指定用于装载Fragment的布局容器(这里是指正真切换的frameLayout)
        mTabHost.setup(this , getSupportFragmentManager() ,R.id.realtabcontent);

        //遍历mTabs集合
        for(Tab tab :mTabs){

            //新建5个TabSpec，并且设置好它的Indicator

            //获取TabSpec参数
            TabHost.TabSpec tabSpec    =  mTabHost.newTabSpec(getString(tab.getTitle()));
            //为该参数TabSpec设置indicator()
            tabSpec.setIndicator(buildIndicator(tab));
            //调用mTabHost.addTab()方法
            mTabHost.addTab(tabSpec , tab.getFragment() , null);
        }


        //为tabHost添加监听器
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //判断是不是选择了购物车选项
                if (tabId == getString(R.string.cart)) {
                       refDatas();  //更新数据

                }else {
//                    //显示toolbar状态
//                    toolbar.showSearchView();
//                    toolbar.hideTitleView();
//                    toolbar.getRightButton().setVisibility(View.GONE);
                }
            }
        });

        //去掉分割线
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        //设置默认选中页面
        mTabHost.setCurrentTab(0);

    }


    /**
     * 更新购物车数据
     */
    private void refDatas() {
        //判空
        if(cartFragment == null){
            //根据id获取fragment对象
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            //判断fragment是否为空
            if(fragment != null){
                //强制转换
                cartFragment    =   (CartFragment)fragment;

                //调用cartFragment的刷新方法，和设置toolbar
                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        }else {
            //调用cartFragment的刷新方法，和设置toolbar
            cartFragment.refData();
            cartFragment.changeToolbar();
        }
    }


    /**
     * 自定义为TabSpec添加数据的方法
     * @param tab
     * @return
     */
    private View buildIndicator(Tab tab) {
        //获取view对象
        View    view    =   mInfalter.inflate(R.layout.tab_indicator ,null);
        //绑定id
        ImageView   img   =   (ImageView)view.findViewById(R.id.icon_tab);
        TextView    text  =   (TextView)view.findViewById(R.id.txt_indicator);

        //设置背景图片和文字
        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());
        //返回文字
        return view ;
    }
}
