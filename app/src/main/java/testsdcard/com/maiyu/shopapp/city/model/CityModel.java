package testsdcard.com.maiyu.shopapp.city.model;

import java.util.List;

/**
 * City类
 */
public class CityModel {
	//定义城市名字
	private String name;
	//定义List集合：乡区
	private List<DistrictModel> districtList;

	//一些列的get,set方法，和构造方法，toString方法
	
	public CityModel() {
		super();
	}

	public CityModel(String name, List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", districtList=" + districtList
				+ "]";
	}
	
}
