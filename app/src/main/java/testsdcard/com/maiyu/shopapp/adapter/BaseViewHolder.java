package testsdcard.com.maiyu.shopapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**基类：BaseViewHolder继承RecyclerView.ViewHolder
 * Created by maiyu on 2017/3/5.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //因为不知道是什么控件，多少控件，故而用SparseArray<View>数组来存放View
    private SparseArray<View> views;                     //存放一系列View
    protected BaseAdapter.OnItemClickListener listener;    //定义监听器

    /**
     * 初始化数据
     *
     * @param itemView
     * @param listener
     */
    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);

        views = new SparseArray<View>();        //初始化数组
        this.listener = listener;              //初始化监听
        itemView.setOnClickListener(this);          //设置监听
    }

    /**
     * 获取View
     *
     * @param id
     * @return
     */
    public View getView(int id) {
        return findView(id);
    }

    /**
     * 根据id找到View，返回泛型数据
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int id) {
        //找到相应view对象
        View view = views.get(id);
        //若为空
        if (view == null) {
            //初始化该view
            view = itemView.findViewById(id);
            //添加到views数组中
            views.put(id, view);
        }
        return (T) view;
    }

    /**
     * 监听器
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (listener != null) {
            //为当前view根据布局中的相应位置设置监听
            listener.OnClick(view, getLayoutPosition());
        }
    }






    /**
     * 获取TextView
     * @param id
     * @return
     */
    public TextView getTextView(int id){
        return findView(id);
    }

    /**
     * 获取ImageView
     * @param id
     * @return
     */
    public ImageView getImageView(int id){
        return findView(id);
    }

    /**
     * 获取Button
     */
    public Button getButton(int id){
        return findView(id);
    }
    public CheckBox getCheckBox(int id) {
        return findView(id);
    }


}
