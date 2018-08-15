# Bug Log

* mainfest 配置activity `android:screenOrientation="portrait"`属性导致微信登录失效
> cause: wxentryActivity不支持 screenOreientation属性,导致部分手机无法使用微信登录,删除该属性即可