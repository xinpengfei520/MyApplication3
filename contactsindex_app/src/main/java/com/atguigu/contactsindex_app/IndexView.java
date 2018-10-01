package com.atguigu.contactsindex_app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xinpengfei on 2016/9/24.
 *
 *  在按下和移动时显示更新提示字母
 *  1.自定义接口 OnIndexChangeListener
 *  2.定义成员变量和相关set方法
 *  3.调用接口对应的方法
 *  4.使用回调接口
 *  5.实现相应的效果
 */

public class IndexView extends View {

    //每个item的宽和高
    private int itemWidth;
    private int itemHeight;

    private String[] words = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private Paint paint;//画笔
    private int touchIndex = -1;//字母的下标位置

    private float startY;

    /**
     * 构造器
     * @param context
     * @param attrs
     */
    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();//初始化画笔
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体字
        //如果在高分辨率的手机上运行，文本大小会变小，可使用工具类转换字体的大小

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //得到每条item的宽和高
        itemWidth = getMeasuredWidth();
        itemHeight = getMeasuredHeight() / words.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        for(int i = 0; i < words.length; i++) {
          //设置当前下标对应的字母为灰色，其它为白色
            if(touchIndex == i) {
                paint.setColor(Color.GRAY);
            }else {
                paint.setColor(Color.WHITE);
            }

            String letter = words[i];//得到字母

            Rect rect = new Rect();
            paint.getTextBounds(letter,0,1,rect);

            //计算每个文字的宽和高
            int letterWidth = rect.width();
            int letterHeight = rect.height();

            //计算字母左下角的坐标
            float letterX = itemWidth / 2 - letterWidth / 2;
            float letterY = itemHeight / 2 + letterHeight / 2 + i * itemHeight;

            canvas.drawText(letter,letterX,letterY,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
        
            case MotionEvent.ACTION_MOVE :
                //得到按着的那条
                int index = (int) (event.getY()/itemHeight);
                
                if(touchIndex != index) {
                    touchIndex = index;
                    //调用接口对应的方法
                    if(listener != null && touchIndex < words.length ) {
                        listener.onTextChange(words[touchIndex]);
                    }
                    invalidate();//重绘
                }
                
                break;
            case MotionEvent.ACTION_UP:
                touchIndex = -1;
                invalidate();
                break;
        }

        return true;
    }

    /**
     * 文本变化的监听器
     */
    public interface OnTextChangeListener{
        /**
         * 当滑动文字变化的时候回调
         * @param word 被按下的字母
         */
        public void onTextChange(String word);
    }

    private OnTextChangeListener listener;

    /**
     * 设置监听文本变化
     * @param listener
     */
    public void setOnTextChangeListener(OnTextChangeListener listener){
        this.listener = listener;
    }



}
