package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**基类：封装RecyclerView.Adapter
 * Created by maiyu on 2017/3/5.
 */
public abstract  class BaseAdapter<T , H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> mDatas ;                  //定义List<>集合，参数为泛型
    protected final Context mContext ;            //上下文对象
    protected final LayoutInflater mInflater ;    //LayoutInflater对象
    protected int mLayoutResId ;            //传入的布局id

    protected OnItemClickListener listener ;    //定义监听器接口


    //接口
    public  interface  OnItemClickListener  {
        void OnClick(View view, int position);
    }

    //设置接口
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener   =   listener ;
    }


    public BaseAdapter(Context context , int layoutResId){
        this(context , null,layoutResId );
    }

    /**
     * 初始化数据
     * @param context
     * @param datas
     * @param layoutResId
     */
    public BaseAdapter(Context context , List<T> datas  ,int layoutResId ){
        this.mDatas     =   datas ;             //初始化List<T>数据
        this.mContext   =   context ;           //初始化上下文
        this.mLayoutResId   =   layoutResId ;

        mInflater   =   LayoutInflater.from(context);       //获取Inflater对象
    }


    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //获取View
        View    view    =   mInflater.inflate(mLayoutResId , null , false);
        return new BaseViewHolder(view , listener);         //创建BaseViewHolde
    }

    /**
     * 绑定ViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        T   t  = getItem(position);     //获取当前T对象
        bindData(holder ,t);            //绑定
    }

    /**
     * 返回长度
     * @return
     */
    @Override
    public int getItemCount() {
        if(mDatas == null || mDatas.size() <= 0){
            return 0;
        }
        return mDatas.size();
    }

    /**
     * 获取当前子项
     * @param position
     * @return
     */
    public T getItem(int position){

        if(position >= mDatas.size()){
            return null;
        }return mDatas.get(position);
    }

    //定义一个抽象类型的方法
    public  abstract  void bindData(BaseViewHolder viewHolder , T t);





    //以下这几个方法根据需要去添加,因为上一个例子中有用到
    /**
     * 获取数据
     * @return
     */
    public List<T> getDatas(){

        return  mDatas;
    }

    /**
     * 清除数据
     */
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    /**
     * 添加数据
     * @param datas
     */
    public void addData(List<T> datas){

        addData(0,datas);
    }

    /**
     * 添加数据
     * @param position
     * @param datas
     */
    public void addData(int position,List<T> datas){

        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }


    /**
     * 根据List<T>来刷新数据
     * @param list
     */
    public void refreshData(List<T> list){

        //判空
        if(list !=null && list.size()>0){

            clearData();                    //清空数据
            int size = list.size();
            for (int i=0;i<size;i++){       //遍历新数据
                mDatas.add(i,list.get(i));  //添加新数据
                notifyItemInserted(i);      //刷新
            }

        }
    }

    /**
     * 加载更多数据
     * @param list
     */
    public void loadMoreData(List<T> list){

        //判空
        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){       //遍历并添加数据，然后刷新数据
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }
}
