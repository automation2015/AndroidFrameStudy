package auto.cn.androidframestudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import auto.cn.androidframestudy.base.BaseFragment;
import auto.cn.androidframestudy.fragments.CommonFrameFragment;
import auto.cn.androidframestudy.fragments.CustomFragment;
import auto.cn.androidframestudy.fragments.OtherFragment;
import auto.cn.androidframestudy.fragments.ThirdPartyFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.fl_content)
    FrameLayout flContent;
    @Bind(R.id.rb_common_frame)
    RadioButton rbCommonFrame;
    @Bind(R.id.rb_thirdparty)
    RadioButton rbThirdparty;
    @Bind(R.id.rb_custom)
    RadioButton rbCustom;
    @Bind(R.id.rb_other)
    RadioButton rbOther;
    @Bind(R.id.rg_main)
    RadioGroup rgMain;
    private List<BaseFragment> mBaseFragment;
    private int position;//默认选中的Fragment的位置
    private Fragment mContent;//记录刚刚选中的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化Fragment
        initFragments();
        //设置RadioGroup的监听
        setListener();
    }

    private void setListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_common_frame:
                        position=0;
                        break;
                    case R.id.rb_thirdparty:
                        position=1;
                        break;
                    case R.id.rb_custom:
                        position=2;
                        break;
                    case R.id.rb_other:
                        position=3;
                        break;
                        default:
                            position=0;
                            break;
                }
                //根据位置得到对应Fragment
                BaseFragment to=getFragment();
                //替换
                //switchFragment(to);
                switchFragment(mContent,to);
            }
        });
rgMain.check(R.id.rb_common_frame);
    }

    /**
     *解决切换Fragement，重复初始化的问题
     * @param from 刚刚显示的Fragment，马上就要进行隐藏
     * @param to 马上要切换的Fragment，一会要显示
     */
 private void switchFragment(Fragment from,Fragment to){
     //1.得到FragmentManager
     FragmentManager fm = getSupportFragmentManager();
     //2.开启事务
     FragmentTransaction transaction = fm.beginTransaction();
        if(from!=to){
            mContent=to;
            //两个Fragment不相同，才进行切换
            //判断有没有被添加
            if(!to.isAdded()) {
                //to没有被添加
                //from隐藏
               if(from!=null){
                   transaction.hide(from);
               }
               if(to!=null){
                   //添加to
                   transaction.add(R.id.fl_content,to).commit();
               }

            }else {
                //to已经被添加
                //from隐藏
                if(from!=null){
                    transaction.hide(from);
                }
                if(to!=null){
                    //添加to
                  transaction.show(to).commit();
                }
                //from隐藏
                //显示to
            }
        }
 }
    private void switchFragment(BaseFragment fragment) {
        //1.得到FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        //2.开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        //3.替换
        transaction.replace(R.id.fl_content,fragment);
        //4.提交事务
        transaction.commit();
    }

    private BaseFragment getFragment() {
        BaseFragment fragment = mBaseFragment.get(position);
        return fragment;
    }

    private void initFragments() {

        mBaseFragment = new ArrayList<>();
        mBaseFragment.add(new CommonFrameFragment());
        mBaseFragment.add(new ThirdPartyFragment());
        mBaseFragment.add(new CustomFragment());
        mBaseFragment.add(new OtherFragment());
    }
}
