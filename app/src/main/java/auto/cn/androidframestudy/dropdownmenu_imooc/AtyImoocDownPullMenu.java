package auto.cn.androidframestudy.dropdownmenu_imooc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import auto.cn.androidframestudy.R;

public class AtyImoocDownPullMenu extends Activity implements AdapterView.OnItemClickListener {
    DropDownMemu dropDownMemu;
    private String headers[] = {"城市", "年龄", "性别", "星座"};
    private List<View> popViews = new ArrayList<View>();
    private String citys[] = {"不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆", "天津", "西安", "南京", "杭州"};
    private String ages[] = {"不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁", "35岁以上"};
    private String sexs[] = {"不限", "男", "女"};
    private String constellations[] = {"不限", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座"};
    private int[] imagIds = {R.drawable.guide_image2, R.drawable.guide_image3, R.drawable.guide_image4,R.drawable.guide_image1};
    private int constellationPosition = 0;
    private GridDropDownAdapter cityAdapter;
    private ListDropDownAdapter ageAdapter, sexAdapter;
    private ConstellationAdapter constellationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_imooc_drop_down_menu);
        dropDownMemu = findViewById(R.id.dropDownMenu);
        initViews();
    }

    private void initViews() {
        ListView lvCity = new ListView(this);
        cityAdapter = new GridDropDownAdapter(this, Arrays.asList(citys));
        lvCity.setDividerHeight(0);
        lvCity.setId(R.id.list1);
        lvCity.setAdapter(cityAdapter);

        ListView lvAge = new ListView(this);
        ageAdapter = new ListDropDownAdapter(this, Arrays.asList(ages));
        lvAge.setDividerHeight(0);
        lvAge.setId(R.id.list2);
        lvAge.setAdapter(ageAdapter);

        ListView lvSex = new ListView(this);
        sexAdapter = new ListDropDownAdapter(this, Arrays.asList(sexs));
        lvSex.setDividerHeight(0);
        lvSex.setId(R.id.list3);
        lvSex.setAdapter(sexAdapter);

        View constellationView = getLayoutInflater().inflate(R.layout.item_drop_down_constellation, null);
        GridView gView = constellationView.findViewById(R.id.constellation);
        TextView tvOk = constellationView.findViewById(R.id.tv_ok);
        constellationAdapter = new ConstellationAdapter(this, Arrays.asList(constellations));

        gView.setAdapter(constellationAdapter);

        lvAge.setOnItemClickListener(this);
        lvCity.setOnItemClickListener(this);
        lvSex.setOnItemClickListener(this);
        gView.setOnItemClickListener(this);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMemu.setTabText(constellationPosition==0?headers[3]:constellations[constellationPosition]);
                dropDownMemu.setImageResource(imagIds[3]);
                dropDownMemu.closeMenu();
            }
        });
        popViews.add(lvCity);
        popViews.add(lvAge);
        popViews.add(lvSex);
        popViews.add(constellationView);

        ImageView contentView = new ImageView(this);
        contentView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        contentView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        dropDownMemu.setDropDownMenu(Arrays.asList(headers), popViews, contentView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.list1://city
                cityAdapter.setCheckItem(position);
                dropDownMemu.setTabText(position==0?headers[0]:citys[position]);
                dropDownMemu.setImageResource(imagIds[0]);
                dropDownMemu.closeMenu();
                break;
            case R.id.list2://age
                ageAdapter.setCheckItem(position);
                dropDownMemu.setTabText(position==0?headers[1]:ages[position]);

                dropDownMemu.setImageResource(imagIds[1]);
                dropDownMemu.closeMenu();
                break;
            case R.id.list3://sex
                sexAdapter.setCheckItem(position);
                dropDownMemu.setTabText(position==0?headers[2]:sexs[position]);
                dropDownMemu.setImageResource(imagIds[2]);
                dropDownMemu.closeMenu();
                break;
            case R.id.constellation://星座
                constellationAdapter.setCheckItem(position);
                constellationPosition=position;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(dropDownMemu.isShowing()){
            dropDownMemu.closeMenu();
        }else{
        super.onBackPressed();}
    }
}
