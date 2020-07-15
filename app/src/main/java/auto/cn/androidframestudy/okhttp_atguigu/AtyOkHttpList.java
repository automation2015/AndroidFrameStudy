package auto.cn.androidframestudy.okhttp_atguigu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import auto.cn.androidframestudy.R;
import auto.cn.androidframestudy.utils.CommonBaseAdapter;
import auto.cn.androidframestudy.utils.ViewHolder;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;

public class AtyOkHttpList extends Activity {

    @Bind(R.id.lv_get_image)
    ListView lvGetImage;
    @Bind(R.id.pb_get_image)
    ProgressBar pbGetImage;
    @Bind(R.id.tv_get_image)
    TextView tvGetImage;
    private static final String TAG = AtyOkHttpList.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_okhttp_list);
        ButterKnife.bind(this);
        getDataFromNet();
    }

    private void getDataFromNet() {
        // url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        //url="http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        String url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }
    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id) {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id) {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            e.printStackTrace();
            tvGetImage.setVisibility(View.VISIBLE);
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            tvGetImage.setVisibility(View.GONE);

            switch (id) {
                case 100:
                    Toast.makeText(AtyOkHttpList.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(AtyOkHttpList.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
            if(response!=null){
                //解析数据
                processDatas(response);
            }
        }
    }
/**
 * 解析和显示数据
 */
    private void processDatas(String response) {
        DataBean dataBean = parsedJson(response);
        final List<DataBean.TrailersBean> trailers = dataBean.getTrailers();
        if(trailers!=null&&trailers.size()>0){
            //有数据
            tvGetImage.setVisibility(View.GONE);
            lvGetImage.setAdapter(new CommonBaseAdapter<DataBean.TrailersBean>(AtyOkHttpList.this,trailers,R.layout.item_okhttp_list_image) {
                @Override
                public void convert(ViewHolder holder, DataBean.TrailersBean trailersBean) {
                    holder.setText(R.id.tv_name,trailersBean.getMovieName()).setText(R.id.tv_desc,trailersBean.getVideoTitle())
                            .setImageUrl(R.id.iv_icon, Uri.parse(trailersBean.getCoverImg()));
                }
            });

        }else{
            //没有数据
            tvGetImage.setVisibility(View.VISIBLE);
        }
        pbGetImage.setVisibility(View.GONE);
    }

    /**
     * 解析json数据
     * @param response
     * @return
     */
    private DataBean parsedJson(String response) {
        DataBean dataBean = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if (jsonArray != null && jsonArray.length() > 0) {
                List<DataBean.TrailersBean> trailers = new ArrayList<>();
                dataBean.setTrailers(trailers);
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                    if (jsonObjectItem != null) {

                        DataBean.TrailersBean mediaItem = new DataBean.TrailersBean();

                        String movieName = jsonObjectItem.optString("movieName");//name
                        mediaItem.setMovieName(movieName);

                        String videoTitle = jsonObjectItem.optString("videoTitle");//desc
                        mediaItem.setVideoTitle(videoTitle);

                        String imageUrl = jsonObjectItem.optString("coverImg");//imageUrl
                        mediaItem.setCoverImg(imageUrl);

                        String hightUrl = jsonObjectItem.optString("hightUrl");//data
                        mediaItem.setHightUrl(hightUrl);

                        //把数据添加到集合
                        trailers.add(mediaItem);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataBean;
    }
}

