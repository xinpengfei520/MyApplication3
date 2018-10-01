package com.atguigu.viewpager_app;

import android.app.Activity;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnTouchListener {

    private ViewPager viewpager;
    private TextView tv_msg;
    private LinearLayout ll_group_point;
    private MyPagerAdapter adapter;

    /**
     * 图片的ID资源
     */
    private int[] ids = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};

    /**
     * 图片标题集合
     */
    private final String[] imageDescriptions = {
            "尚硅谷波河争霸赛！",
            "凝聚你我，放飞梦想！",
            "抱歉没座位了！",
            "7月就业名单全部曝光！",
            "平均起薪11345元"
    };

    /**
     * 上一次被高亮显示的位置
     */
    private int lastIndex;
    /**
     * 图片的集合 ：可以是ImageView和Fragment，View
     */
    private ArrayList<ImageView> imageViews;
    /**
     * 当前Activity是否销毁
     */
    private boolean isActivityIsDestroy = false;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);

            if (!isActivityIsDestroy) {
                handler.sendEmptyMessageDelayed(0, 3000);
            }

        }
    };

    /**
     * ListView的使用
     * 1.在布局文件定义
     * 2.准备数据-集合
     * 3.设置适配器
     * 4.实现getView 和getCount();
     * 5.实现getView()-item.xml
     * <p>
     * ViewPager的使用和ListView类似
     * 1.在布局文件定义
     * 2.准备数据-集合
     * 3.设置适配器-PagerAdapter
     * 4.有四个方法一定要实现的
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        ll_group_point = (LinearLayout) findViewById(R.id.ll_group_point);

        //2.准备数据
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {

            ImageView imageView = new ImageView(this);
            imageView.setTag(i);//把位置设为tag
            imageView.setBackgroundResource(ids[i]);
            //给imageView设置点击事件
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = (int) v.getTag();//把tag转换为位置
                    String text = imageDescriptions[position];//获取图片标题并显示
                    Toast.makeText(MainActivity.this, "text = " + text, Toast.LENGTH_SHORT).show();
                }
            });

            imageView.setOnTouchListener(this);

            //加入到集合中
            imageViews.add(imageView);
            initPoint(i);//初始化viewpager中的点

            adapter = new MyPagerAdapter();//设置适配器
            viewpager.setAdapter(adapter);

            //设置文本为第0个
            tv_msg.setText(imageDescriptions[0]);

            //设置viewPager页面改变的监听
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

            //保证Integer.MAX_VALUE/2是imageViews.size()的整数倍
            int item = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imageViews.size();

            //设置中间值
            viewpager.setCurrentItem(item);

            //开始循环滑动
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }


    /**
     * 触摸事件的回调
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handler.removeCallbacksAndMessages(null);
            Log.e("TAG", "ACTION_DOWN");
        } else if (event.getAction() == MotionEvent.ACTION_UP) {

            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(0, 3000);
            Log.e("TAG", "ACTION_UP");
        }
        /*else if (event.getAction() == MotionEvent.ACTION_CANCEL) {

            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(0, 3000);
            Log.e("TAG", "ACTION_CANCEL");
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(0, 3000);
            Log.e("TAG", "ACTION_MOVE");
        }*/

        return false;
    }

    /**
     * 初始化并创建视图中的点
     *
     * @param i
     */
    private void initPoint(int i) {
        //有多少个页面就创建多少个点
        ImageView point = new ImageView(this);
        int widthDp = DensityUtil.dip2px(this, 8);
        Log.e("TAG", widthDp + "");
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthDp, widthDp);
        if (i != 0) {
            params.leftMargin = widthDp;
        }

        point.setLayoutParams(params);
        point.setBackgroundResource(R.drawable.point_selector);

        //默认第一个点告诉
        if (i == 0) {
            point.setEnabled(true);
        } else {
            point.setEnabled(false);
        }

        //添加指示点到线性布局
        ll_group_point.addView(point);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivityIsDestroy = true;
    }


    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当页面滚动了的时候回调这个方法(必须掌握)
         *
         * @param position             滚动页面的位置
         * @param positionOffset       当前滑动页面的百分比，例如滑动一半是0.5
         * @param positionOffsetPixels 当前页面滑动的像素
         */

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            Log.e("TAG", "position=" + position + "," + positionOffset + "," + positionOffsetPixels);
        }

        /**
         * 当页面改变了的时候回调这个方法
         *
         * @param position 当前被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {

            int realPosition = position % imageViews.size();
            String text = imageDescriptions[realPosition];
            tv_msg.setText(text);
            //1.把之前的设置为默认
            ll_group_point.getChildAt(lastIndex).setEnabled(false);
            //2.把当前的位置对应的页面设置为高亮
            ll_group_point.getChildAt(realPosition).setEnabled(true);
            //ll_group_point.getChildAt(position) --->ImageView
            lastIndex = realPosition;

        }

        /**
         * 当页面状态发生变化的时候回调这个方法
         * 静止到滑动
         * 滑动到静止
         *
         * @param state
         */
        boolean isDragging = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //正在拖动中...
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                Log.e("TAG", "正在拖动中...");
                //移除消息
                handler.removeCallbacksAndMessages(null);
                isDragging = true;

                //静止状态中...
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                Log.e("TAG", "静止状态中...");

                handler.removeCallbacksAndMessages(null);//这个一定要加上
                handler.sendEmptyMessageDelayed(0, 3000);

                //正在滑动中...
            } else if (state == ViewPager.SCROLL_STATE_SETTLING && isDragging) {
                Log.e("TAG", "正在滑动中...");
                isDragging = false;
                handler.removeMessages(0);//这个一定要加上
                handler.sendEmptyMessageDelayed(0, 3000);
            }
        }
    }

    /**
     * 创建一个继承于PagerAdapter的适配器，至少需要实现4方法
     */
    private class MyPagerAdapter extends PagerAdapter {

        //返回总条数
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;//imageViews.size()
        }


        /**
         * 作用类似于getView();
         * 把页面添加到ViewPager中
         * 并且返回当前页面的相关的特性
         * container:容器就是ViewPager自身
         * position：实例化当前页面的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            //1.添加到viewPager中
            ImageView imageView = imageViews.get(position % imageViews.size());
            container.addView(imageView);

            //2.并且返回当前页面的相关的特性
            return imageView;
        }

        /**
         * view和object比较是否是同一个View
         * 如果相同返回true
         * 不相同返回false
         * object:其实就是instantiateItem()方法返回的值
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {

//            if(view==object){
//				return true;
//			}else{
//				return false;
//			}

            return view == object;//此句和上面的相同
        }

        /**
         * 销毁container:ViewPager
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }

}
