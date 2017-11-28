package com.example.xin.sendjsondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button sendJsonBt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendJsonBt = (Button) findViewById(R.id.sendJson_Bt);
        sendJsonBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpPostJSON();//异步方式请求数据，不需要手动创建线程，需要处理回调
                //sendRequestWithOkHttp();//同步方式请求数据，手动创建新线程
            }
        });
    }

    private void sendRequestWithOkHttp(){
                try{
                    //以下为测试代码
                    User testUser = new User();
                    testUser.setUsername("张健欣");
                    testUser.setPassword("123456");
                    testUser.setEmail("1070386906@qq.com");
                    testUser.setPhone("12345678910");
                    //以上为测试代码
                    Gson gson = new Gson();
                    String jsonUser = gson.toJson(testUser);
                    MediaType JSON = MediaType.parse("application/json;charset=utf-8");

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON,jsonUser);
                    Request request = new Request.Builder()
                            .url("http://10.242.23.104:8080/UserServlet")
                            .post(body)
                            .build();
                    final Response response = client.newCall(request).execute();
                    final String responseData = response.body().toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                Toast.makeText(MainActivity.this,responseData,Toast.LENGTH_LONG)
                                        .show();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
    }

    public void httpPostJSON(){
        //以下为测试代码
        User testUser = new User();
        testUser.setUsername("张健欣");
        testUser.setPassword("123456");
        testUser.setEmail("1070386906@qq.com");
        testUser.setPhone("12345678910");
        //以上为测试代码
        Gson gson = new Gson();
        String jsonUser = gson.toJson(testUser);
        MediaType JSON = MediaType.parse("application/json;charset=utf-8");
        String url = "http://10.243.6.27:8080/UserServlet";//服务器地址
        OkHttpClient client = new OkHttpClient();//创建okhttp实例
        RequestBody body = RequestBody.create(JSON,jsonUser);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);//以request为参数创建一个call实例
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //请求失败时调用,若要更新UI界面，需在runOnUIThread方法中
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"请求数据失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //请求成功时调用
                final String res = response.body().string();//将服务器返回的数据解析为字符串
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"请求数据完毕：" + res,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
