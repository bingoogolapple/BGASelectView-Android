package cn.bingoogolapple.selectview.demo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.selectview.BGASelectView;
import cn.bingoogolapple.selectview.demo.R;
import cn.bingoogolapple.selectview.demo.model.CityModel;
import cn.bingoogolapple.selectview.demo.model.DistrictModel;
import cn.bingoogolapple.selectview.demo.model.ProvinceModel;
import cn.bingoogolapple.selectview.demo.util.AddressXmlParserHandler;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/3 11:12
 * 描述:
 */
public class MainActivity extends AppCompatActivity implements BGASelectView.SelectViewDelegate {
    private BGASelectView mProvinceSv;
    private BGASelectView mCitySv;
    private BGASelectView mDistrictSv;
    private TextView mAddressTv;

    private ProvinceAdapter mProvinceAdapter;
    private CityAdapter mCityAdapter;
    private DistrictAdapter mDistrictAdapter;
    private List<ProvinceModel> mProvinces;
    private List<CityModel> mCitys;
    private List<DistrictModel> mDistricts;
    private ProvinceModel mProvinceModel;
    private CityModel mCityModel;
    private DistrictModel mDistrictModel;

    private String mProvinceId = "0001";
    private String mCityId = "0100";
    private String mDistrictId = "0103";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProvinceSv = (BGASelectView) findViewById(R.id.sv_main_province);
        mCitySv = (BGASelectView) findViewById(R.id.sv_main_city);
        mDistrictSv = (BGASelectView) findViewById(R.id.sv_main_district);
        mAddressTv = (TextView) findViewById(R.id.tv_main_address);

        initAddress();
        initData();
    }

    private void initAddress() {
        mProvinceAdapter = new ProvinceAdapter(this);
        mProvinceSv.setActivity(this);
        mProvinceSv.setAdapter(mProvinceAdapter);
        mProvinceSv.setDelegate(this);

        mCityAdapter = new CityAdapter(this);
        mCitySv.setActivity(this);
        mCitySv.setAdapter(mCityAdapter);
        mCitySv.setDelegate(this);

        mDistrictAdapter = new DistrictAdapter(this);
        mDistrictSv.setActivity(this);
        mDistrictSv.setAdapter(mDistrictAdapter);
        mDistrictSv.setDelegate(this);
    }

    private void initData() {
        mProvinces = AddressXmlParserHandler.getProvinceList(this);
        mProvinceAdapter.setDatas(mProvinces);

        setProvinceModel(AddressXmlParserHandler.getSelectedProvinceModel(mProvinces, mProvinceId));
        setCityModel(AddressXmlParserHandler.getSelectedCityModel(mProvinceModel, mCityId));
        setDistrictModel(AddressXmlParserHandler.getSelectedDistrictModel(mCityModel, mDistrictId));
        handleAddressChanged();
    }

    @Override
    public void onSelectViewValueChanged(BGASelectView selectView, int position) {
        switch (selectView.getId()) {
            case R.id.sv_main_province:
                handleProvinceChanged(position);
                break;
            case R.id.sv_main_city:
                handleCityChanged(position);
                break;
            case R.id.sv_main_district:
                handleDistrictChanged(position);
                break;
            default:
                break;
        }
        handleAddressChanged();
    }

    private void handleProvinceChanged(int position) {
        if (mProvinces != null && mProvinces.size() > 0) {
            setProvinceModel(mProvinces.get(position));
        } else {
            setProvinceModel(null);
        }
        handleCityChanged(0);
    }

    private void handleCityChanged(int position) {
        if (mCitys != null && mCitys.size() > 0) {
            setCityModel(mCitys.get(position));
        } else {
            setCityModel(null);
        }
        handleDistrictChanged(0);
    }

    private void handleDistrictChanged(int position) {
        if (mDistricts != null && mDistricts.size() > 0) {
            setDistrictModel(mDistricts.get(position));
        } else {
            setDistrictModel(null);
        }
    }

    private void setProvinceModel(ProvinceModel provinceModel) {
        mProvinceModel = provinceModel;

        if (mProvinceModel != null) {
            mProvinceSv.setText(mProvinceModel.name);
            mCitys = mProvinceModel.cityList;
        } else {
            mProvinceSv.reset();
            mCitys = null;
        }
        mCityAdapter.setDatas(mCitys);
    }

    private void setCityModel(CityModel cityModel) {
        mCityModel = cityModel;

        if (mCityModel != null) {
            mCitySv.setText(mCityModel.name);
            mDistricts = mCityModel.districtList;
        } else {
            mCitySv.reset();
            mDistricts = null;
        }
        mDistrictAdapter.setDatas(mDistricts);
    }

    private void setDistrictModel(DistrictModel districtModel) {
        mDistrictModel = districtModel;
        if (mDistrictModel != null) {
            mDistrictSv.setText(mDistrictModel.name);
        } else {
            mDistrictSv.reset();
        }
    }

    private void handleAddressChanged() {
        mProvinceId = mProvinceModel == null ? null : mProvinceModel.id;
        mCityId = mCityModel == null ? null : mCityModel.id;
        mDistrictId = mDistrictModel == null ? null : mDistrictModel.id;
        mAddressTv.setText(AddressXmlParserHandler.getCompleteAddress(mProvinces, mProvinceId, mCityId, mDistrictId));
    }

    private static class ProvinceAdapter extends BGAAdapterViewAdapter<ProvinceModel> {

        public ProvinceAdapter(Context context) {
            super(context, R.layout.item_selectview);
        }

        @Override
        protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        }

        @Override
        protected void fillData(BGAViewHolderHelper viewHolderHelper, ProvinceModel provinceModel, int position) {
            viewHolderHelper.setText(R.id.tv_item_address_edit_name, provinceModel.name);
        }
    }

    private static class CityAdapter extends BGAAdapterViewAdapter<CityModel> {

        public CityAdapter(Context context) {
            super(context, R.layout.item_selectview);
        }

        @Override
        protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        }

        @Override
        protected void fillData(BGAViewHolderHelper viewHolderHelper, CityModel cityModel, int position) {
            viewHolderHelper.setText(R.id.tv_item_address_edit_name, cityModel.name);
        }
    }

    private static class DistrictAdapter extends BGAAdapterViewAdapter<DistrictModel> {

        public DistrictAdapter(Context context) {
            super(context, R.layout.item_selectview);
        }

        @Override
        protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        }

        @Override
        protected void fillData(BGAViewHolderHelper viewHolderHelper, DistrictModel districtModel, int position) {
            viewHolderHelper.setText(R.id.tv_item_address_edit_name, districtModel.name);
        }
    }

}