package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Wares;
import testsdcard.com.maiyu.shopapp.utils.CartProvider;
import testsdcard.com.maiyu.shopapp.utils.ToastUtils;

/**HWAdapter适配器，继承自SimpleAdapter，参数为Wares类型
 * Created by maiyu on 2017/3/5.
 */
public class HWAdapter extends SimpleAdapter<Wares> {

    private CartProvider provider ;         //定义CartProvider对象
    private Context mContext;               //定义上下文对象

    /**
     * 初始化适配器
     * @param context
     * @param datas
     */
    public HWAdapter(Context context, List<Wares> datas) {
        //设置布局，初始化上下文，CartProvider对象
        super(context, datas, R.layout.template_hot_wares);
        this.mContext = context;
        provider    =   new CartProvider(context);
    }

    /**
     * 绑定数据
     * @param viewHolder
     * @param wares
     */
    @Override
    public void bindData(BaseViewHolder viewHolder, final Wares wares) {

        //分别获取相应控件，并设置显示出来
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice() + "");

        //为按钮设置监听
        Button  button  =   viewHolder.getButton(R.id.btn_add);
        if(button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //这里必须再次初始化provider，不然删除过的数据会重写显示出来
                    provider = new CartProvider(mContext);
                    provider.put(wares);
                    ToastUtils.show(mContext, "已添加到购物车");
                }
            });
        }
    }



    /**
     * 刷新布局：根据新布局id,刷新数据，重新布局
     * @param layoutId
     */
    public void resetLayout(int layoutId){
        this.mLayoutResId = layoutId ;
        notifyItemChanged(0 , getDatas().size());
    }


}
