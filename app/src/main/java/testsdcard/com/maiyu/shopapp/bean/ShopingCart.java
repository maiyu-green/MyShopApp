package testsdcard.com.maiyu.shopapp.bean;

import java.io.Serializable;

/**ShopingCart类：继承热卖商品类，增加变量count,isChecked
 * Created by maiyu on 2017/3/10.
 */
public class ShopingCart extends Wares implements Serializable {
    private int count ;
    private boolean isChecked = true ;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
