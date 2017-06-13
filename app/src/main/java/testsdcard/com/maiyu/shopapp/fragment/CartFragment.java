package testsdcard.com.maiyu.shopapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.activity.CreateOrderActivity;
import testsdcard.com.maiyu.shopapp.activity.MainActivity;
import testsdcard.com.maiyu.shopapp.adapter.CartAdapter;
import testsdcard.com.maiyu.shopapp.bean.ShopingCart;
import testsdcard.com.maiyu.shopapp.decoration.DividerItemDecoration;
import testsdcard.com.maiyu.shopapp.http.OkHttpHelper;
import testsdcard.com.maiyu.shopapp.utils.CartProvider;
import testsdcard.com.maiyu.shopapp.widget.CnToolbar;

/**购物车fragment
 * Created by maiyu on 2017/2/23.
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    public static final int ACTION_EDIT = 1;        //编辑---显示删除按钮标志
    public static final int ACTION_CAMPLATE = 2;    //编辑---显示结算按钮标志

    //@ViewInject(R.id.txt_total)
    //private TextView mTextTotal;

    private CheckBox mCheckBox ;        //是否选中按钮
    private TextView mTextTotal ;       //总价格显示
    private Button  mBtnOrder ;         //结算按钮
    private Button  mBtnDelete;         //删除按钮
    private RecyclerView    mRecycler ; //RecyclerView显示

    private CartAdapter mAdapter ;          //CartAdapter对象
    private CartProvider cartProvider ;     //CartProvider对象
    private CnToolbar   mToolbar ;          //CnToolbar对象

    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();



    @Override
    public void init() {



    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View   view =   inflater.inflate(R.layout.fragment_cart , container , false);
        findView(view);     //为控件绑定id
        //ViewUtils.inject(this , view);

        //初始化CartProvider对象
        cartProvider    =   new CartProvider(getContext());
        //显示数据
        showDatas();

        return view ;
    }




    /**
     * onAttach方法，拿到MainActivity的toolbar对象
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof MainActivity){
            //拿到MainActivity对象
            MainActivity activity = (MainActivity)context ;
            //拿到MainActivity的Toolbar对象
            mToolbar    =   (CnToolbar)activity.findViewById(R.id.toolbar);

            //改变Toolbar的显示
            changeToolbar();
        }
    }

    /**
     * 为控件本地id和设置监听
     * @param v
     */
    private void findView(View v) {
        mCheckBox   =   (CheckBox)v.findViewById(R.id.checkbox_all);
        mTextTotal  =   (TextView)v.findViewById(R.id.txt_total);
        mBtnOrder     =   (Button)v.findViewById(R.id.btn_order);
        mBtnDelete  =   (Button)v.findViewById(R.id.btn_del);
        mRecycler   =   (RecyclerView)v.findViewById(R.id.recycler_view);

        mBtnOrder.setOnClickListener(new MyButtonListener());
        mBtnDelete.setOnClickListener(new MyButtonListener());
    }


    /**
     * 展示数据
     */
    private void showDatas() {
        //获取List<ShopingCart>数据
        List<ShopingCart>   carts = cartProvider.getAll();
        //初始化Adapter
        mAdapter    =   new CartAdapter(getContext() , carts ,mCheckBox ,mTextTotal);

        mRecycler.setAdapter(mAdapter);
        //为RecyclerView设置布局管理器，动画，子项间隔
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

    }


    /**
     * 编辑选中的监听：删除or结算
     * @param v
     */
    @Override
    public void onClick(View v) {
        int action = (int)v.getTag();
        if(ACTION_EDIT == action){
            showDelControl();       //显示删除按钮
        }else if(ACTION_CAMPLATE == action){
            hideDelControl();       //隐藏删除按钮
        }

    }

    /**
     * 刷新数据
     */
    public void refData(){
        mAdapter.clearData();                               //清除mAdapter
        List<ShopingCart>   carts = cartProvider.getAll();  //重新获取数据
        mAdapter.addData(carts);                            //把数据设置到mAdapter中
        mAdapter.showTotalPrice();                          //显示总价格
    }

    /**
     * 改变toolbar的布局及显示情况
     */
    public void changeToolbar(){
        mToolbar.hideSearchView();              //隐藏搜索图标
        mToolbar.showTitleView();               //隐藏标题
        mToolbar.setTitle(R.string.cart);           //设置新标题
        mToolbar.getRightButton().setVisibility(View.VISIBLE);  //设置编辑按钮可见
        mToolbar.setRightButtonText("编辑");

        mToolbar.getRightButton().setOnClickListener(this);     //为编辑按钮设置监听

        mToolbar.getRightButton().setTag(ACTION_EDIT);          //初始化编辑按钮的显示情况

    }


    /**
     * 隐藏删除按钮，显示结算按钮
     */
    private void hideDelControl(){
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);

        mBtnDelete.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mAdapter.checkAll_None(true);       //设置全选
        mAdapter.showTotalPrice();          //展示总价格

        mCheckBox.setChecked(true);
    }

    /**
     * 显示删除按钮，隐藏结算按钮
     */
    private void showDelControl(){
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDelete.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mAdapter.checkAll_None(false);          //不设置选中
        mCheckBox.setChecked(false);
    }

    /**
     * 自定义监听器：监听删除和付款按钮
     */
    private class MyButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch ( v.getId()){
                case R.id.btn_order :

                    Intent intent = new Intent(getActivity() , CreateOrderActivity.class);
                    startActivity(intent , true);

                    Log.d("CartFragment" , "付款成功");
                    break;
                case R.id.btn_del :
                    Log.d("CartFragment" , "删除成功");
                    mAdapter.delCart();     //删除监听
                    break;

            }
        }
    }
}








