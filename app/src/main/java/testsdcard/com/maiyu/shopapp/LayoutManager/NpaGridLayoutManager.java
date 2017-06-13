package testsdcard.com.maiyu.shopapp.LayoutManager;

/**
 * Created by maiyu on 2017/3/8.
 */

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * 该类用于解决：recyclerview滑动与clear产生的异常
 * No Predictive Animations GridLayoutManager
 */
public  class NpaGridLayoutManager extends GridLayoutManager {
    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    /**重写该方法，返回false
     *
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public NpaGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NpaGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NpaGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }
}

