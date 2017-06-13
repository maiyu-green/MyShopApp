package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Wares;

/**
 * Created by maiyu on 2017/3/7.
 */
public class CategoryWaresAdapter extends SimpleAdapter<Wares> {
    public CategoryWaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares) {
        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice() + "");

        SimpleDraweeView    simpleDraweeView    =   (SimpleDraweeView)viewHolder.getImageView(R.id.drawee_view);
        simpleDraweeView.setImageURI(Uri.parse(wares.getImgUrl()));
    }
}
