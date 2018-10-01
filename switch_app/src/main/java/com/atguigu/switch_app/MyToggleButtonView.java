package com.atguigu.switch_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xinpengfei on 2016/9/23.
 * * 作用：自定义开关按钮
 *
 * 一个View从创建到显示屏幕主要调用到的方法
 * 1.测量-measure()-->onMeasure();
 * 2.指定视图的位置和大小layout->onLayout();
 *  如果当前控件是普通的View,该方法一般不用实现；但是该View是ViewGroup，就一定要实现，需要指定孩子的位置和大小
 * 3.绘制draw-->onDraw();最终把控件绘制在屏幕上
 */

public class MyToggleButtonView extends View implements View.OnClickListener {

    private static final String TAG = MyToggleButtonView.class.getSimpleName();
    private Bitmap backgroundBitmap;
    private Bitmap slidingBitmap;

    //两张图片的最大差值
    private int slidingLeftMax;
    private Paint paint;//画笔

    //动态显示左边距离的参数
    private int slidingLeft;

    /**
     * 在代码中创建实例的时候使用
     * @param context
     */
    public MyToggleButtonView(Context context) {
        this(context,null);
    }

    /**
     * 当要设置样式的时候可以调用这个方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyToggleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * Android系统规定，当在布局文件xml中定义，会通过这个构造方法实例化这个类,如果缺少，将会崩溃
     * @param context
     * @param attrs
     */
    public MyToggleButtonView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    private void initView() {

        //创建画笔
        paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slidingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        slidingLeftMax = backgroundBitmap.getWidth() - slidingBitmap.getWidth();
        //设置点击事件
        setOnClickListener(this);
    }

    /**
     * 测量得到控件的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(backgroundBitmap.getWidth(),backgroundBitmap.getHeight());

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(backgroundBitmap,0,0,paint);
        canvas.drawBitmap(slidingBitmap,slidingLeft,0,paint);
    }

    /**
     * 按钮是否打开
     * true:按钮打开
     * false:按钮关闭
     */
    private boolean isOpen = true;

    /**
     * true:点击事件生效，触摸事件失效
     * false:点击事件失效，触摸事件生效
     */
    private boolean isClickEnable = true;

    @Override
    public void onClick(View v) {

        if(isClickEnable) {

            isOpen = !isOpen;
            flushView();
            Log.e("TAG", "onClick==");
            
        }
    }

    private void flushView() {
        if(isOpen) {
            //显示开的状态
            slidingLeft = slidingLeftMax;

        }else {
            //显示关的状态
            slidingLeft  = 0;
        }

        //重新绘制
        invalidate();//会导致onDraw 方法执行，在主线程
//        postInvalidate();//在子线程中执行

    }
    private float startX;
    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                isClickEnable  = true;
                Log.e("TAG", "onTouchEvent == ACTION_DOWN");

                //1.记录按下的坐标
                lastX = startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE :
                Log.e("TAG", "onTouchEvent == ACTION_MOVE");
                //2.来到新的坐标
                float endX = event.getX();
                //3.计算偏移量
                float distanceX = endX - startX;
                //判断是否是触摸事件
                if(Math.abs(event.getX() - lastX) > 5) {
                    isClickEnable = false;
                }
                //4.动态改变slidingLeft
//                slidingLeft = (int) (slidingLeft + distanceX);
                slidingLeft += distanceX;

                //5.屏蔽 非法值
                if(slidingLeft < 0 ) {
                    slidingLeft = 0;
                }
                if(slidingLeft > slidingLeftMax) {
                    slidingLeft  = slidingLeftMax;
                }
                //6.重新绘制
                invalidate();//会导致onDraw
                //7.重新记录起始坐标
                startX  = event.getX();

               break;
            case MotionEvent.ACTION_UP :

                if(!isClickEnable) {
                    Log.e("TAG", "onTouchEvent== ACTION_UP");
                    if(slidingLeft > slidingLeftMax/2) {
                        isOpen = true;
                    }else {
                        isOpen = false;
                    }

                    flushView();
                }
                break;
        }

        return true;
    }
}
