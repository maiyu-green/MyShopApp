package testsdcard.com.maiyu.shopapp.bean;

/**Category类:用于存放炫酷页面的显示
 * Created by maiyu on 2017/2/24.
 */
public class Category extends BaseBean {
    private String name;
    public Category() {
    }
    public Category(String name) {
        this.name = name ;
    }
    public Category(long id , String name){
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
