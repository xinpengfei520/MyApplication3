package com.atguigu.like_viewpager_app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by xinpengfei on 2016/9/23.
 * 作用：自定义ViewPager类似功能
 */
public class MyViewPager extends ViewGroup { //注意此处继承的是ViewGroup而非ViewPager!

    private static final String TAG = MyViewPager.class.getSimpleName();

    /**
     * 1.定义手势识别器
     * 2.实例化手势识别器
     * 3.从onTouchEvent()接收事件
     */
    private GestureDetector detector;

    //当前页面的下标
    private int currentIndex;
    private Scroller scroller;


    /**
     * 构造器
     * @param context
     * @param attrs
     */
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        scroller = new Scroller(context);
        detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            /**
             *
             * @param e1 : 按下
             * @param e2 ：离开
             * @param distanceX ：在X轴产生的距离
             * @param distanceY ：在Y轴产生的距离
             * @return
             */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                /**
                 * android系统提供移动控件“内容”的两个方法之一，其控件本身的位置 未发生改变
                 * x : 在x轴要移动的距离
                 * y : 在Y轴要移动的距离
                 */
                scrollBy((int) distanceX, 0);//移动的是距离
                Log.e("TAG", "distanceX==" + distanceX);

//                scrollTo(x,y); //这个方法移动的是坐标

                return super.onScroll(e1, e2, distanceX, distanceY);
            }

        });

    }

    /**
     * 测量每个孩子
     * widthMeasureSpec :包含父类该当前控件的宽和模式
     * 三种模式：EXACTLY,AT_MOST,UNSPECIFIED
     * 系统的onMesaure中所干的事：
     * 1、根据 widthMeasureSpec 求得宽度width，和父view给的模式
     * 2、根据自身的宽度width 和自身的padding 值，相减，求得子view可以拥有的宽度newWidth
     * 3、根据 newWidth 和模式求得一个新的MeasureSpec值:
     * MeasureSpec.makeMeasureSpec(newSize, newmode);
     * 4、用新的MeasureSpec来计算子view
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);

        Log.e("TAG", "width== " + width + ",height" + height + ",mode == " + mode);

        for (int i = 0; i < getChildCount(); i++) {
            View children = getChildAt(i);
            children.measure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    /**
     * 指定孩子的位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < getChildCount(); i++) {
            View children = getChildAt(i);

            //指定第0个页面的坐标
            children.layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }

    private float startX;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        //把事件传递给手势识别器
        detector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //1.记录坐标
                startX = ev.getX();
                break;

            case MotionEvent.ACTION_HOVER_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                //2.来到新的坐标
                float endX = ev.getX();
                //3.判断滑动方向
                int tempIndex = currentIndex;

                if ((endX - startX) > getWidth() / 2) {
                    //显示上一个页面
                    tempIndex--;
                } else if ((startX - endX) > getWidth() / 2) {
                    //显示下一个页面
                    tempIndex++;
                }

                //根据位置移动到对应的页面
                moveToPager(tempIndex);

                //重新记录起始值
                startX = ev.getX();

                break;
        }

        return true;
    }

    /**
     * 屏蔽非法下标位置和移动对应下标位置的页面
     *
     * @param tempIndex
     */
    public void moveToPager(int tempIndex) {

        //屏蔽非法值
        if (tempIndex < 0) {
            tempIndex = 0;
        }
        if (tempIndex > getChildCount() - 1) {
            tempIndex = getChildCount() - 1;
        }
        currentIndex = tempIndex;
        //根据坐标移动控件的内容

        if (changeListener != null) {
            changeListener.onChangePager(currentIndex);
        }

        int distanceX = currentIndex * getWidth() - getScrollX();

//        scrollTo(currentIndex*getWidth(),0);
        //开始计时
        scroller.startScroll(getScrollX(), getScrollY(), distanceX, 0, Math.abs(distanceX));

        invalidate();//会导致onDraw执行，并且还会导致computeScroll执行

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {

            //一小段对应的坐标
            float currX = scroller.getCurrX();
            scrollTo((int) currX, 0);

            invalidate();//导致computeScroll执行
        }
    }

    /**
     * 当滑动页面改变的监听者
     */
    public interface OnPagerChangeListener {

        /**
         * 当页面改变的时候回调这个方法
         *
         * @param index : 页面的下标位置
         */
        void onChangePager(int index);
    }

    private OnPagerChangeListener changeListener;

    /**
     * 设置监听页面改变 -by me
     *
     * @param listener
     */
    public void setOnPagerChangeListener(OnPagerChangeListener listener) {

        this.changeListener = listener;
    }
}
