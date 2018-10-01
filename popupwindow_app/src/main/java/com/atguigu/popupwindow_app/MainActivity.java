package com.atguigu.popupwindow_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private EditText et_input;
    private ImageView down_arrow;

    private PopupWindow popupWindow;
    private ListView listView;

    private ArrayList<String> msgs;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.down_arrow:

                    if (popupWindow == null) {
                        //在当前的Activity创建(浮现)一个Popupwindow
                        popupWindow = new PopupWindow(MainActivity.this);
                        popupWindow.setWidth(et_input.getWidth());//设置popupwindow的宽
                        popupWindow.setHeight(DensityUtil.dip2px(MainActivity.this, 200));///设置popupwindow的高

                        //popupWindow中浮现的是一个listview
                        popupWindow.setContentView(listView);

                        //设置当点击popupWindow控件以外的区域时，让popupwindow消失
                        popupWindow.setOutsideTouchable(true);

                        //注意PopupWindow 要设置焦点，不然点击时没有作用
                        popupWindow.setFocusable(true);

                    }
                    //设置固定popupWindow的位置，参数一：要挂在哪个视图组件上，参数23:x,y轴的坐标
                    popupWindow.showAsDropDown(et_input, 0, 0);

                    break;

                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_input = (EditText) findViewById(R.id.et_input);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);

        //设置点击事件
        down_arrow.setOnClickListener(mOnClickListener);

        //代码创建listView 
        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.listview_background);//设置Listview的背景

        //数据
        msgs = new ArrayList<String>();
        for (int i = 0; i < 200; i++) {
            msgs.add(i + "aaaaaaaaaaaa" + i);

        }
        listView.setAdapter(new MyAdapter());

        //设置选择某一个Item时的回调
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String text = msgs.get(position);
                et_input.setText(text);

                if(popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });

    }

    //设置适配器
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = View.inflate(MainActivity.this, R.layout.listview_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_msg = (TextView) view.findViewById(R.id.tv_msg);
                viewHolder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            //根据位置得到对应的数据
            final String msg = msgs.get(position);
            viewHolder.tv_msg.setText(msg);

            //设置某一条的Item中的控件
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //1.把数据从集合中移除
                    msgs.remove(msg);
                    //2.刷新页面
                    notifyDataSetChanged();

                }
            });

            return view;
        }

    }

    static class ViewHolder {
        TextView tv_msg;
        ImageView iv_delete;
    }
}
