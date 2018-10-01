package com.atguigu.slidemenu_app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by xinpengfei on 2016/9/24.
 *
 * 作用：侧滑菜单继承于FrameLayout
 */

public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;

    //内容的宽
    private int contentWidth;
    //menu的宽
    private int menuWidth;
    //两个控件的高
    private int viewHeight;
    private Scroller scroller;

    private float startX;
    private float startY;
    /**
     * 记录历史起始值
     */
    private float lastX;
    private float lastY;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    /**
     * 测量视图
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        //得到菜单的宽
        menuWidth = menuView.getMeasuredWidth();

        viewHeight = getMeasuredHeight();
    }

    /**
     * 指定视图的位置
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);

    }

    /**
     * 反拦截事件
     * @param event
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        boolean isIntercept = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //按下
                if (onStatechangeListener != null) {
                    onStatechangeListener.onDown(this);
                }
                Log.e("TAG", "onInterceptTouchEvent--ACTION_DOWN");
                //1.记录起始坐标
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("TAG", "onInterceptTouchEvent--ACTION_MOVE");
                //2.来到结束坐标
                float endX = event.getX();

                //5.重新记录坐标
                float DX = Math.abs(endX - lastX);//在水平方向滑动的距离

                if (DX > 5) {
                    //1.让父层视图不拦截子view的事件；2.把事件传给控件
                    isIntercept = true;
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.e("TAG", "onInterceptTouchEvent--ACTION_UP");
                break;
        }
        return isIntercept;
    }

    /**
     * 触摸事件的回调
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.e("TAG", "onTouchEvent--ACTION_DOWN");
                //1.记录起始坐标
                lastX = startX = event.getX();
                lastY = startY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.e("TAG", "onTouchEvent--ACTION_MOVE");
                //2.来到结束坐标
                float endX = event.getX();
                float endY = event.getY();
                //3.计算偏移量
                float distanceX = endX - startX;

                //4.移动视图
                int toScrollX = (int) (getScrollX() - distanceX);
                if (toScrollX < 0) {
                    toScrollX = 0;
                }
                if (toScrollX > menuWidth) {
                    toScrollX = menuWidth;
                }
                scrollTo(toScrollX, getScrollY());
                //5.重新记录坐标
                startX = event.getX();
                startY = event.getY();

                float DX = Math.abs(endX - lastX);//在水平方向滑动的距离
                float DY = Math.abs(endY - lastY);//在竖直方向滑动的距离

                if (DX > DY && DX > 5) {
                    //1.让父视图不拦截子view事件，把事件传给当前控件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:

                Log.e("TAG", "onTouchEvent--ACTION_UP");
                int totalScrollX = getScrollX();
                Log.e("TAG", "totalScrollX=" + totalScrollX);
                if (totalScrollX < menuWidth / 2) {
                    //关闭
                    closeMenu();
                } else {
                    openMenu();
                }

                break;
        }

        return true;
    }


    private void openMenu() {

        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), getScrollY());
        invalidate();//导致重绘，还会导致computeScroll执行
        if (onStatechangeListener != null) {
            onStatechangeListener.onOpen(this);
        }
    }

    // 0
    public void closeMenu() {

        scroller.startScroll(getScrollX(), getScrollY(), 0 - getScrollX(), getScrollY());
        invalidate();//导致重绘，还会导致computeScroll执行
        //关闭
        if (onStatechangeListener != null) {
            onStatechangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), getScrollY());
            invalidate();
        }
    }

    /**
     * 设置状态改变的监听者
     */
    public interface OnStateChangeListener {
        /**
         * 当按下的时候回调
         */
        public void onDown(SlideLayout layout);

        /**
         * 当打开的时候回调的方法
         */
        public void onOpen(SlideLayout layout);

        /**
         * 当关闭的时候的回调方法
         *
         * @param layout
         */
        public void onClose(SlideLayout layout);

    }

    private OnStateChangeListener onStatechangeListener;

    /**
     * 设置状态变化的监听
     *
     * @param onStatechangeListener
     */
    public void setOnstatechangeListener(OnStateChangeListener onStatechangeListener) {
        this.onStatechangeListener = onStatechangeListener;
    }

}
