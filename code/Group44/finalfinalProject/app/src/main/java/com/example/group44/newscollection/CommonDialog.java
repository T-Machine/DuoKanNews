package com.example.group44.newscollection;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CommonDialog extends Dialog{

    private Context mContext;
    private ArrayList<String> favWords = new ArrayList<>();
    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    CheckBox tv1, tv2, tv3, tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disliketagdialog);
        setCanceledOnTouchOutside(false);
        Button dislikeOkbutton = findViewById(R.id.dislikeOkButton);
        tv1 = findViewById(R.id.dislikeitem1);
        tv2 = findViewById(R.id.dislikeitem2);
        tv3 = findViewById(R.id.dislikeitem3);
        tv4 = findViewById(R.id.dislikeitem4);
        for(int i = 0; i < favWords.size(); i++){
            if(i == 0) release1(favWords.get(0));
            else if(i == 1) release2(favWords.get(1));
            else if(i == 2) release3(favWords.get(2));
            else if(i == 3) release4(favWords.get(3));
        }
        dislikeOkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void getMessage(ArrayList<String> favWords){
        this.favWords = favWords;
    }

    // 设置文本
    public void release1(String str){
        tv1.setVisibility(View.VISIBLE);
        tv1.setText(str);
    }
    public void release2(String str){
        tv2.setVisibility(View.VISIBLE);
        tv2.setText(str);
    }
    public void release3(String str){
        tv3.setVisibility(View.VISIBLE);
        tv3.setText(str);
    }

    public void release4(String str){
        tv4.setVisibility(View.VISIBLE);
        tv4.setText(str);
    }
}