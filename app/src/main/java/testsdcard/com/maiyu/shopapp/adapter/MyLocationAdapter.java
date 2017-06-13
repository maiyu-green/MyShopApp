package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.MyLocation;

/**收货地址的适配器
 * Created by maiyu on 2017/4/10.
 */

public class MyLocationAdapter extends SimpleAdapter<MyLocation> {

    private MyLocationListener listener;    //监听器接口

    /**
     * 构造方法
     * @param context
     * @param datas
     * @param listener
     */
    public MyLocationAdapter(Context context, List<MyLocation> datas, MyLocationListener listener) {
        super(context , datas ,R.layout.template_location); //这里传入布局

        this.listener = listener;
    }


    /**
     * 设置控件
     * @param viewHolder
     * @param item
     */
    @Override
    public void bindData(BaseViewHolder viewHolder,final MyLocation item) {

        viewHolder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHolder.getTextView(R.id.txt_phone).setText(item.getPhone());
        viewHolder.getTextView(R.id.txt_location).setText(item.getAddr());

        final CheckBox checkBox = viewHolder.getCheckBox(R.id.cb_is_defualt);
        final boolean isDefault = item.getDefault();
        checkBox.setChecked(isDefault);

        if(isDefault){
            checkBox.setText("默认地址");
        }
        else {
            checkBox.setClickable(true);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && listener != null) {

                        //刷新数据
                        item.setDefault(true);
                        listener.setDefault(item);
                    }
                }
            });
        }

    }


    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


    //定义监听接口
    public interface MyLocationListener{
        public void setDefault(MyLocation location);
    }


}
