package cn.bingoogolapple.selectview.demo.engine;

import android.content.Context;
import android.text.TextUtils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import cn.bingoogolapple.selectview.CascadeModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/11 15:56
 * 描述:地址解析引擎
 */
public class AddressEngine extends DefaultHandler {
    private List<CascadeModel> provinceList = new ArrayList<>();
    private CascadeModel provinceModel;
    private CascadeModel cityModel;
    private CascadeModel districtModel;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
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

    public static List<CascadeModel> getProvinceList(Context context) {
        AddressEngine parserHandler = new AddressEngine();
        InputStream is = null;
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            is = context.getAssets().open("address_data.xml");
            parser.parse(is, parserHandler);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return parserHandler.provinceList;
    }

    public static CascadeModel getSelectedProvinceModel(List<CascadeModel> provinces, String provinceId) {
        CascadeModel result = null;
        if (provinces != null) {
            for (CascadeModel provinceModel : provinces) {
                if (provinceModel.id.equals(provinceId)) {
                    result = provinceModel;
                }
            }
        }
        return result;
    }

    public static CascadeModel getSelectedCityModel(CascadeModel provinceModel, String cityId) {
        CascadeModel result = null;
        if (provinceModel != null && provinceModel.childrens != null) {
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
        if (cityModel != null && cityModel.childrens != null) {
            for (CascadeModel districtModel : cityModel.childrens) {
                if (districtModel.id.equals(districtId)) {
                    result = districtModel;
                }
            }
        }
        return result;
    }

    public static String getCompleteAddress(List<CascadeModel> provinces, String provinceId, String cityId, String districtId) {
        CascadeModel provinceMode = getSelectedProvinceModel(provinces, provinceId);
        String result = "";
        if (provinceMode != null) {
            result = provinceMode.name;

            if (!TextUtils.isEmpty(cityId)) {
                CascadeModel cityMode = getSelectedCityModel(provinceMode, cityId);
                if (cityMode != null) {
                    CascadeModel districtModel = getSelectedDistrictModel(cityMode, districtId);
                    String districtName = districtModel == null ? "" : districtModel.name;
                    result = provinceMode.name + cityMode.name + districtName;
                }
            }
        }
        return result;
    }
}