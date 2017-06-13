package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.WareListActivity;
import testsdcard.com.maiyu.shopapp.adapter.HomeCategoryAdapter;
import testsdcard.com.maiyu.shopapp.bean.Banner;
import testsdcard.com.maiyu.shopapp.bean.Compaign;
import testsdcard.com.maiyu.shopapp.bean.HomeCampaign;
import testsdcard.com.maiyu.shopapp.decoration.CardViewtemDecortion;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;
import testsdcard.com.maiyu.shopapp.widget.Contants;

/**主页面fragment
 * Created by maiyu on 2017/2/23.
 */
public class HomeFragment extends Fragment {
    private SliderLayout    mSliderLayout;      //创建SliderLayout对象
    private PagerIndicator indicator ;          //创建PagerIndicator

    private RecyclerView    mRecyclerView;      //创建RecyclerView:比ListView更好
    private HomeCategoryAdapter mAdapter;       //创建自定义的适配器：cardView


    private Gson mGson = new Gson();                //创建Gson对象
    private List<Banner> mBanner ;                  //创建List<Banner>对象
    private static final String TAG = "HomeFragment";

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();   //创建OkHttpHelper对象


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View   view =   inflater.inflate(R.layout.fragment_home , container , false);
        mSliderLayout   =   (SliderLayout)view.findViewById(R.id.slider);
        indicator       =   (PagerIndicator)view.findViewById(R.id.custom_indicator);

        //加载炫酷图片
      //  initImageSlider();

        //加载首页的cardView视图
        initOkHttpImage();
        initRecyclerView(view);



        return view ;
    }

    /**
     * 利用封装的okHttp加载网络数据
     */
    private void initOkHttpImage() {

        //url地址
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";


        //这里用自己封装的方法来实现
        httpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()){


            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                //获取成功返回的数据
                mBanner = banners;
                //初始化炫酷轮播图片
                initImageSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

    /**
     * 加载首页面视图
     * @param view
     */
    private void initRecyclerView(View view) {
        mRecyclerView   =   (RecyclerView)view.findViewById(R.id.recycle);

         httpHelper.get(Contants.API.CAMPAING_HOME, new SpotsCallBack<List<HomeCampaign>>(getContext()) {


             @Override
             public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initDatas(homeCampaigns);
             }

             @Override
             public void onError(Response response, int code, Exception e) {

             }
         });




    }

    /**
     * 初始化数据
     * @param homeCampaigns
     */
    private  void initDatas(List<HomeCampaign> homeCampaigns ){


        //初始化适配器
        mAdapter    =   new HomeCategoryAdapter(homeCampaigns , getActivity());
        //为适配器设置每个商品主件的监听
        mAdapter.setOnCampaignListener(new HomeCategoryAdapter.OnCampaignListener() {
            @Override
            public void onClcik(View view, Compaign compaign) {

                //创建intent,跳转到商品列表Activity
                Intent  intent  =   new Intent(getActivity() , WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID , compaign.getId());

                startActivity(intent);

            }
        });

        //为该recyclerView设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //为该recyclerView设置图片分隔距离
        mRecyclerView.addItemDecoration(new CardViewtemDecortion());
        //为该recyclerView设置布局管理：此方法是必须的
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }



    /**
     * 初始化首页的商品广告条
     */
    private void initImageSlider() {


        //判断商品数据是否为空
        if(mBanner != null){
            //遍历数据
            for(Banner banner : mBanner){
                //创建TextSliderView对象
                TextSliderView  textSliderView = new TextSliderView(this.getActivity());
                //设置图片
                textSliderView.image(banner.getImgUrl());
                //设置描述
                textSliderView.description(banner.getName());
                //设置type
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                //把textSliderView添加到SliderLayout
                mSliderLayout.addSlider(textSliderView);
            }
        }



        //对SliderLayout进行一些自定义的配置
        //设置动画
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        //设置preset的展示方式：文字是从下往上出现的
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        //设置时长时长
        mSliderLayout.setDuration(3000);
        //设置indicator的位置:可以是小圆点
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
  //     mSliderLayout.setCustomIndicator(indicator);
        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {


                Log.d(TAG,"onPageScrolled");

            }

            @Override
            public void onPageSelected(int i) {

                Log.d(TAG,"onPageSelected");
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                Log.d(TAG,"onPageScrollStateChanged");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSliderLayout.stopAutoCycle();
    }
}
