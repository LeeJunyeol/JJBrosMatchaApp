package com.matcha.jjbros.matchaapp.owner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyInfoView extends LinearLayout {

    public MyInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
