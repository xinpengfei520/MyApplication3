package com.atguigu.contactsindex_app;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity implements SearchView.SearchViewListener {

    private ListView lv_main;
    private TextView tv_main_letter;
    private IndexView index_words;

    private Handler handler = new Handler();
    private ArrayList<Person> persons;
    private MyAdapter adapter;
    private SearchView searchView;//自定义searchView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView) findViewById(R.id.lv_main);
        tv_main_letter = (TextView) findViewById(R.id.tv_main_letter);
        index_words = (IndexView) findViewById(R.id.index_words);

        //设置按下字母变化的监听
        index_words.setOnTextChangeListener(new MyOnTextChangeListener());

        initData();//初始化数据

        adapter = new MyAdapter();
        lv_main.setAdapter(adapter);

    }


    /**
     * 初始化数据
     */
    private void initData() {

        persons = new ArrayList<>();
        persons.add(new Person("张晓飞"));
        persons.add(new Person("杨光福"));
        persons.add(new Person("胡继群"));
        persons.add(new Person("刘畅"));

        persons.add(new Person("钟泽兴"));
        persons.add(new Person("尹革新"));
        persons.add(new Person("安传鑫"));
        persons.add(new Person("张骞壬"));

        persons.add(new Person("温松"));
        persons.add(new Person("李凤秋"));
        persons.add(new Person("刘甫"));
        persons.add(new Person("娄全超"));
        persons.add(new Person("张猛"));

        persons.add(new Person("王英杰"));
        persons.add(new Person("李振南"));
        persons.add(new Person("孙仁政"));
        persons.add(new Person("唐春雷"));
        persons.add(new Person("牛鹏伟"));
        persons.add(new Person("姜宇航"));

        persons.add(new Person("刘挺"));
        persons.add(new Person("张洪瑞"));
        persons.add(new Person("张建忠"));
        persons.add(new Person("侯亚帅"));
        persons.add(new Person("刘帅"));

        persons.add(new Person("乔竞飞"));
        persons.add(new Person("徐雨健"));
        persons.add(new Person("吴亮"));
        persons.add(new Person("王兆霖"));

        persons.add(new Person("阿三"));
        persons.add(new Person("周娟"));
        persons.add(new Person("的哥"));

        //排序
        Collections.sort(persons, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getPinyin().compareTo(o2.getPinyin());
            }
        });

    }

    /**
     * MainActivity实现SearchView.SearchViewListener接口的回调刷新方法
     *
     * @param text
     */
    @Override
    public void onRefreshAutoComplete(String text) {

        String inputText = text.toUpperCase();//将传入的文本转换为大写字母
//        updateWord(inputText);
//        updateList(inputText);
        Log.e("TAG", "inputText=" + inputText);
        for (int i = 0; i < persons.size(); i++) {

            if (inputText.equals(persons.get(i).getPinyin().substring(0, 1))) {
                lv_main.setSelection(i);//设置搜索到的listview的位置
//                lv_main.notifyAll();
            }
        }

        tv_main_letter.setVisibility(View.VISIBLE);
        tv_main_letter.setText(inputText);

        //移除所有消息
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_main_letter.setVisibility(View.GONE);

            }
        }, 1000);

        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * MainActivity实现SearchView.SearchViewListener接口的回调搜索方法
     *
     * @param text
     */
    @Override
    public void onSearch(String text) {

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return persons.size();
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
                convertView = View.inflate(MainActivity.this, R.layout.item_name, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHolder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            String word = persons.get(position).getPinyin().substring(0, 1);
            String name = persons.get(position).getName();

            viewHolder.tv_name.setText(name);
            viewHolder.tv_word.setText(word);

            if (position == 0) {
                viewHolder.tv_word.setVisibility(View.VISIBLE);
            } else {
                //前一条的首字母
                String wordPre = persons.get(position - 1).getPinyin().substring(0, 1);
                if (wordPre.equals(word)) {
                    viewHolder.tv_word.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_word.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }
    }

    static class ViewHolder {
        TextView tv_word;
        TextView tv_name;
    }

    class MyOnTextChangeListener implements IndexView.OnTextChangeListener {

        @Override
        public void onTextChange(String word) {
            updateWord(word);
            updateList(word);
        }
    }

    /**
     * A ~ Z
     *
     * @param word
     */
    private void updateList(String word) {
        for (int i = 0; i < persons.size(); i++) {
            String listWord = persons.get(i).getPinyin().substring(0, 1);
            if (word.equals(listWord)) {
                lv_main.setSelection(i);
                break;
            }
        }
    }

    private void updateWord(String word) {
        tv_main_letter.setVisibility(View.VISIBLE);
        tv_main_letter.setText(word);
        handler.removeCallbacksAndMessages(null);//把所有的消息移除
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //在主线程
                Log.e("TAG", Thread.currentThread().getName() + "");
                tv_main_letter.setVisibility(View.GONE);
            }
        }, 2000);
    }


    /*private class MyOnLetterChangeListener implements IndexView.OnLetterChangeListener {
        @Override
        public void onLetterChange(String letter) {

        }
    }

    *//**
     * 设置监听输入字母的变化
     *//*
    public void setOnLetterChangeListener(OnLetterChangeListener listener) {
        this.listener2 = listener;
    }
    private OnLetterChangeListener listener2;

    public interface OnLetterChangeListener {

        public void onLetterChange(String letter);
    }*/
}
