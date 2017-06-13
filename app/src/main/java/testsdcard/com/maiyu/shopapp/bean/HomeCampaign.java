package testsdcard.com.maiyu.shopapp.bean;

import java.io.Serializable;

/**
 * Created by maiyu on 2017/2/25.
 */
public class HomeCampaign  implements Serializable {
    private long id ;
    private String title ;
    private Compaign cpOne ;
    private Compaign cpTwo ;
    private Compaign cpThree;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Compaign getCpOne() {
        return cpOne;
    }

    public void setCpOne(Compaign cpOne) {
        this.cpOne = cpOne;
    }

    public Compaign getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Compaign cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Compaign getCpThree() {
        return cpThree;
    }

    public void setCpThree(Compaign cpThree) {
        this.cpThree = cpThree;
    }
}
