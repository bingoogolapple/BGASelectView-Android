package cn.bingoogolapple.selectview.demo.util;

import android.content.Context;
import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.bingoogolapple.selectview.demo.model.CascadeModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/11 15:56
 * 描述:地址解析工具类
 */
public class AddressXmlParserHandler extends DefaultHandler {
    private List<CascadeModel> provinceList = new ArrayList<>();
    private CascadeModel provinceModel;
    private CascadeModel cityModel;
    private CascadeModel districtModel;

    @Override
    public void startDocument() throws SAXException {
        // 当读到第一个开始标签的时候，会触发这个方法
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 当遇到开始标记的时候，调用这个方法
        if (qName.equals("province")) {
            provinceModel = new CascadeModel();
            provinceModel.name = attributes.getValue(0);
            provinceModel.id = attributes.getValue(1);
            provinceModel.childrens = new ArrayList<>();
        } else if (qName.equals("city")) {
            cityModel = new CascadeModel();
            cityModel.name = attributes.getValue(0);
            cityModel.id = attributes.getValue(1);
            cityModel.childrens = new ArrayList<>();
        } else if (qName.equals("district")) {
            districtModel = new CascadeModel();
            districtModel.name = attributes.getValue(0);
            districtModel.id = attributes.getValue(1);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 遇到结束标记的时候，会调用这个方法
        if (qName.equals("district")) {
            cityModel.childrens.add(districtModel);
        } else if (qName.equals("city")) {
            provinceModel.childrens.add(cityModel);
        } else if (qName.equals("province")) {
            provinceList.add(provinceModel);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    public static List<CascadeModel> getProvinceList(Context context) {
        AddressXmlParserHandler parserHandler = new AddressXmlParserHandler();
        try {
            InputStream input = context.getAssets().open("address_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();

            parser.parse(input, parserHandler);
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parserHandler.provinceList;
    }

    public static CascadeModel getSelectedProvinceModel(List<CascadeModel> provinceList, String provinceId) {
        CascadeModel result = null;
        for (CascadeModel provinceModel : provinceList) {
            if (provinceModel.id.equals(provinceId)) {
                result = provinceModel;
            }
        }
        return result;
    }

    public static CascadeModel getSelectedCityModel(CascadeModel provinceModel, String cityId) {
        CascadeModel result = null;
        if (provinceModel != null) {
            for (CascadeModel cityModel : provinceModel.childrens) {
                if (cityModel.id.equals(cityId)) {
                    result = cityModel;
                }
            }
        }
        return result;
    }

    public static CascadeModel getSelectedDistrictModel(CascadeModel cityModel, String districtId) {
        CascadeModel result = null;
        if (cityModel != null) {
            for (CascadeModel districtModel : cityModel.childrens) {
                if (districtModel.id.equals(districtId)) {
                    result = districtModel;
                }
            }
        }
        return result;
    }

    public static String getCompleteAddress(List<CascadeModel> provinceList, String provinceId, String cityId, String districtId) {
        CascadeModel provinceMode = getSelectedProvinceModel(provinceList, provinceId);
        String result = provinceMode.name;
        if (!TextUtils.isEmpty(cityId)) {
            CascadeModel cityMode = getSelectedCityModel(provinceMode, cityId);
            if (cityMode != null) {
                CascadeModel districtModel = getSelectedDistrictModel(cityMode, districtId);
                String districtName = districtModel == null ? "" : districtModel.name;
                result = provinceMode.name + cityMode.name + districtName;
            }
        }
        return result;
    }
}