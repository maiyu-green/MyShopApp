package testsdcard.com.maiyu.shopapp.city.model;

/**
 * 区域类:名字，编码，
 * get,set,super,toString方法
 */
public class DistrictModel {
	//区域名字
	private String name;
	//编码
	private String zipcode;


	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String zipcode) {
		super();
		this.name = name;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", zipcode=" + zipcode + "]";
	}

}
