package com.www.lunbo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.www.lunbo.utils.DensityUtil;
import com.www.lunbo.view.AutoTextView;
import com.www.lunbo.view.TextSwitchView;
import com.www.lunbo.view.UPMarqueeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.inflate;

public class MainActivity extends AppCompatActivity {
    ViewPager mViewPager;
    RadioGroup mRadioGroup;
    UPMarqueeView mUPMarqueeView;
    //轮播图
    private int mIntTimeLunBo = 5000;
    private Timer mTimer;
    private int mIntVPNum = 6000;
    private int mIntVPImgCount = 4;
    private boolean mIsDrag;
    public ArrayList<RadioButton> mRadioButtonList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://控制轮播图的轮换
                    if (mViewPager != null) {
                        mViewPager.setCurrentItem(mIntVPNum);
                        if (mIntVPImgCount != 0) {
                            RadioButton radio = (RadioButton) mRadioGroup.getChildAt(mIntVPNum % mIntVPImgCount);
                            if (radio != null) {
                                radio.setChecked(true);
                            }
                        }
                    }
                    break;
            }
        }
    };

    //滚动图片
    private int[] mListUserPhotos = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};
    CompleteHomeAdapter pagerAdapter;

    private ArrayList<View> mUpView = new ArrayList<>();
    private ArrayList<String> mTouTiao = new ArrayList<>();

    private Handler handler = new Handler();
    private AutoTextView autoTextView;
    private int count = 0;
    private List<String> arrList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getData();
        autoTextView = (AutoTextView) findViewById(R.id.autoTextView);
        handler.postDelayed(runnable, 0);
    }

    private void initView() {
        mTouTiao.add("节目组为了请袁立参加节目，直接向她保证可以进第二轮，甚至肯答应落实到合同，说明浙江卫视为请资深演员坐镇而不择手段");
        mTouTiao.add("那么之前几期演员的晋级就难以保证不参任何水分了。聊天记录不仅让浙江卫视“打脸”，也让和袁立PK的张彤非常尴尬。");
        mTouTiao.add("而宋丹丹现场临时变卦也就侧面说明了刘烨和章子怡完全是照着起初商量好的方向投票的，更加说明了极大黑幕的存在。");
        mTouTiao.add("剪辑混乱，细心的网友发现两分钟内袁立的站位变化了许多次不说，且有恶意消费袁立和张国立“绯闻”的嫌疑，讲话顺序和其他嘉宾的表情也不遵从实际。");
        mTimer = new Timer();
        mUPMarqueeView = (UPMarqueeView) findViewById(R.id.upMarqueeView);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        creatRadioButton(mIntVPImgCount);
        pagerAdapter = new CompleteHomeAdapter(MainActivity.this);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(mIntVPNum);
        setViewPagerRolling();
        changeNum();
        setView();
        TextSwitchView tsv = (TextSwitchView) findViewById(R.id.tsv11111);
        String [] res={
                "静夜思" +
                        "床前明月光",
                "床前明月光","疑是地上霜",
                "举头望明月",
                "低头思故乡"
        };
        tsv.setResources(res);
        tsv.setTextStillTime(500);
    }

    /**
     * 初始化需要循环的View
     * 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
     * 假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
     */
    private void setView() {
        for (int i = 0; i < mTouTiao.size(); i = i + 2) {
            final int position = i;
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.customview_upmarqueeview_item_layout, null);
            //初始化布局的控件
            LinearLayout layout = (LinearLayout) moreView.findViewById(R.id.upmarqueeview_relayout);
            TextView tv1 = (TextView) moreView.findViewById(R.id.tv1);
            TextView tv2 = (TextView) moreView.findViewById(R.id.tv2);


            //进行对控件赋值
            tv1.setText(mTouTiao.get(i));
            if (mTouTiao.size() > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                tv2.setText(mTouTiao.get(i + 1).toString());
            } else {
                moreView.findViewById(R.id.rl2).setVisibility(View.GONE);
            }

            //添加到循环滚动数组里面去
            mUpView.add(moreView);
        }

        mUPMarqueeView.setViews(mUpView);
        /**
         * 设置item_view的监听
         */
        mUPMarqueeView.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {

            }
        });
    }



    //根据服务器返回的个数创建轮播图下面对应的小圆点
    private void creatRadioButton(int size) {
        for (int i = 0; i < size; i++) {
            RadioButton radioButton = (RadioButton) inflate(MainActivity.this, R.layout.lsh_homefrag_radiobutton, null);
            radioButton.setEnabled(false);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = DensityUtil.dip2px(getApplicationContext(), 2);
            layoutParams.rightMargin = DensityUtil.dip2px(getApplicationContext(), 2);
            mRadioButtonList.add(i, radioButton);
            mRadioGroup.addView(radioButton, i, layoutParams);
        }
    }

    //viewpager设置自动翻页
    public void setViewPagerRolling() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!mIsDrag) {
                        mIntVPNum++;
                        Message msg = Message.obtain();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                }
            }, mIntTimeLunBo, mIntTimeLunBo);
        }
    }

    // viewpager设置当页码改变后 对应的 radioButton 的 选中状态改变
    public void changeNum() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mIntVPNum = arg0;
                mViewPager.setCurrentItem(mIntVPNum);
                if (mIntVPImgCount != 0) {
                    RadioButton radio = (RadioButton) mRadioGroup.getChildAt(arg0 % mIntVPImgCount);
                    radio.setChecked(true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (arg1 != 0) { // 手动拖动img 时 自动翻页不执行
                    mIsDrag = true;
                } else {
                    mIsDrag = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    public class CompleteHomeAdapter extends PagerAdapter {
        LayoutInflater mInflater;

        public CompleteHomeAdapter(Activity c) {
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return 10000;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View v = mInflater.inflate(R.layout.xuebang_viewpager_imgv, container, false);
            ImageView img = (ImageView) v.findViewById(R.id.homeFrag_vp_imgv);
            img.setTag(position);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            img.setImageResource(mListUserPhotos[position % 4]);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

    private void getData() {
        // TODO Auto-generated method stub
        for (int i = 0; i < 10; i++) {
            arrList.add("竖直滚动TextView-数据" + i);
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                handler.postDelayed(this, 3000);
                autoTextView.next();
                autoTextView.setText(arrList.get(count % arrList.size()));
                count++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
