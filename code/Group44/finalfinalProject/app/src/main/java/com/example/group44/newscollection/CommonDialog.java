package com.example.group44.newscollection;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommonDialog extends Dialog{

    private Context mContext;

    public CommonDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disliketagdialog);
        setCanceledOnTouchOutside(false);
        Button dislikeOkbutton = findViewById(R.id.dislikeOkButton);
        dislikeOkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}