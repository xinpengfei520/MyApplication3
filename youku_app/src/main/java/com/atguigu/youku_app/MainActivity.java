package com.atguigu.youku_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView icon_menu;
    private ImageView icon_home;
    private RelativeLayout level1;
    private RelativeLayout level2;
    private RelativeLayout level3;

    /**
     * true:三级菜单显示
     * false:三级菜单隐藏
     */
    private boolean isLevel3Show = true;

    /**
     * true:二级菜单显示
     * false:二级菜单隐藏
     */
    private boolean isLevel2Show = true;

    /**
     * true:一级菜单显示
     * false:一级菜单隐藏
     */
    private boolean isLevel1Show = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        icon_home = (ImageView) findViewById(R.id.icon_home);
        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);

        //设置点击事件
        icon_menu.setOnClickListener(this);
        icon_home.setOnClickListener(this);

        level1.setOnClickListener(this);
        level2.setOnClickListener(this);
        level3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.level1:
                Toast.makeText(MainActivity.this, "level1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.level2:
                Toast.makeText(MainActivity.this, "level2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.level3:
                Toast.makeText(MainActivity.this, "level3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.icon_menu:
                if (isLevel3Show) {
                    isLevel3Show = false;
                    //隐藏
                    Tools.hideView(level3);
                } else {
                    isLevel3Show = true;
                    //显示
                    Tools.showView(level3);
                }
                break;
            case R.id.icon_home:
                //判断二级菜单和三级菜单是否显示，显示就都隐藏
                if (isLevel2Show) {
                    isLevel2Show = false;
                    Tools.hideView(level2);

                    if (isLevel3Show) {
                        isLevel3Show = false;
                        //隐藏三级菜单
                        Tools.hideView(level3, 300);
                    }
                } else {//判断二级菜单是否隐藏，隐藏就显示
                    isLevel2Show = true;
                    //显示二级菜单
                    Tools.showView(level2);
                }

                break;
            default:
                break;
        }
    }

    //监听菜单键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //判断一级，二级，三级菜单是否显示，如果显示，就全部隐藏

            if (isLevel1Show) {
                isLevel1Show = false;
                //隐藏一级菜单
                Tools.hideView(level1);
                if (isLevel2Show) {
                    isLevel2Show = false;
                    //隐藏二级菜单
                    Tools.hideView(level2, 200);

                    if (isLevel3Show) {
                        isLevel2Show = false;
                        Tools.hideView(level3, 400);
                    }
                }

            } else {//判断二级菜单是否隐藏，隐藏就显示

                isLevel1Show = true;
                //显示一级菜单
                Tools.showView(level1);

                isLevel2Show = true;

                //显示二级菜单
                Tools.showView(level2, 200);

            }

            //判断一级，二级菜单是否隐藏，隐藏就显示
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
