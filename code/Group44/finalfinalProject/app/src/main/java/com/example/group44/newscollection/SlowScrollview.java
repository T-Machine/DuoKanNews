package com.example.group44.newscollection;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

// 阻尼调大
public class SlowScrollview extends ScrollView {
    public SlowScrollview(Context context, AttributeSet attrs, int defstyle) {
        super(context, attrs, defstyle);
    }
    public SlowScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public SlowScrollview(Context context) {
        super(context);
    }

    @Override
    public void fling(int velocityy) {
        super.fling(velocityy / 9);
    }

}
