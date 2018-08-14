package com.maple.wangfeng.otherlogin;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.maple.wangfeng.otherlogin.callback.IWeChatCallback;
import com.maple.wangfeng.otherlogin.callback.IWeChatCallback;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by xmg on 2016/11/29.
 */

public class WeChatUtil {
    public static String APP_ID;
    public static String APP_SECRET;
    public static final int FRIEND = 0;  //分享好友
    public static final int MOMENT = 1;  //分享朋友圈
    private static IWXAPI iwxapi;
    private IWeChatCallback mLoginCallback;
    private static Context mContext;

    /**
     * use in Application;
     *
     * @param appId
     * @param appSecret
     */
    public static void init(String appId, String appSecret, Context context) {
        APP_ID = appId;
        APP_SECRET = appSecret;
        mContext = context;
    }

    public WeChatUtil onCallback(IWeChatCallback chatCallback) {
        this.mLoginCallback = chatCallback;
        return this;
    }

    public static WeChatUtil getInstance() {
        return Holder.INSTANCE;
    }


    /**
     * 微信登录
     */
    public void login() {
        if (!judgeCanGo()) {
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        //授权域 获取用户个人信息则填写snsapi_userinfo
        req.scope = "snsapi_userinfo";
        //用于保持请求和回调的状态 可以任意填写
        req.state = "test_login";
        iwxapi.sendReq(req);
    }

    /**
     * 分享文本至朋友圈
     *
     * @param text  文本内容
     * @param judge 类型选择 好友-WECHAT_FRIEND 朋友圈-WECHAT_MOMENT
     */
    public void share(String text, int judge) {
        if (!judgeCanGo()) {
            return;
        }
        //初始化WXTextObject对象，填写对应分享的文本内容
        WXTextObject textObject = new WXTextObject();
        textObject.text = text;
        //初始化WXMediaMessage消息对象，
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = textObject;
        message.description = text;
        //构建一个Req请求对象
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());   //transaction用于标识请求
        req.message = message;
        req.scene = judge;      //分享类型 好友==0 朋友圈==1
        //发送请求
        iwxapi.sendReq(req);
    }

    /**
     * 分享图片
     *
     * @param bitmap 图片bitmap,建议别超过32k
     * @param judge  类型选择 好友-WECHAT_FRIEND 朋友圈-WECHAT_MOMENT
     */
    public void share(Bitmap bitmap, int judge) {
        if (!judgeCanGo()) {
            return;
        }
        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = wxImageObject;

        Bitmap thunmpBmp = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        bitmap.recycle();
        message.thumbData = Util.bmpToByteArray(thunmpBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        req.scene = judge;

        iwxapi.sendReq(req);

    }

    /**
     * 网页分享
     *
     * @param url         地址
     * @param title       标题
     * @param description 描述
     * @param bitmap      缩略图 不能超过32k
     * @param judge       类型选择 好友-WECHAT_FRIEND 朋友圈-WECHAT_MOMENT
     */
    public void share(String url, String title, String description, Bitmap bitmap, int judge) {
        if (!judgeCanGo()) {
            return;
        }
        WXWebpageObject wxWebpageObject = new WXWebpageObject();
        wxWebpageObject.webpageUrl = url;

        WXMediaMessage wxMediaMessage = new WXMediaMessage(wxWebpageObject);
        wxMediaMessage.title = title;
        wxMediaMessage.description = description;
        Bitmap thunmpBmp = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
        bitmap.recycle();
        wxMediaMessage.thumbData = Util.bmpToByteArray(thunmpBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = wxMediaMessage;
        req.scene = judge;

        iwxapi.sendReq(req);
    }

    public IWeChatCallback getLoginCallback() {
        return mLoginCallback;
    }

    private static class Holder {
        static WeChatUtil INSTANCE = new WeChatUtil();
    }

    public static IWXAPI getApi() {
        if (iwxapi == null) {
            //通过WXAPIFactory创建IWAPI实例
            iwxapi = WXAPIFactory.createWXAPI(mContext, APP_ID, true);
            //将应用的appid注册到微信
            iwxapi.registerApp(APP_ID);
        }
        return iwxapi;
    }

    /**
     * 判断微信是否可用
     *
     * @return
     */
    private boolean judgeCanGo() {
        getApi();
        if (!iwxapi.isWXAppInstalled()) {
            Log.e("Wechat", "judgeCanGo: 请先安装微信应用");
            if (mLoginCallback != null) mLoginCallback.onError("请先安装微信应用");
            return false;
        } else if (!iwxapi.isWXAppSupportAPI()) {
            Log.e("Wechat", "judgeCanGo: 请先更新微信应用");
            if (mLoginCallback != null) mLoginCallback.onError("请先安装微信应用");
            return false;
        }
        return true;
    }
}
