package com.atguigu.likeiphoneswitch_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xinpengfei on 2016/9/25.
 * <p>
 * 自定义控件的实现步骤 ：
 * 1)自定义一个类继承于View;
 * 2)测量-->重写onMeasure()
 * 3)指定视图的位置和大小onLayout()，如果是普通的view该方法一般不去实现，如果是viewGroup就一定要去实现
 * 因为要去指定每个孩子的位置和大小；
 * 4)绘制 : 重写onDraw()最终把控件在屏幕上显示(按照之前测量的大小和视图的位置绘制出来)
 */

/**
 * 仿iphone的开关，此处的开关过小，可取消触摸事件
 */
public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap bitmapClose;
    private Bitmap bitmapOpen;

    private Paint paint;

    private int measureWidth;
    private int measureHeight;
    /**
     * 按钮是否打开
     * true : 打开
     * false : 关闭(默认)
     */
    private boolean isOpen = false;
    /**
     * true:点击事件生效，触摸事件失效(默认)
     * false:点击事件失效，触摸事件生效
     */
    private boolean isClickEnable = true;


    /**
     * 使用this这样写构造方法，依次向下调用，这样就可以在初始化的时候传一个参数就相当于调用三个参数的构造器
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MyToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    public MyToggleButton(Context context) {
        this(context, null);
    }

    public MyToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 初始化视图
     */
    private void initView() {

        paint = new Paint();
        paint.setAntiAlias(true);

        bitmapClose = BitmapFactory.decodeResource(getResources(), R.drawable.switch_close);
        bitmapOpen = BitmapFactory.decodeResource(getResources(), R.drawable.switch_open);

        setOnClickListener(this);
    }

    /**
     * 测量视图的大小,并保存
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = bitmapClose.getWidth();
        measureHeight = bitmapClose.getHeight();
        setMeasuredDimension(measureWidth, measureHeight);
    }

    /**
     * 绘制视图的大小
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isOpen) {
            canvas.drawBitmap(bitmapClose, 0, 0, paint);
//            isOpen = true;
        } else {
            canvas.drawBitmap(bitmapOpen, 0, 0, paint);
//            isOpen = false;
        }
    }

    private float startX;
    private float endX;

    /**
     * 触摸事件的回调
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();//记录按下的位置

                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();//来到新的坐标

                //判断是否是触摸事件！！！判断其移动距离是否大于5
                if (Math.abs(event.getX() - startX) > 2) {
                    isClickEnable = false;//让点击事件失效
                    isOpen = !isOpen;
                }
                invalidate();
                startX = event.getX();

                break;
            case MotionEvent.ACTION_UP:

                if (!isClickEnable) {//如果不是点击事件

                    if (!isOpen) {

                        if ((event.getX() - startX) < -2) {
                            isOpen = true;
                        }

                    } else {
                        if ((event.getX() - startX) > 2) {
                            isOpen = false;
                        }

                    }
//                    if(Math.abs(event.getX() - startX) > 5) {
//                        isOpen = !isOpen;
//                    }

                    invalidate();
                }
                isClickEnable = true;
                break;
        }


        return true;//表示消费了此事件

    }

    /**
     * 开关点击事件的回调
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (isClickEnable) {
            if (!isOpen) {
                isOpen = true;
            } else {
                isOpen = false;
            }
//            isOpen = !isOpen;//和上面的效果一样
            invalidate();
        }
    }
}
