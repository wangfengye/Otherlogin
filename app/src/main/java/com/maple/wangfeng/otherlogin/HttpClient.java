package com.maple.wangfeng.otherlogin;

import com.maple.wangfeng.otherlogin.convert.ScalarsConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class HttpClient {
    private static final String BASE_URL = "https://api.weixin.qq.com/sns/";

    public static WeChatApi getInstance() {
        return Build.weChatApi;
    }

    private static class Build {

        public static WeChatApi weChatApi;

        static {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            weChatApi = retrofit.create(WeChatApi.class);
        }

    }
}


