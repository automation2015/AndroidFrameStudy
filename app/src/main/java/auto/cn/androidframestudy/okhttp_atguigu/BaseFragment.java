package auto.cn.androidframestudy.okhttp_atguigu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Fragment 的基类
 */
public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=initView();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    /**
     * 强制子类重写，实现子类特有的UI
     * @return
     */
    protected abstract View initView() ;

    /**
     * 由子类根据自己的需求重写该方法，用于初始化数据，联网获取数据等
     */
    protected void initData(){};
}
