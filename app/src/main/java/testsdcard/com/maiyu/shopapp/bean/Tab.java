package testsdcard.com.maiyu.shopapp.bean;

/**Tab实体类
 * 用于定义主页面最下面的图片和标题显示
 * Created by maiyu on 2017/2/23.
 */
public class Tab {
    private int title ;
    private int icon ;
    private Class fragment ;

    public Tab(int title, Class fragment, int icon) {
        this.title = title;
        this.fragment = fragment;
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
