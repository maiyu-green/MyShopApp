package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;

import java.util.List;

import testsdcard.com.maiyu.shopapp.LayoutManager.NpaGridLayoutManager;
import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.WareDetailActivity;
import testsdcard.com.maiyu.shopapp.adapter.BaseAdapter;
import testsdcard.com.maiyu.shopapp.adapter.CategoryAdapter;
import testsdcard.com.maiyu.shopapp.adapter.CategoryWaresAdapter;
import testsdcard.com.maiyu.shopapp.bean.Banner;
import testsdcard.com.maiyu.shopapp.bean.Category;
import testsdcard.com.maiyu.shopapp.bean.Page;
import testsdcard.com.maiyu.shopapp.bean.Wares;
import testsdcard.com.maiyu.shopapp.decoration.DividerGridItemDecoration;
import testsdcard.com.maiyu.shopapp.decoration.DividerItemDecoration;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**分类fragment
 * Created by maiyu on 2017/2/23.
 * 现在存在一个问题：就是下拉后再点击左边一栏目录会出错：
 * 原因据说是：
 */
public class CategoryFragment extends BaseFragment {



    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView ;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout ;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefresh ;

    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerViewWares ;





    private final static int NORMAL_STATE = 1;          //正常显示状态
    private final static int REFRESH_STATE = 2;         //下拉刷新状态
    private final static int LOADMORE_STATE = 3;        //上拉加载更多状态
    private  static int state = NORMAL_STATE;           //设置初始化状态

    private int totalPages = 3 ;

    private int  curPage = 1;           //请求参数
    private int  pageSize = 10;         //请求参数
    private long  category_id = 0 ;
//
//    private RecyclerView mRecyclerView ;            //左边页面的RecyclerView
//    private SliderLayout mSliderLayout ;            //炫酷页面SliderLayout
    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();      //获取OKHttpHelper实例
    private CategoryAdapter mCategoryAdapter ;          //定义CategoryAdapter


    private CategoryWaresAdapter    mWaresAdapter ;     //wares的适配器
   // private  boolean mIsRefreshing = false;



    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return   inflater.inflate(R.layout.fragment_category , container , false);
    }


    @Override
    public void init() {

        requestCategoryData();      //左边recyclerView请求数据
        requestCategorySlider() ;       //右边炫酷页面Slider请求数据

        setRefreshListener() ;          //设置加载与刷新
    }




    /**
     * 左边页面请求数据
     */
    private void requestCategoryData(){
        //传入相应url,和返回类型参数
        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                    showCategoryData(categories);   //展示左边分类数据

                    if(categories != null  && categories.size() > 0){
                        category_id  =   categories.get(0).getId();

                       // curPage =   1;
                        //state   =   NORMAL_STATE ;
                        requestCategoryWaresDatas(category_id);

                    }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    /**
     * 展示左边分栏数据
     * @param categorys
     */
    private void showCategoryData(List<Category> categorys){

        //初始化CategoryAdapter适配器
        mCategoryAdapter    =   new CategoryAdapter(getContext() , categorys);
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Category category = mCategoryAdapter.getItem(position);

                category_id =   category.getId();
                curPage =   1;
                state   =   NORMAL_STATE ;
                requestCategoryWaresDatas(category_id);

            }
        });

        //为mRecyclerView设置适配器和相关属性
        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() ));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }


    /**
     * 右边炫酷页面slider请求数据
     */
    private void requestCategorySlider() {

        //String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";
        String url =  Contants.API.BANNER + "?type=1";


        //这里用自己封装的方法来实现：传入相应url ,和相应返回数据类型作为参数
        mHttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()){


            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                //显示炫酷页面数据
                showCategorySlider(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }


    /**
     * 显示炫酷页面数据
     */
    private void showCategorySlider(List<Banner> banners) {


        //判断传回来的数据是否为空
        if (banners != null) {
            //遍历数据
            for (Banner banner : banners) {
                //设置数据
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(textSliderView);
            }
        }

        //对SliderLayout进行一些自定义的配置
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
    }


    /**
     * 右下角的商品请求
     * @param categoryId
     */
    private void requestCategoryWaresDatas(Long categoryId){

        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId
                + "&curPage=" + curPage + "&pageSize=" + pageSize ;

        mHttpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                    curPage =   waresPage.getCurrentPage();
                    //totalPages  =   waresPage.getTotalPage();
                //商品显示
                    showCategoryWaresDatas(waresPage.getList());

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    /**
     * 右下角的商品显示
     * @param wares
     */
    private void showCategoryWaresDatas(List<Wares> wares) {
        switch (state){
            case NORMAL_STATE :
                if(mWaresAdapter == null){
                    mWaresAdapter    =   new CategoryWaresAdapter(getContext() ,wares);

                    mWaresAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void OnClick(View view, int position) {

                            Wares wares = mWaresAdapter.getItem(position);

                            Intent intent = new Intent(getActivity() , WareDetailActivity.class);
                            intent.putExtra(Contants.WARE , wares);
                            startActivity(intent);

                        }
                    });
                    mRecyclerViewWares.setAdapter(mWaresAdapter);

                    //为RecyclerView设置布局管理器，动画，子项间隔
                    mRecyclerViewWares.setLayoutManager(new GridLayoutManager(getContext() , 2));

                    mRecyclerViewWares.setLayoutManager(new NpaGridLayoutManager(getContext() ,2));
                   // mRecyclerViewWares.setLayoutManager(new GridLayoutManager(getContext() , 2));
                    mRecyclerViewWares.setItemAnimator(new DefaultItemAnimator());
                    //mRecyclerViewWares.addItemDecoration(new DividerItemDecoration());
                    mRecyclerViewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else {
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(wares);
                }

                break;

            case REFRESH_STATE :
                //先清理数据
                mWaresAdapter.clearData();
                //然后重写加载数据
                mWaresAdapter.addData(wares);

                //设置位置
                mRecyclerViewWares.scrollToPosition(0);
                //向上刷新完成
                mRefresh.finishRefresh();
                break;

            case LOADMORE_STATE :
                //得到当前页面最下面位置
                int position = mWaresAdapter.getDatas().size();
                //从position位置开始加载数据
                mWaresAdapter.addData(position , wares);

                //为view设置当前滑动位置:为新加载完毕后的最下面
                mRecyclerViewWares.scrollToPosition(mWaresAdapter.getDatas().size());
                //向下下拉加载完毕
                mRefresh.finishRefreshLoadMore();

                break;
            default:
                break;
        }

    }


    /**
     * RefreshLayout的监听
     */
    private void setRefreshListener(){

        //设置支持加载更多（向下下拉加载更多）
        mRefresh.setLoadMore(true);
        //mIsRefreshing   =   false ;
        mRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            //向上下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                addRefresh();
            }

            //向下下拉加载更多
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //判断是否还有数据
                if(curPage < totalPages){
                    loadMore();
                }else {
                    Toast.makeText(getContext(), "已经没有数据了" , Toast.LENGTH_SHORT).show();

                    mRefresh.finishRefreshLoadMore(); //停止加载更多
                }
            }
        });
    }

    /**
     * 向上下拉刷新
     */
    private void addRefresh() {

        curPage =   1;                  //设置当前页数
        state   =   REFRESH_STATE ;     //刷新状态
        requestCategoryWaresDatas(category_id);

    }
    /**
     * 向下下拉加载更多
     */
    private void loadMore() {
        curPage =   curPage + 1;            //当前页数
        state   =   LOADMORE_STATE ;        //加载更多状态
        requestCategoryWaresDatas(category_id);

    }




}
