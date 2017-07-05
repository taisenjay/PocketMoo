package com.taisenjay.pocketmoo.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.taisenjay.pocketmoo.R;
import com.taisenjay.pocketmoo.utils.DensityUtil;
import com.wefika.flowlayout.FlowLayout;

import java.util.List;

/**
 * Author : WangJian
 * Date   : 2017/7/2
 * Created by a handsome boy with love
 */

public class SimpleTextHeader extends FlowLayout {
    public SimpleTextHeader(Context context) {
        super(context);
        init(context);
    }

    public SimpleTextHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleTextHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.simple_header, this);
    }

    public void putTexts(List<String> texts,Context context){
        for(String string : texts){
            TextView textView = new TextView(context);
            textView.setText(string);
//            textView.setTextColor(0xffffff);
            textView.setPadding(DensityUtil.dip2px(context,10),0, DensityUtil.dip2px(context,5),0);
            addView(textView);
        }
    }

}
