package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;

/**
 * Created by maiyu on 2017/3/5.
 */
public class TSAdapter extends SimpleAdapter<String> {
    public TSAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.item);

    }

    @Override
    public void bindData(BaseViewHolder viewHolder, String s) {
        viewHolder.getTextView(R.id.text).setText(s + "");
    }
}
