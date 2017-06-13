package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Compaign;
import testsdcard.com.maiyu.shopapp.bean.HomeCampaign;

/**主页面适配器：用于加载设置首页图片
 * Created by maiyu on 2017/2/24.
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {

    private static int VIEW_LEFT = 0 ;      //用于标记采用哪个布局
    private static int VIEW_TYPE_R =1;

    private LayoutInflater  mInflater;      //定义LayoutInflater
    private List<HomeCampaign> mDatas;      //List<HomeCategory>列表
    private Context mContext ;

    private OnCampaignListener mListener;

    public  void setOnCampaignListener (OnCampaignListener listener){
        mListener = listener;
    }

    /**
     * 初始化列表数据
     * @param datas
     */
    public HomeCategoryAdapter(List<HomeCampaign>  datas , Context context){
        mDatas      =   datas ;
        mContext    =   context ;
    }


    /**
     * 创建VIewHolder对象
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //获得LayoutInflater对象
        mInflater   =   LayoutInflater.from(parent.getContext());

        //判断是单还是双，是要显示左边大图还是右边大图
        if(viewType == VIEW_TYPE_R){

            return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview2
                            , parent , false));
        }

        return  new ViewHolder(mInflater.inflate(R.layout.template_home_cardview
                , parent , false));
    }


    /**
     * 绑定ViewHolder的数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //创建HomeCategory对象
        HomeCampaign    homeCampaign    =   mDatas.get(position);
        //为viewHolder设置数据
        holder.textTitle.setText(homeCampaign.getTitle());
        Picasso.with(mContext).load(homeCampaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(homeCampaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(homeCampaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);
    }


    @Override
    public int getItemCount() {
        return mDatas.size();       //返回list的长度
    }


    @Override
    public int getItemViewType(int position) {
        //判断单还是双页：显示左边大图还是右边大图
        if(position % 2 == 0){
            return VIEW_TYPE_R;
        }
        else {
            return  VIEW_LEFT;
        }
    }

    /**
     * ViewHolder类，
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView    textTitle;
        ImageView   imageViewBig;
        ImageView   imageViewSmallTop;
        ImageView   imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);

            textTitle   =   (TextView)itemView.findViewById(R.id.text_title);
            imageViewBig=   (ImageView)itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop=   (ImageView)itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom=   (ImageView)itemView.findViewById(R.id.imgview_small_bottom);


            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
            if(mListener != null){
                switch (view.getId()){
                    case R.id.imgview_big :
                        mListener.onClcik(view , homeCampaign.getCpOne());
                        break;
                    case R.id.imgview_small_top :
                        mListener.onClcik(view , homeCampaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom :
                        mListener.onClcik(view , homeCampaign.getCpThree());
                        break;
                }
            }

        }
    }

    public interface OnCampaignListener{
        void onClcik(View view, Compaign compaign);
    }
}
