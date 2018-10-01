package com.atguigu.contactsindex_app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by xinpengfei on 2016/9/27.
 *
 * Function : 联系人搜索类SearchView
 */

public class SearchView extends LinearLayout{

    private EditText sv_search;
    private ImageView sv_delete;
    private Context mContext;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_view, this);
        initView();
    }

    /**
     * 初始化视图对象
     */
    private void initView() {

        sv_search = (EditText)findViewById(R.id.sv_search);
        sv_delete = (ImageView)findViewById(R.id.sv_delete);

        /**
         * 点击删除按钮的回调
         */
        sv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(sv_search.getText().toString())) {

                    sv_search.setText("");
                    sv_delete.setVisibility(GONE);
                }
            }
        });

        /**
         * 给sv_search设置一个文本改变的监听
         * 注 ： 参数：需要一个TextWatcher的对象，而TextWatcher是一个接口，所以需要一个接口的实现类
         */
        sv_search.addTextChangedListener(new inputLetterChangerListener());
    }


    /**
     * 定义一个TextWatcher接口的实现类inputLetterChangerListener
     * 作用 : 监听输入文本的变化
     */
    class inputLetterChangerListener implements TextWatcher{

        /**
         * 文本改变前的回调
         * @param s
         * @param start
         * @param count
         * @param after
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        /**
         * 文本正在改变的回调
         * @param s
         * @param start
         * @param before
         * @param count
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.e("TAG", "CharSequence=" + s);

            if(!"".equals(s.toString())) {
                sv_delete.setVisibility(View.VISIBLE);
                if(mlistener != null) {
                    mlistener.onRefreshAutoComplete(s+"");
                    Log.e("TAG", "s=" + s);
                }
            }else {
                sv_delete.setVisibility(View.GONE);
            }

        }

        /**
         * 文本改变后的回调
         * @param s
         */
        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 搜索输入文本的回调
     */
    public interface SearchViewListener{

        /**
         * 传入补全后的文本
         * @param text
         */
        void onRefreshAutoComplete(String text);

        /**
         * 开始搜索
         * @param text
         */
        void onSearch(String text);

    }

    private SearchViewListener mlistener;//定义一个搜索的监听

    /**
     * 设置搜索的回调的方法
     * @param listener
     */
    public void setSearchViewListener(SearchViewListener listener){
        mlistener = listener;
    }


}
