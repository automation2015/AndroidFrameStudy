package auto.cn.androidframestudy.okhttp_atguigu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import auto.cn.androidframestudy.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AtyOkHttp extends Activity {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_get)
    Button btnGet;
    @Bind(R.id.btn_post)
    Button btnPost;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.btn_get_utils)
    Button btnGetUtils;
    @Bind(R.id.btn_post_utils)
    Button btnPostUtils;
    @Bind(R.id.btn_download_file)
    Button btnDownloadFile;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.btn_upload_file)
    Button btnUploadFile;
    @Bind(R.id.btn_get_image)
    Button btnGetImage;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.btn_get_image_list)
    Button btnGetImageList;
    private OkHttpClient client = new OkHttpClient();
    private static final String TAG = AtyOkHttp.class.getName();
    private static final int GET = 1;
    private static final int POST = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET:
                    tvResult.setText("");
                    tvResult.setText((String) msg.obj);
                    break;
                case POST:
                    tvResult.setText("");
                    tvResult.setText((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_okhttp);
        ButterKnife.bind(this);
        tvTitle.setText("OkHttp");
    }

    //使用原生的okhttp请求网络数据，get和post

    @OnClick(R.id.btn_get)
    public void getRequest(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = get("http://api.m.mtime.cn/PageSubArea/TrailerList.api");
                    Log.d(TAG, "getAndPostRequest() called with: result = [" + result + "]");
                    Message msg = Message.obtain();
                    msg.what = GET;
                    msg.obj = result;

                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * post请求数据
     *
     * @param view
     */
    @OnClick(R.id.btn_post)
    public void postRequest(View view) {
        new Thread() {
            @Override
            public void run() {
                try {
                    String result = post("http://api.m.mtime.cn/PageSubArea/TrailerList.api", "");
                    Log.d(TAG, "postRequest() called with: result = [" + result + "]");
                    Message msg = Message.obtain();
                    msg.what = POST;
                    msg.obj = result;

                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //使用okhttp_utils 的get请求文本数据
    @OnClick(R.id.btn_get_utils)
    public void getByUtils(View view) {
        getByOkHttpUtil();
    }

    //使用okhttp_utils 的post请求文本数据
    @OnClick(R.id.btn_post_utils)
    public void postByUtils(View view) {
        postByOkHttpUtil();
    }

    //使用okhttp_utils 的下载大文件
    @OnClick(R.id.btn_download_file)
    public void downloadFileByUtils(View view) {
        downloadFile();
    }

    //使用okhttp_utils 的下载大文件
    @OnClick(R.id.btn_upload_file)
    public void upFileByUtils(View view) {
        multiFileUpload();
    }

    //使用okhttp_utils 的下请求单张图片
    @OnClick(R.id.btn_get_image)
    public void getImage(View view) {
        getImage();
    }

    //使用okhttp_utils 的下请求多张图片
    @OnClick(R.id.btn_get_image_list)
    public void getImageList(View view) {
        startActivity(new Intent(AtyOkHttp.this,AtyOkHttpList.class));
    }

    /**
     * 原生get 请求
     * @param url
     * @return
     * @throws IOException
     */
    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 原生post请求
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * 使用okhttp_utils 的get请求
     */
    public void getByOkHttpUtil() {
        String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        //url="http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .get()
                .url(url)
                .id(100)
                .build()
                .execute(new MyStringCallback());
    }

    /**
     * 使用okhttp_utils 的post请求
     */
    public void postByOkHttpUtil() {
        String url = "http://www.zhiyun-tech.com/App/Rider-M/changelog-zh.txt";
        //url="http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        url = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
        OkHttpUtils
                .post()
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
            tvResult.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e(TAG, "onResponse：complete");
            tvResult.setText("");
            tvResult.setText("onResponse:" + response);

            switch (id) {
                case 100:
                    Toast.makeText(AtyOkHttp.this, "http", Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(AtyOkHttp.this, "https", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e(TAG, "inProgress:" + progress);
            pb.setProgress((int) (100 * progress));
        }

    }

    /**
     * 使用okhttp_utils下载大文件
     */
    public void downloadFile() {
        //String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/okhttputils-2_4_1.jar?raw=true";
        String url = "https://vfx.mtime.cn/Video/2016/07/24/mp4/160724055620533327_480.mp4";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "okhttp_utils.mp4")//
                {
                    @Override
                    public void onBefore(Request request, int id) {
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        pb.setProgress((int) (100 * progress));
                        Log.e(TAG, "inProgress :" + (int) (100 * progress));
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError :" + e.getMessage());
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        Log.e(TAG, "onResponse :" + file.getAbsolutePath());
                        tvResult.setText(file.getAbsolutePath());
                    }
                });
    }

    /**
     * 使用okhttp_utils上传多个文件
     */
    //http://172.16.36.92:8080/FileUpload/index.jsp
    public void multiFileUpload() {
        File file = new File(Environment.getExternalStorageDirectory(), "snowman.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test.txt");
        if (!file.exists() || !file2.exists()) {
            Toast.makeText(AtyOkHttp.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "张鸿洋");
        params.put("password", "123");
        String mBaseUrl = "http://172.16.36.92:8080/FileUpload/FileUploadServlet";
        String url = mBaseUrl;

        OkHttpUtils.post()//
                .addFile("mFile", "service_snowman.png", file)//
                .addFile("mFile", "service_test.txt", file2)//
                .url(url)
                .params(params)//
                .build()//
                .execute(new MyStringCallback());
    }

    public void getImage() {
        tvTitle.setText("");
        String url = "http://images.csdn.net/20150817/1.jpg";
        OkHttpUtils
                .get()//
                .url(url)//
                .tag(this)//
                .build()//
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000)
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tvResult.setText("onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int id) {
                        Log.e("TAG", "onResponse：complete");
                        ivIcon.setImageBitmap(bitmap);
                    }
                });
    }

}
