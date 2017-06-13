package testsdcard.com.maiyu.shopapp.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import testsdcard.com.maiyu.shopapp.bean.ShopingCart;
import testsdcard.com.maiyu.shopapp.bean.Wares;

/**
 * Created by maiyu on 2017/3/10.
 */
public class CartProvider {

    private static final String CART_JSON = "cart_json";    //
    private Context mContext;                               //上下文对象
    private SparseArray<ShopingCart> datas = null;          //稀疏数组：相当于java中的hasMap,比它更高效


    /**
     * 构造方法：
     * @param context
     */
    public CartProvider(Context context) {
        this.mContext   =   context ;
        datas   =   new SparseArray<>(10);  //这里先初始化为10个
        listToSpare();                      //列出本地的数据（本页面的数据）
    }

    /**
     * 添加数据
     * @param shopingCart
     */
    public void put(ShopingCart shopingCart){
        //定义中间变量
        ShopingCart temp = datas.get(shopingCart.getId().intValue());
        //判断原来是否已经选中了
        if(temp != null){
            //数量+1
            temp.setCount(temp.getCount() + 1);
        }else {
            //设置temp,并赋值
            temp    =   shopingCart ;
            temp.setCount(1);
        }
        //存放到datas
        datas.put(shopingCart.getId().intValue() , temp);
        //同步PreferencesUtils中的数据
        commit();

    }


    /**
     * 删除数据
     * @param shopingCart
     */
    public void delete(ShopingCart shopingCart){
        //从datas中删除数据
        datas.delete(shopingCart.getId().intValue());
        //同步PreferencesUtils中的数据
        commit();

    }

    /**
     * 更新数据
     * @param shopingCart
     */
    public void update(ShopingCart shopingCart){
        //把新数据存放到datas中
        datas.put(shopingCart.getId().intValue() , shopingCart);
        //同步PreferencesUtils中的数据
        commit();

    }

    /**
     * 获取本页面的所有数据（全选）
     * @return
     */
    public List<ShopingCart> getAll(){
        return getDataFromLocal();  //获取本地（本页面）的数据
    }


    /**
     * 提交
     */
    private void commit() {
        //获取data中的数据存放到carts
        List<ShopingCart> carts = spareToList();
        //把carts转化为字符串利用PreferencesUtils去存储（根据相应参数）
        PreferencesUtils.putString(mContext , CART_JSON , JSONUtil.toJSON(carts));
    }

    /**
     * 把数据放到list中
     * @return
     */
    private List<ShopingCart> spareToList() {
        int size = datas.size();            //获取data的长度
        //根据长度创建新的List<ShopingCart>集合
        List<ShopingCart> list = new ArrayList<>(size);
        for(int i = 0 ; i < size ; i++){
            //把datas中的数据添加到list中
            list.add(datas.valueAt(i));
        }
        return list ;
    }


    /**
     * 列出本地的数据（本页面）
     */
    private void listToSpare(){
        //获取本地已选中的数据
        List<ShopingCart> carts = getDataFromLocal();
        //判断是否为空
        if(carts != null && carts.size() > 0){
            for(ShopingCart cart : carts){
                //加载到datas集合中
                datas.put(cart.getId().intValue() , cart);
            }
        }
    }

    /**
     * 获取本地的数据（本页面）
     * @return
     */
    private List<ShopingCart> getDataFromLocal() {
        //根据上下文对象和cart_json字符串取出字符串
        String json = PreferencesUtils.getString(mContext , CART_JSON);
        //创建空的List<ShopingCart>集合
        List<ShopingCart> carts = null ;

        //如果json不为空
        if(json != null){
            //把字符串转化为List<ShopingCart>类型，并赋给cart
            carts = JSONUtil.fromJson(json , new TypeToken<List<ShopingCart>>(){}.getType());
        }
        return carts ;
    }

    /**
     * 把商品添加到购物车中
     * @param wares
     */
    public  void put(Wares wares){
        ShopingCart cart = convertData(wares);
        put(cart);
    }

    /**
     * 把商品对象Wares装换成ShopingCart对象
     * 返回ShopingCart对象
     * @param item
     * @return
     */
    private ShopingCart convertData(Wares item) {

        //new一个ShopingCart对象，添加Wares对象
        ShopingCart cart    =   new ShopingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart ;

    }

}
