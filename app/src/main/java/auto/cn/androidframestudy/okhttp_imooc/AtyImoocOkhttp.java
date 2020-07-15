package auto.cn.androidframestudy.okhttp_imooc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import auto.cn.androidframestudy.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 1.拿到okHttpClient对象；
 * 2.构造Request
 * 2.1构造requestBody
 * 2.2包装requestBody
 * 3.Call->execute
 */
public class AtyImoocOkhttp extends Activity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_do_get)
    Button btnDoGet;
    @Bind(R.id.btn_do_post)
    Button btnDoPost;
    @Bind(R.id.btn_do_post_string)
    Button btnDoPostString;
    @Bind(R.id.btn_do_post_file)
    Button btnDoPostFile;
    @Bind(R.id.btn_do_post_uploadfile)
    Button btnDoPostUploadfile;
    @Bind(R.id.btn_do_download)
    Button btnDoDownload;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_result)
    TextView tvResult;
    @Bind(R.id.btn_do_download_img)
    Button btnDoDownloadImg;
    //1.拿到okHttpClient对象；
    private OkHttpClient okHttpClient;
    private static final String TAG = "imooc";
    private String mBaseUrl = "http://172.16.36.92:8080/Imooc_okhttp1/";
    public static final MediaType USERNAME
            = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_imooc_okhttp);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        tvTitle.setText("慕课OkHttp学习");
        okHttpClient = new OkHttpClient();
    }

    /**
     * get请求
     */

    @OnClick(R.id.btn_do_get)
    public void doGet() {
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //3.将Request封装为Call
        Request request = builder.get()
                .url(mBaseUrl + "login?username=hyman&password=123456")
                .build();
        //4.执行Call
        try {
            excuteCall(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //call.execute();//同步任务
    }

    @OnClick(R.id.btn_do_post)
    public void doPost() {
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //3.将Request封装为Call
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("username", "hyman");
        formBuilder.add("password", "0000");
        RequestBody requestBody = formBuilder.build();
        Request request = builder.post(requestBody)
                .url(mBaseUrl + "login")
                .build();
        //4.执行Call
        try {
            excuteCall(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post 上传String
     */
    @OnClick(R.id.btn_do_post_string)
    public void doPostString() {

        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //3.将Request封装为Call

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain charset=utf-8"), "{username:hyman,password:1234567}");
        Request request = builder.post(requestBody)
                .url(mBaseUrl + "postString")
                .build();
        //4.执行Call
        try {
            excuteCall(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * post 上传文件
     */

    @OnClick(R.id.btn_do_post_file)
    public void doPostFile() {

        //3.将Request封装为Call
        File file = new File(Environment.getExternalStorageDirectory(), "snowman.png");
        if (!file.exists()) {
            Log.d(TAG, "postFile() called" + file.getAbsolutePath() + "not exist!");
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        Request.Builder builer = new Request.Builder();
        Request request = builer.url(mBaseUrl + "postFile")
                .post(requestBody)
                .build();
        //4.执行Call
        try {
            excuteCall(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
//        //mime type

    }

    /**
     * post 上传文件和参数
     */
    @OnClick(R.id.btn_do_post_uploadfile)
    public void doUploadFile() {
        File file = new File(Environment.getExternalStorageDirectory(), "snowman.png");
        if (!file.exists()) {
            Log.d(TAG, "postFile() called" + file.getAbsolutePath() + "not exist!");
            return;
        }
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        RequestBody requestBody = multipartBuilder
                .setType(MultipartBody.FORM)//不要忘记
                .addFormDataPart("username", "hyman")
                .addFormDataPart("password", "321")
                //mPhoto 表单域   "snowman1.png"服务器端的文件名称
                .addFormDataPart("mPhoto", "snowman1.png", RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onRequestProgress(long byteWrited, long contentLength) {
             Log.e(TAG,byteWrited+"/"+contentLength);
            }
        });
        Request.Builder builer = new Request.Builder();
        Request request = builer.url(mBaseUrl + "uploadInfo")
                .post(countingRequestBody)
                .build();
        //4.执行Call
        try {
            excuteCall(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载图片文件
     */
    @OnClick(R.id.btn_do_download)
    public void doPostDownloadFile() {

        Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url(mBaseUrl + "files/snowman.png")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final long fileTotal = response.body().contentLength();//文件总长度
                long sum=0L;

                InputStream is = response.body().byteStream();
                int len = 0;
                byte[] buff = new byte[1024];
                FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "snowman1.png"));
                while ((len = is.read(buff)) != -1) {
                    fos.write(buff, 0, len);
                    sum+=len;
                    Log.e(TAG,sum+"/"+fileTotal);

                }
                final long finalSum = sum;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(finalSum +"/"+fileTotal);
                    }
                });
                fos.flush();
                fos.close();
                is.close();
                Log.e(TAG, "download succ");
            }
        });

    }

    /**
     * 下载图片文件
     */
    @OnClick(R.id.btn_do_download_img)
    public void doPostDownloadImg() {
        Request.Builder builder = new Request.Builder();
        Request request = builder.get()
                .url(mBaseUrl + "files/snowman.png")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream is = response.body().byteStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(is);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ivIcon.setImageBitmap(bitmap);
                    }
                });
                is.close();
                Log.e(TAG, "download succ");
            }
        });

    }

    private void excuteCall(Request request) throws IOException {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure() called with: call = [" + call + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                final String res = response.body().string();
                Log.d(TAG, "onResponse() called with: res = [" + res + "]");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvResult.setText(res);
                    }
                });
            }
        });
    }
}
