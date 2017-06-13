package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Favorites;
import testsdcard.com.maiyu.shopapp.bean.Wares;

/**收藏商品的adapter
 * Created by maiyu on 2017/6/12.
 */
public class FavoriteAdapter extends SimpleAdapter<Favorites> {

    public FavoriteAdapter(Context context, List<Favorites> datas) {
        super(context,datas , R.layout.template_favorite );
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Favorites favorites) {

        Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView)viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥ "+wares.getPrice());

        Button buttonRemove = viewHolder.getButton(R.id.btn_remove);
        Button buttonLike   = viewHolder.getButton(R.id.btn_like);

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }
}
