package com.maple.wangfeng.otherlogin;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeChatApi {
    @GET("oauth2/access_token")
    Observable<String> getAccessToken(@Query("appid") String appId, @Query("secret") String secret,
                                      @Query("code") String code, @Query("grant_type") String grant_type);

    @GET("userinfo")
    Observable<String> getUserInfo(@Query("access_token") String accessToken, @Query("openid") String openId);
}
