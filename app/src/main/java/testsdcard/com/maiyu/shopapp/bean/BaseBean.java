package testsdcard.com.maiyu.shopapp.bean;

import java.io.Serializable;

/**存放id
 * Created by maiyu on 2017/2/24.
 */
public class BaseBean implements Serializable {
    protected   long  id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
