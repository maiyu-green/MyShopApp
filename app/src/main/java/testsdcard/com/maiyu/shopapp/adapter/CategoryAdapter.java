package testsdcard.com.maiyu.shopapp.adapter;

import android.content.Context;

import java.util.List;

import testsdcard.com.maiyu.shopapp.R;
import testsdcard.com.maiyu.shopapp.bean.Category;

/**CategoryFragment的适配器
 * Created by maiyu on 2017/3/6.
 */
public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> datas) {
        //初始化布局文件R.layout.template_single_text
        super(context, datas, R.layout.template_single_text);
    }

    @Override
    public void bindData(BaseViewHolder viewHolder, Category category) {
        //绑定id并设置显示
        viewHolder.getTextView(R.id.textView).setText(category.getName());

    }
}
