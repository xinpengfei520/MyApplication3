package com.atguigu.auto_attribute;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xinpengfei on 2016/9/23.
 * 作用：自定义属性类
 */

public class MyAttributeView  extends View{

    private Bitmap bitmap;
    private int ageint;
    private String nameStr;

    public MyAttributeView(Context context) {
        this(context,null);
    }

    public MyAttributeView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyAttributeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //得到属性有三种方式
        String name = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","name");
        String age = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","age");
        String bg = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","bg");

        //2.使用循环
        for (int i = 0; i< attrs.getAttributeCount();i++){
            System.out.println(attrs.getAttributeName(i)+"_"+attrs.getAttributeValue(i));

        }

        //3.使用系统工具
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MyAttributeView);
        for (int i = 0;i<array.getIndexCount();i++){
            int index = array.getIndex(i);//id
            switch (index) {
                case R.styleable.MyAttributeView_name :
                    nameStr = array.getString(index);
                    break;
                case R.styleable.MyAttributeView_age :
                    ageint  = array.getInt(index,0);
                    break;
                case R.styleable.MyAttributeView_bg :
                    Drawable drawable = array.getDrawable(index);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    bitmap = bitmapDrawable.getBitmap();
                    break;
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.RED);
        canvas.drawText(nameStr + ":" + ageint,0,20,paint);
        canvas.drawBitmap(bitmap,0,0,paint);
    }
}
