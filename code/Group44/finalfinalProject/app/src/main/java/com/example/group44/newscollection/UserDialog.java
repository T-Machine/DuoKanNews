package com.example.group44.newscollection;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class UserDialog extends Dialog{

    private Context mContext;
    private MainActivity.editView callback;
    private ArrayList<String> favWords = new ArrayList<>();
    public UserDialog(Context context, MainActivity.editView e) {
        super(context);
        this.mContext = context;
        this.callback = e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usernamedialog);
        setCanceledOnTouchOutside(false);
        Button okbutton = findViewById(R.id.userOkButton);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = findViewById(R.id.usernameAgain);
                callback.edit(tv.getText().toString());
                dismiss();
            }
        });
    }

}