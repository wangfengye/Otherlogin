
# Otherlogin

![avatar](https://img.shields.io/badge/Otherlogin-v0.9-green.svg)

> 本库的目的是简化接入微信登录,微信分享的操作.

## Download

Project gradle添加
```  
maven { url "https://jitpack.io" }
 ```
               
               
 moudle gradle 添加
```
    implementation 'com.github.wangfengye.Otherlogin:app:v0.9'
    annotationProcessor 'com.github.wangfengye.Otherlogin:maple-compiler:v0.9'

```
manifest 添加
```
<activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:label="wechatCallback"
            android:launchMode="singleTop"
            android:theme="@style/Theme.AppCompat.Translucent">
        </activity>

```

## Use
初始化
```
 WeChatUtil.init(appid,appSecret,
                   this.getApplicationContext());
```
       
 登录
 
```
  WeChatUtil.getInstance().onCallback(new IWeChatCallback() {
             @Override
             public void onSuccess(String info) {
               // 授权成功,info:微信个人信息的json
             }
 
             @Override
             public void onError(String msg) {
                // 授权失败,msg:错误信息
             }
         }).login();
```
分享
```
  WeChatUtil.getInstance().onCallback(new IWeChatCallback() {
            @Override
            public void onSuccess(String info) {
                // 分享成功
            }
        }).share();
```
