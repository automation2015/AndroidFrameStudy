package auto.cn.androidframestudy.fragments;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import auto.cn.androidframestudy.okhttp_atguigu.BaseFragment;

public class OtherFragment extends BaseFragment {
    private TextView tv;
    private static final String TAG=OtherFragment.class.getName();

    @Override
    protected View initView() {
        Log.e(TAG, "其他页面");
        tv=new TextView(mContext);
        tv.setTextSize(20);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.RED);
        return tv;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "其他页面");
        tv.setText("其他页面");
    }
}
