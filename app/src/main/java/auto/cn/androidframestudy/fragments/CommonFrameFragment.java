package auto.cn.androidframestudy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import auto.cn.androidframestudy.R;
import auto.cn.androidframestudy.base.BaseFragment;
import auto.cn.androidframestudy.utils.CommonBaseAdapter;
import auto.cn.androidframestudy.utils.ViewHolder;
import butterknife.Bind;
import butterknife.ButterKnife;

public class CommonFrameFragment extends BaseFragment {
    private static final String TAG = CommonFrameFragment.class.getName();
    @Bind(R.id.lv_common_frame)
    ListView lvCommonFrame;
    private List<String> mDatas;
    private String[] datas;

    @Override
    protected View initView() {
        Log.e(TAG, "常用框架页面");
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_common_frame, null);
        return view;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.e(TAG, "常用框架页面");
        mDatas=new ArrayList<>();
        datas = new String[]{"OKHttp", "xUtils3", "Retrofit2", "Fresco", "Glide", "greenDao", "RxJava", "volley", "Gson", "FastJson", "picasso", "evenBus", "jcvideoplayer", "pulltorefresh", "Expandablelistview", "UniversalVideoView", "....."};
        for(int i=0;i<datas.length;i++){
            mDatas.add(datas[i]);
        }

        lvCommonFrame.setAdapter(new CommonBaseAdapter<String>(mContext,mDatas,R.layout.item_common_frame) {
            @Override
            public void convert(ViewHolder holder, String s) {
                holder.setText(R.id.tv_item_common_frame,s);
            }
        });
        lvCommonFrame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,"data=="+mDatas.get(position),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
