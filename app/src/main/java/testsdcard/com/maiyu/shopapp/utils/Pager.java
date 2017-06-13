package testsdcard.com.maiyu.shopapp.utils;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import testsdcard.com.maiyu.shopapp.bean.Page;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.http.SpotsCallBack;

/**
 * Created by maiyu on 2017/3/12.
 */

public class Pager {
    private static Builder builder ;

    private OkHttpHelper httpHelper ;       //获取OkHttpHelper实例

    private static final  int NORMAL_STATE = 1;          //正常显示状态
    private static final  int REFRESH_STATE = 2;         //下拉刷新状态
    private static final  int LOADMORE_STATE = 3;        //上拉加载更多状态
    private   int state = NORMAL_STATE;           //设置初始化状态

    private Pager(){
        httpHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    public static Builder newBuilder(){
        builder =   new Builder();
        return builder;
    }

    public void request(){
        requestDatas();
    }

    public void putParam(String key , Object value){
        builder.params.put(key , value);
    }

    /**
     * RefreshLayout的监听
     */
    private void initRefreshLayout(){
        //设置支持加载更多（向下下拉加载更多）
        builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
        builder.mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            //向上下拉刷新
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshLayout.setLoadMore(builder.canLoadMore);
                refresh();
            }

            //向下下拉加载更多
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //判断是否还有数据
                if(builder.curPage < builder.totalPages){
                    loadMore();
                }else {
                    Toast.makeText(builder.mContext, "已经没有数据了" , Toast.LENGTH_SHORT).show();
                    materialRefreshLayout.finishRefreshLoadMore();
                    materialRefreshLayout.setLoadMore(false);
                    //builder.mRefreshLayout.finishRefreshLoadMore(); //停止加载更多
                }
            }
        });
    }


    /**
     * 向上下拉刷新
     */
    private void refresh() {

        builder.curPage =   1;                  //设置当前页数
        state   =   REFRESH_STATE ;     //刷新状态
        requestDatas();

    }
    /**
     * 向下下拉加载更多
     */
    private void loadMore() {
        builder.curPage =   builder.curPage + 1;            //当前页数
        state   =   LOADMORE_STATE ;        //加载更多状态
        requestDatas();

    }


    /**
     * 获取数据
     */
    private void requestDatas() {

        //定义请求url
        //http://112.124.22.238:8081/course_api/wares/hot?curPage=1&pageSize=10
        //String url = Contants.API.WARES_HOT + "?" + "curPage=" + curPage + "&pageSize=" + pageSize;
        String  url = buildUrl();
        httpHelper.get(url , new RequestCallBack(builder.mContext));
        //访问网络
    }

    private String buildUrl(){
        return builder.mUrl + "?" + buildUrlParams();
    }
    private String buildUrlParams(){
        HashMap<String , Object> map  = builder.params ;
        map.put("curPage" , builder.curPage);
        map.put("pageSize", builder.pageSize);

        StringBuffer    sb = new StringBuffer();
        for(Map.Entry<String , Object> entry : map.entrySet()){
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString() ;
        if(s.endsWith("&")){
            s = s.substring(0 , s.length()-1);
        }
        return s ;
    }

    //添加数据
    private <T> void showDatas(List<T> datas , int totalPage , int totalCount) {
        switch (state){
            case NORMAL_STATE :
                if(builder.onPageListener != null){
                    builder.onPageListener.load(datas , totalPage , totalCount);
                }
                break;

            case REFRESH_STATE :
                //向上刷新完成
                builder.mRefreshLayout.finishRefresh();
                if(builder.onPageListener != null){
                    builder.onPageListener.refresh(datas , totalPage , totalCount);
                }
                break;

            case LOADMORE_STATE :
                //向下下拉加载完毕
                builder.mRefreshLayout.finishRefreshLoadMore();
                if(builder.onPageListener != null){
                    builder.onPageListener.loadMore(datas , totalPage , totalCount);
                }

                break;
            default:
                break;
        }


    }


    public static class Builder{

        private Context mContext ;
        private Type mType ;
        private boolean canLoadMore ;

        private String mUrl ;
        private HashMap<String , Object> params = new HashMap<>(5);

        private int totalPages = 3 ;
        private int  curPage = 1;           //请求参数
        private int  pageSize = 10;         //请求参数

        private MaterialRefreshLayout mRefreshLayout;

        private OnPageListener onPageListener ;

        public Builder setUrl(String url){
            this.mUrl    =   url ;
            return builder;
        }
        public Builder putParam(String key , Object value){
            params.put(key , value);
            return builder ;
        }

        public Builder setPageSize(int pageSize){
            this.pageSize = pageSize ;
            return builder;
        }

        public Builder setLoadMore(boolean loadMore){
            this.canLoadMore = loadMore ;
            return builder;
        }


        public Builder setRefreshLayout(MaterialRefreshLayout refreshLayout){
            this.mRefreshLayout =   refreshLayout ;
            return builder;
        }

        public Builder setOnPageListener(OnPageListener onPageListener){
            this.onPageListener =   onPageListener ;
            return builder ;
        }

        public Pager builder(Context context , Type type){
            this.mContext   =   context ;
            this.mType      =   type;

            valid();
            return new Pager() ;
        }

        private void valid(){
            if(this.mContext    ==  null){
                throw new RuntimeException("content can't be null");
            }
            if(this.mUrl    ==  null || "".equals(this.mUrl)){
                throw new RuntimeException("url can't be null");
            }
            if(this.mRefreshLayout == null){
                throw new RuntimeException("MaterialRefreshLayout can't be null");
            }
        }

    }

    class RequestCallBack<T> extends SpotsCallBack<Page<T>>{

        public RequestCallBack(Context context) {
            super(context);

            super.mType =   builder.mType;
        }

        @Override
        public void onFailure(Request request, Exception e) {
            //super.onFailure(request, e);
            dismissDialog();
            ToastUtils.show(builder.mContext , "请求出错：" + e.getMessage());
            if(state == REFRESH_STATE){
                builder.mRefreshLayout.finishRefresh();
            }else if(state == LOADMORE_STATE){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }

        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            builder.curPage     =   page.getCurrentPage();
            builder.pageSize    =   page.getPageSize() ;
            builder.totalPages  =   page.getTotalPage() ;

            showDatas(page.getList() , page.getTotalPage() , page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            ToastUtils.show(builder.mContext , "数据加载失败");
            if(state == REFRESH_STATE){
                builder.mRefreshLayout.finishRefresh();
            }else if(state == LOADMORE_STATE){
                builder.mRefreshLayout.finishRefreshLoadMore();
            }
        }

    }


    public interface OnPageListener<T>{
        void load(List<T> datas, int totalPage, int totalCount);
        void loadMore(List<T> datas, int totalPage, int totalCount);
        void refresh(List<T> datas, int totalPage, int totalCount);
    }
}


