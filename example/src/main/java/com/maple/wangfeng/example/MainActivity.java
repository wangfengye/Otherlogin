package com.maple.wangfeng.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maple.wangfeng.annotations.EntryGenerator;
import com.maple.wangfeng.otherlogin.WXEntryActivity;
import com.maple.wangfeng.otherlogin.WeChatUtil;

@EntryGenerator(
        packageName = "com.maple.wangfeng.example",
        entryTemplete = WXEntryActivity.class
)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
