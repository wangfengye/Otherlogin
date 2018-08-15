# Otherlogin
本库的目的是简化接入微信登录,微信分享的操作.

## Download

Project gradle添加
```  maven {
                   url "https://jitpack.io"
               }
 ```
               
               
 moudle gradle 添加
```
    implementation 'com.github.wangfengye.Otherlogin:app:v0.8'
    annotationProcessor 'com.github.wangfengye.Otherlogin:maple-compiler:v0.8'

```
mianfesst 添加
```
<activity
            android:name=".wxapi.WXEntryActivity"
            android:exported="true"
            android:label="wechatCallback"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.AppCompat.Translucent">
        </activity>

```

## Use
初始化
```
 WeChatUtil.init("wxe033521045c83e75","cce2c8d2028931062f38b8061a224d38",
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