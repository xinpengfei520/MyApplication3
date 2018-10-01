package com.atguigu.slidemenu_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView lv_main;

    private ArrayList<MyBean> myBeans;
    private SlideLayout slideLayout;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView) findViewById(R.id.lv_main);

        initData();//初始化集合数据

        //设置适配器
        adapter = new MyAdapter();
        lv_main.setAdapter(adapter);
    }

    /**
     * 初始化集合数据
     */
    private void initData() {

        myBeans = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            myBeans.add(new MyBean("Content" + i));
        }
    }

    /**
     * listview的适配器(用于装数据)
     */
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return myBeans.size();
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

            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item, null);
                viewHolder = new ViewHolder();
                viewHolder.item_content = (TextView) convertView.findViewById(R.id.item_content);
                viewHolder.item_menu = (TextView) convertView.findViewById(R.id.item_menu);
                convertView.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            final MyBean myBean = myBeans.get(position);
            viewHolder.item_content.setText(myBean.getName());

            //设置item_content的tag
            viewHolder.item_content.setTag(position);

            /**
             * 设置listview中某个item中的item_content控件的点击事件的监听(在getVeiw中)
             */
            viewHolder.item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    MyBean myBean1 = myBeans.get(position);
                    Toast.makeText(MainActivity.this, "myBena==", Toast.LENGTH_SHORT).show();

                }
            });

            //设置item_menu的tag
            viewHolder.item_menu.setTag(position);

            /**
             * 设置listview中某个item中的item_menu控件的点击事件的监听(在getVeiw中)
             */
            viewHolder.item_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    SlideLayout slideLayout = (SlideLayout) v.getParent();
                    slideLayout.closeMenu();
                    Toast.makeText(MainActivity.this, "移除成功", Toast.LENGTH_SHORT).show();
                    myBeans.remove(myBean);//从集合中移除
                    notifyDataSetChanged();//刷新

                }
            });

            //slideLayout实质是FrameLayout也是convertView因此可以强转
            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnstatechangeListener(new MyOnStateChangeListener());

            return convertView;
        }
    }

    /**
     * 自定义一个OnStateChangeListener接口的实现类MyOnStateChangeListener
     * 用于监听滑动状态的改变
     */
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {

        @Override
        public void onDown(SlideLayout layout) {

            //当不同的时候才关闭
            if (slideLayout != null && slideLayout != layout) {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onOpen(SlideLayout layout) {

            //记录是谁打开的
            slideLayout = layout;
        }

        @Override
        public void onClose(SlideLayout layout) {

            if (slideLayout == layout) {
                slideLayout = null;
            }
        }
    }

    static class ViewHolder {
        TextView item_content;
        TextView item_menu;
    }
}
