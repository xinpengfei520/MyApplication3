package com.atguigu.mywave_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xinpengfei on 2016/9/26.
 * App : 水波纹效果完整版(自定义控件练习)
 */

public class MyWave extends View {

    //设置相邻波浪中心点的最小距离
    private static final int DIS_SOLP = 5;

    protected boolean isRunning = false;//判断是波浪是否正在扩散

    private ArrayList<Wave> wList;//用于存储多个波浪的集合

    private Paint paint;//画笔
    private int radius;//圆环的半径
    /**
     * 圆心的坐标(x,y)
     */
    private float startX;
    private float startY;

    //颜色数组用于随机获取
    private int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE,Color.YELLOW, Color.CYAN};

    /**
     * 初始化波浪时的构造函数
     *
     * @param context
     * @param attrs
     */
    public MyWave(Context context, AttributeSet attrs) {
        super(context, attrs);

        wList = new ArrayList<Wave>();//初始化集合数据
    }

    /**
     * 初始化波浪视图
     */
    private void initView() {

        radius = 5;
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);

        //设置样式-圆环 ：代表圆环的样式STROKE
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeWidth(radius / 3);//设置圆环的厚度
    }

    /**
     * 定义一个用与按给定时间间隔发消息的handler
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            //刷新数据
            flushData();

            invalidate();//刷新页面
            //循环动画
            if (isRunning) {
                handler.sendEmptyMessageDelayed(0, 50);//每50ms发一次0
            }
        }
    };

    /**
     * //刷新数据
     */
    private void flushData() {

        for (int i = 0; i < wList.size(); i++) {

            Wave w = wList.get(i);

            //如果透明度为0,从集合中删除
            int alpha = w.paint.getAlpha();
            if (alpha == 0) {
                //删除i以后，i的值应该减一，否则会漏掉一个对象，不过此处影响不大，效果上看不出来
                wList.remove(i);
                continue;
            }

            alpha -= 5;//每次循环让透明度减5
            if (alpha < 0) {//屏蔽非法值
                alpha = 0;
            }

            //降低透明度
            w.paint.setAlpha(alpha);

            //同时扩大半径
            w.radius += 3;
            //设置圆环的宽度
            w.paint.setStrokeWidth(w.radius / 3);
        }

        /**
         * 如果集合被清空，就停止刷新动画
         */
        if (wList.size() == 0) {
            isRunning = false;
        }
    }

    /**
     * 绘制圆环
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        //遍历集合中所有的波浪并绘制
        for (int i = 0; i < wList.size(); i++) {
            Wave wave = wList.get(i);
            canvas.drawCircle(wave.pointX, wave.pointY, wave.radius, wave.paint);
        }

    }

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

            case MotionEvent.ACTION_MOVE:
                //圆心
                int x = (int) event.getX();
                int y = (int) event.getY();

                //每移动一个位置的时候就将新的坐标(圆心)加入到集合中
                addPoint(x, y);

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 将新的坐标(圆心)加入到集合中
     *
     * @param x
     * @param y
     */
    private void addPoint(int x, int y) {

        if (wList.size() == 0) {
            addPoint2List(x, y);
            /**
             * 第一次启动的动画
             */
            isRunning = true;
            handler.sendEmptyMessage(0);
        } else {
            //取最后一个圆环
            Wave w = wList.get(wList.size() - 1);

            if (Math.abs(w.pointX - x) > DIS_SOLP || Math.abs(w.pointY - y) > DIS_SOLP) {
                addPoint2List(x, y);
            }
        }
    }

    /**
     * 添加新的(第二个)波浪到集合中
     *
     * @param x
     * @param y
     */
    private void addPoint2List(int x, int y) {

        Wave w = new Wave();//创建一个水波浪的实例

        /**
         * 将传入的位置(圆心)设置给这个水波浪的实例
         */
        w.pointX = x;
        w.pointY = y;

        Paint pa = new Paint();//创建一个画笔

        //从颜色数组中随机获取一个颜色设置给画笔
        pa.setColor(colors[(int) (Math.random() * 4)]);

        pa.setAntiAlias(true);//设置抗锯齿
        pa.setStyle(Paint.Style.STROKE);//设置画笔的样式为STROKE(圆环)

        w.paint = pa;//将设置好的画笔设置给当前实例的属性

        wList.add(w);//将波浪加入到波浪集合中
    }


    /**
     * 定义一个水波浪类型(面向对象的思想)！
     */
    private class Wave {

        //圆心
        int pointX;
        int pointY;

        //画笔
        Paint paint;
        //半径
        int radius;
    }
}
