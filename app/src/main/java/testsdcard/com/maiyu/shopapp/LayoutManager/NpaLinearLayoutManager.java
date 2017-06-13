package testsdcard.com.maiyu.shopapp.LayoutManager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by maiyu on 2017/3/8.
 */
public class NpaLinearLayoutManager extends LinearLayoutManager {
    public NpaLinearLayoutManager(Context context) {
        super(context);
    }

    public NpaLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NpaLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 重写该方法，去捕捉该异常
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try{
            super.onLayoutChildren(recycler, state);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }
}
