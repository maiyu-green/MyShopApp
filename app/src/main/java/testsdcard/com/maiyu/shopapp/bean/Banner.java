package testsdcard.com.maiyu.shopapp.bean;

/**
 * Created by maiyu on 2017/2/25.
 */
public class Banner extends BaseBean {
    private String  name;
    private String  imgUrl;
    private String  description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
