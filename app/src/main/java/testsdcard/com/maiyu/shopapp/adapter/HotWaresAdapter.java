package testsdcard.com.maiyu.shopapp.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Wares;

/**HotFragment的Adapter
 * Created by maiyu on 2017/3/1.
 */
public class HotWaresAdapter  extends RecyclerView.Adapter<HotWaresAdapter.ViewHolder>{
    private LayoutInflater mInflater ;          //LayoutInflater对象
    private List<Wares> mDatas;                 //List<Wares>集合


    //初始化数据
    public HotWaresAdapter(List<Wares> wares){
        mDatas  =   wares;
    }


    /**
     * 加载数据
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater   =   LayoutInflater.from(parent.getContext());
        View  view  =   mInflater.inflate(R.layout.template_hot_wares , null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Wares ware = mDatas.get(position);
        holder.simpleDraweeView.setImageURI(Uri.parse(ware.getImgUrl()));
        holder.textTitle.setText(ware.getName());
        holder.textPrice.setText("$:" + ware.getPrice());
    }

    @Override
    public int getItemCount() {
        if(mDatas != null && mDatas.size() > 0){
            return mDatas.size();
        }
        return 0;
    }


    public Wares getData(int position){

        return mDatas.get(position);
    }


    public List<Wares> getDatas(){

        return  mDatas;
    }
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }

    public void addData(List<Wares> datas){

        addData(0,datas);
    }

    public void addData(int position,List<Wares> datas){

        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView simpleDraweeView ;
        private TextView textTitle;
        private TextView textPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            simpleDraweeView    =   (SimpleDraweeView)itemView.findViewById(R.id.drawee_view);
            textTitle           =   (TextView)itemView.findViewById(R.id.text_title);
            textPrice           =   (TextView)itemView.findViewById(R.id.text_price);
        }
    }
}
