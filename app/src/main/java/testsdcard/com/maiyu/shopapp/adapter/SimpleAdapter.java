package testsdcard.com.maiyu.shopapp.adapter;


import android.content.Context;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;

/**继承BaseAdapter的类SimpleAdapter
 * Created by maiyu on 2017/3/5.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T , BaseViewHolder> {
    public SimpleAdapter(Context context , int layoutResId){
        super(context , layoutResId);
    }

    public SimpleAdapter(Context context, List<T> datas, int layoutResId) {
        super(context, datas, layoutResId);
    }


    public SimpleAdapter(Context context, List<T> datas) {
        super(context, datas, R.layout.template_hot_wares);
    }
}
