package testsdcard.com.maiyu.shopapp.bean;

/**HomeCategory实体类
 * Created by maiyu on 2017/2/24.
 */
public class HomeCategory extends Category {
    private int imgBig ;
    private int imgSmallTop;
    private int imgSmallBottom;

    public int getImgBig() {
        return imgBig;
    }

    public void setImgBig(int imgBig) {
        this.imgBig = imgBig;
    }

    public int getImgSmallBottom() {
        return imgSmallBottom;
    }

    public void setImgSmallBottom(int imgSmallBottom) {
        this.imgSmallBottom = imgSmallBottom;
    }

    public int getImgSmallTop() {
        return imgSmallTop;
    }

    public void setImgSmallTop(int imgSmallTop) {
        this.imgSmallTop = imgSmallTop;
    }

    public HomeCategory( String name, int imgBig , int imgSmallTop, int imgSmallBottom) {
        super( name);
        this.imgSmallBottom = imgSmallBottom;
        this.imgSmallTop = imgSmallTop;
        this.imgBig = imgBig;
    }
}
