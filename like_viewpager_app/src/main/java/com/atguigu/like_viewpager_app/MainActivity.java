package com.atguigu.like_viewpager_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    //初始化6张图片数据
    private int[] ids = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6};

    private MyViewPager myViewPager;
    private RadioGroup rg_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myViewPager = (MyViewPager) findViewById(R.id.myviewpager);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        //添加6个页面
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            //把6个页面添加到视图中
            myViewPager.addView(imageView);

        }

        //添加测试页面
        View test = View.inflate(this, R.layout.test, null);
        myViewPager.addView(test, 2);

        //根据有多少个页面就添加多少个RaidoButton
        //添加RaidoButton
        for (int i = 0; i < myViewPager.getChildCount(); i++) {

            RadioButton button = new RadioButton(this);
            button.setId(i);//0~5
            if (i != 0) {
                button.setChecked(false);
            } else {
                button.setChecked(true);
            }

            //添加到RadioButton中
            rg_main.addView(button);
        }

        //设置按钮的点击状态改变的监听
        rg_main.setOnCheckedChangeListener(new MyOnCheckedChangeListener());

        //设置监听页面的改变
        myViewPager.setOnPagerChangeListener(new MyViewPager.OnPagerChangeListener() {

            /**
             *  0 ~ 5
             * @param index : 页面的下标位置
             */
            @Override
            public void onChangePager(int index) {
                rg_main.check(index);
            }
        });
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * 0 ~ 5
         *
         * @param group
         * @param checkedId
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            myViewPager.moveToPager(checkedId);
        }
    }
}
