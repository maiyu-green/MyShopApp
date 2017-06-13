package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.ShopingCart;
import testsdcard.com.maiyu.shopapp.utils.CartProvider;
import testsdcard.com.maiyu.shopapp.widget.NumberAddSubView;

/**购物车适配器
 * Created by maiyu on 2017/3/11.
 */

public class CartAdapter extends SimpleAdapter<ShopingCart> implements BaseAdapter.OnItemClickListener{
    private CartProvider cartProvider ;     //卡车的相关操作类
    private CheckBox checkBox ;             //选中
    private TextView textView ;             //文本

    public static final String TAG = "CartAdapter" ;


    public CartAdapter(Context context, List<ShopingCart> datas ,final CheckBox checkBox ,TextView tv) {
        super(context, datas, R.layout.template_cart);

        setCheckBox(checkBox);      //设置选中
        setTextView(tv);            //设置文本显示
        cartProvider    =   new CartProvider(context);      //初始化CartProvider

        setOnItemClickListener(this);       //设置监听
        showTotalPrice();                   //展示总价格

    }

    @Override
    public void bindData(BaseViewHolder viewHolder, final ShopingCart shopingCart) {

        // 绑定并显示每个商品数据

        viewHolder.getTextView(R.id.text_title).setText(shopingCart.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥" + shopingCart.getPrice());
        SimpleDraweeView    draweeView = (SimpleDraweeView)viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(shopingCart.getImgUrl()));

        //设置是否选中
        CheckBox    checkBox = (CheckBox)viewHolder.getView(R.id.checkbox);
        checkBox.setChecked(shopingCart.isChecked());

        //每个商品右边的计数器类
        NumberAddSubView numberAddSubView   =   (NumberAddSubView)viewHolder.getView(R.id.num_control);
        numberAddSubView.setValue(shopingCart.getCount());
        numberAddSubView.setOnButtonClickListener(new NumberAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                shopingCart.setCount(value);            //设置值
                cartProvider.update(shopingCart);       //添加数量
                showTotalPrice();                       //展示总价格
            }

            @Override
            public void onButtonSubClick(View view, int value) {
                shopingCart.setCount(value);            //设置值
                cartProvider.delete(shopingCart);       //删除数量
                showTotalPrice();                       //展示总价格
            }
        });


    }

    //判断是否为空
    private boolean isNull(){
        return (mDatas != null && mDatas.size() > 0);
    }

    /**
     * 检查所有是否为空
     * @param isChecked
     */
    public void checkAll_None(boolean isChecked){
        if(!isNull()){
            return;
        }

        int i = 0 ;
        for(ShopingCart cart : mDatas){
            cart.setChecked(isChecked);
            notifyItemChanged(i);
            i++ ;
        }
    }

    /**
     * 删除卡车商品
     */
    public void delCart(){
        //商品为0，无需删除
        if(!isNull()){
            return;
        }
        //遍历遍历数据
        for(Iterator iterator = mDatas.iterator(); iterator.hasNext() ; ){
            //获取商品
            ShopingCart cart = (ShopingCart)iterator.next();
            //判断是否选中
            if(cart.isChecked()){
                int position    =   mDatas.indexOf(cart);
                //删除商品
                cartProvider.delete(cart);
                iterator.remove();  //
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * 获取总价格
     * @return
     */
    private float getTotalPrice(){
        //定义sum
        float sum = 0;
        //没有商品时直接返回0
        if(!isNull()){
            return sum ;
        }
        //遍历商品
        for (ShopingCart cart : mDatas){
            if(cart.isChecked()){
                sum += cart.getCount()*cart.getPrice();
            }
        }
        return sum ;
    }

    /**
     * 展示总价格
     */
    public void showTotalPrice(){
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }


    /**
     * 判断选中与非选中的显示
     * @param view
     * @param position
     */
    @Override
    public void OnClick(View view, int position) {
        ShopingCart cart    =   getItem(position);      //获取相应位置的商品
        cart.setChecked(!cart.isChecked());             //设置商品选中状态的改变
        notifyItemChanged(position);                    //更新

        //判断是否全部选中了
        checkListen();
        //展示总价格
        showTotalPrice();
    }

    /**
     * 设置是否所以的商品已经都被选中了，
     * 是的话则全选需设置为选中状态，否则为非全选状态
     */
    private void checkListen(){
        int count = 0;
        int checkNum = 0 ;
        if(mDatas != null){
            count = mDatas.size();      //获取数据长度

            //遍历商品判断是否全部选中了
            for(ShopingCart cart : mDatas){
                if(!cart.isChecked()){
                    checkBox.setChecked(false); //设置非全选显示
                    break;
                }else {
                    checkNum = checkNum + 1;        //设置已经选中的数量
                }
            }

            //判断是否都选中了
            if(count == checkNum){
                checkBox.setChecked(true);  //设置全选显示
            }
        }
    }

    //设置文本显示
    public void setTextView(TextView textView){
        this.textView = textView;
    }

    /**
     * 设置选中
     * @param ck
     */
    public void setCheckBox(CheckBox ck){
        this.checkBox = ck ;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();       //展示总价格
            }
        });
    }
}
