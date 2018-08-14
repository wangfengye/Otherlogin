package com.maple.wangfeng.otherlogin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    public void onSuccess(String info) {
        if (WeChatUtil.getInstance().getLoginCallback() != null)
            WeChatUtil.getInstance().getLoginCallback().onSuccess(info);
    }

    public void onError(String msg) {
        if (WeChatUtil.getInstance().getLoginCallback() != null)
            WeChatUtil.getInstance().getLoginCallback().onError(msg);
    }

    public int WX_LOGIN = 1;

    private IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        iwxapi = WeChatUtil.getInstance().getApi();
        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi.handleIntent(getIntent(), this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    public void onReq(BaseReq baseReq) {
    }


    //请求回调结果处理
    @Override
    public void onResp(BaseResp baseResp) {
        //微信登录为getType为1，分享为0
        if (baseResp.getType() == WX_LOGIN) {
            //登录回调
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = String.valueOf(resp.code);
                    //获取用户信息
                    getAccessToken(code);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝授权
                    onError("用户拒绝授权");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    onError("用户取消");
                    break;
                default:
                    onError("未知问题");
                    break;
            }
        } else {
            //分享成功回调
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    //分享成功
                    onSuccess("分享成功");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    //分享取消
                    onError("分享取消");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    //分享拒绝
                    onError("分享拒绝");
                    break;
            }
        }
    }

    private void getAccessToken(String code) {
        //获取授权

        HttpClient.getInstance().getAccessToken(WeChatUtil.APP_ID, WeChatUtil.APP_SECRET, code, "authorization_code")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String access = jsonObject.getString("access_token");
                        String openId = jsonObject.getString("openid");
                        return HttpClient.getInstance().getUserInfo(access, openId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onError("JSON解析异常:" + s);
                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        WXEntryActivity.this.onSuccess(s);
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        WXEntryActivity.this.onError(e.getMessage());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
