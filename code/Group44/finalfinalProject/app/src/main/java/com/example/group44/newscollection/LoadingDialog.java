package com.example.group44.newscollection;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class LoadingDialog extends Dialog{

    boolean flag = true;
    final Handler handler = new Handler();
    private Context mContext;
    TextView tv;
    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        tv = findViewById(R.id.loadingHint);


    }
    // 循环显示
    @Override
    public void show(){
        flag = true;
        new Thread(){
            @Override
            public void run(){
                while(flag){
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(tv.getText().equals("加载中...")){
                                tv.setText("加载中.");
                            } else if(tv.getText().equals("加载中.")){
                                tv.setText("加载中..");
                            } else{
                                tv.setText("加载中...");
                            }
                        }
                    });
                }
            }
        }.start();
        super.show();
    }
    @Override
    public void dismiss(){
        flag = false;
        super.dismiss();
    }
}