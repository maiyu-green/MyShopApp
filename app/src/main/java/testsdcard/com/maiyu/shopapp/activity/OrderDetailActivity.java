package testsdcard.com.maiyu.shopapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import testsdcard.com.maiyu.shopapp.R;

/**
 * 我的订单数据Activity
 * Created by maiyu on 2017/6/13.
 */

public class OrderDetailActivity  extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
    }
}
