package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.ShopingCart;

/**用于显示购物的图片和计算总价格
 * Created by maiyu on 2017/4/4.
 */

public class OrderAdapter extends  SimpleAdapter<ShopingCart>{

    /**
     * 初始化
     * @param context
     * @param datas
     */
    public OrderAdapter(Context context, List<ShopingCart> datas) {
        super(context, R.layout.template_order_wares);
    }

    /**
     * 绑定布局中的控件
     * @param viewHolder
     * @param shopingCart
     */
    @Override
    public void bindData(BaseViewHolder viewHolder, ShopingCart shopingCart) {

        //获取simpleDraweeView抽屉view
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView)viewHolder.getView(R.id.drawee_view);
       //设置图片
        simpleDraweeView.setImageURI(Uri.parse(shopingCart.getImgUrl()));
    }


    /**
     * 计算出总价格
     * @return
     */
    public float getTotalPrice(){

        //初始化总价格0
        float sum=0;

        //判空
        if(!isNull())
            //返回数量
            return sum;

        //遍历购物车数据
        for (ShopingCart cart: mDatas) {
            //计算总价格
            sum += cart.getCount()*cart.getPrice();
        }

        //返回总价格
        return sum;
    }


    /**
     * 判断是否为空
     * @return
     */
    private boolean isNull(){

        //数据不为空并且商品大于0
        return (mDatas !=null && mDatas.size()>0);
    }

}
