package com.matcha.jjbros.matchaapp.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by jylee on 2016-08-19.
 */
// 스크롤 뷰 겹칠 때, 속에 있는 거 터치할 수 있게 해줌
public class NoInterceptScrollView extends ScrollView {

    public NoInterceptScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}