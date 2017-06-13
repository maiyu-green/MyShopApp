package testsdcard.com.maiyu.shopapp.city;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import testsdcard.com.maiyu.shopapp.city.model.CityModel;
import testsdcard.com.maiyu.shopapp.city.model.DistrictModel;
import testsdcard.com.maiyu.shopapp.city.model.ProvinceModel;


/**
 * 自定义DefaultHandler类
 * 用于解析省市县三级数据
 * author :maiyu
 */
public class XmlParserHandler extends DefaultHandler {

	/**
	 * 存储所有的解析对象
	 */
	private List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();

	public XmlParserHandler() {

	}

	public List<ProvinceModel> getDataList() {
		return provinceList;
	}

	/**
	 * 开始解析文档
	 * @throws SAXException
	 */
	@Override
	public void startDocument() throws SAXException {

	}

	//初始化省，市，县
	ProvinceModel provinceModel = new ProvinceModel();
	CityModel cityModel = new CityModel();
	DistrictModel districtModel = new DistrictModel();

	/**
	 * 开始解析元素
	 * @param uri
	 * @param localName
	 * @param qName
	 * @param attributes
	 * @throws SAXException
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
							 Attributes attributes) throws SAXException {
		//解析省
		if (qName.equals("province")) {
			//创建省对象
			provinceModel = new ProvinceModel();
			//设置省的名字
			provinceModel.setName(attributes.getValue(0));
			//设置省包含的城市
			provinceModel.setCityList(new ArrayList<CityModel>());
		} else if (qName.equals("city")) {
			//解析城市

			//创建城市对象
			cityModel = new CityModel();
			//设置城市名字
			cityModel.setName(attributes.getValue(0));
			//设置城市包含的县
			cityModel.setDistrictList(new ArrayList<DistrictModel>());
		} else if (qName.equals("district")) {
			//解析县

			//创建县对象
			districtModel = new DistrictModel();
			//设置县的名字
			districtModel.setName(attributes.getValue(0));
			//设置县的邮编
			districtModel.setZipcode(attributes.getValue(1));
		}
	}

	/**
	 * 结束解析元素---遇到结束标记
	 * @param uri
	 * @param localName
	 * @param qName
	 * @throws SAXException
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		//县结束
		if (qName.equals("district")) {
			//把县加入当前城市
			cityModel.getDistrictList().add(districtModel);
		} else if (qName.equals("city")) {
			//把城市加入当前省份
			provinceModel.getCityList().add(cityModel);
		} else if (qName.equals("province")) {
			//把省份加入省份列表
			provinceList.add(provinceModel);
		}
	}

	/**
	 * 当SAX解析器处理字符数据时触发
	 * 除了endDocument方法外，其他方法每调用一次后就会触发
	 * 此方法一次
	 * @param ch
	 * @param start
	 * @param length
	 * @throws SAXException
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
	}


	/**
	 * 结束文档解析
	 * @throws SAXException
	 */
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
}
