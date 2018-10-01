package com.atguigu.propertyanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 测试属性动画的基本使用
 */
public class MainActivity extends Activity {

    private ImageView iv_animation;
    private TextView tv_animation_msg;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        iv_animation = (ImageView)findViewById(R.id.iv_animation);
        iv_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了图片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 测试补间(视图)动画
     * @param v
     */
    public void testTweenAnimation(View v) {

        TranslateAnimation animation = new TranslateAnimation(0,iv_animation.getWidth(),0,iv_animation.getHeight());
        animation.setDuration(500);
        //设置动画结束之后停留在此
        animation.setFillAfter(true);
        iv_animation.startAnimation(animation);
    }

    /**
     * 测试属性动画
     * @param v
     */
    public void testPropertyAnimation(View v) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_animation,"translationX",0,iv_animation.getWidth());
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(iv_animation,"translationY",0,iv_animation.getHeight());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        //两个动画一起播放
        animatorSet.playTogether(animator,animator1);
        //开始播放
        animatorSet.start();

        //方法二：
        /*iv_animation.animate()
                .translationXBy(iv_animation.getWidth())
                .translationYBy(iv_animation.getHeight())
                .setDuration(3000)
                .setInterpolator(new BounceInterpolator())
                .start();*/
    }

    /**
     * 重置补间动画
     * @param v
     */
    public void reset(View v) {

        iv_animation.clearAnimation();
    }
}
