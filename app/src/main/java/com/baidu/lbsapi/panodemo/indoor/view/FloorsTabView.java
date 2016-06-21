package com.baidu.lbsapi.panodemo.indoor.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.lbsapi.panodemo.indoor.util.ScreenUtils;

/**
 * 内景楼层控件
 */
public class FloorsTabView extends HorizontalScrollView {

    private View[] views;
    private LinearLayout tabContainer;

    private int currentCheck;// 当前选中位置
    private int tabTextSize = 14;// tab导航文字大小
    private int tabTextColor = 0xFFFFFFFF;// tab导航文字默认颜色
    private int tabTextColorCheck = 0xFF3385FF;// tab导航文字选中颜色
    private LinearLayout.LayoutParams tabTextParams;

    private int screenWidth;

    private LinkedHashMap<String, OnClickListener> tabsStrListener;// tab导航文字及点击事件
    private OnScrollStopListner onScrollStopListner;

    public interface OnScrollStopListner {
        void onScrollStoped();
    }

    public FloorsTabView(Context context) {
        this(context, null);
    }

    public FloorsTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloorsTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabContainer = new LinearLayout(context);
        tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabContainer.setGravity(Gravity.CENTER);
        tabContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        addView(tabContainer);

        tabTextParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        // tabTextParams.setMargins(tabMargins, 0, tabMargins, 0);

        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        if (metric.widthPixels > metric.heightPixels) {
            screenWidth = metric.heightPixels;
        } else {
            screenWidth = metric.widthPixels;
        }
    }

    public void notifyDataSetChanged() {

        tabContainer.removeAllViews();
        views = new View[tabsStrListener.size()];
        Iterator<Entry<String, OnClickListener>> tabsStrListenerIter = tabsStrListener.entrySet().iterator();
        int i = 0;
        while (tabsStrListenerIter.hasNext()) {
            Entry<String, OnClickListener> tabsStrListenerEntry = (Entry<String, OnClickListener>) tabsStrListenerIter.next();

            TextView textView = new TextView(getContext());
            textView.setId(i);
            textView.setGravity(Gravity.CENTER);
            textView.setText(tabsStrListenerEntry.getKey());
            textView.setOnClickListener(tabsStrListenerEntry.getValue());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, tabTextSize);
            textView.setTextColor(tabTextColor);
            textView.setFocusable(true);
            textView.setSingleLine(true);
            textView.setBackgroundResource(R.drawable.baidupano_floortab_line);
            textView.setPadding(ScreenUtils.dip2px(18, getContext()), 0, ScreenUtils.dip2px(18, getContext()), 0);

            views[i] = textView;
            tabContainer.addView(textView, tabTextParams);

            i++;
        }
    }

    /**
     * 设置文字选中
     */
    public void setCheck(int position) {
        setCheck(position, false);
    }

    public void setCheck(int position, boolean isFirst) {

        if (!isFirst && (currentCheck == position)) {
            return;
        }

        LinearLayout textAll = tabContainer;//(LinearLayout) getChildAt(0);

        if (isFirst) {
            int tabTextWidthAll = 0;
            for (int m = 0; m < textAll.getChildCount(); m++) {
                // 临时解决读取控件宽度
                int w = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                int h = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

                textAll.getChildAt(m).measure(w, h);
                tabTextWidthAll = tabTextWidthAll + textAll.getChildAt(m).getMeasuredWidth();
            }
            // 加上该控件的padding(目前一般6个楼层以下效果是padding8dp,如果该控件滑动了就占满)
            tabTextWidthAll = tabTextWidthAll + ScreenUtils.dip2px(8, getContext()) + ScreenUtils.dip2px(8, getContext());
            if (tabTextWidthAll < screenWidth) {
                TextView gapTextView = new TextView(getContext());
                // 内容为空时控件长度取出来有问题
                gapTextView.setTag("Gap");
                gapTextView.setText("Pano");
                gapTextView.setTextColor(Color.TRANSPARENT);
                tabContainer.addView(gapTextView, new LinearLayout.LayoutParams(screenWidth - tabTextWidthAll, LayoutParams.MATCH_PARENT));
            }
        }

        for (int n = 0; n < textAll.getChildCount(); n++) {
            if (n == position) {
                currentCheck = position;
                ((TextView) textAll.getChildAt(n)).setTextColor(tabTextColorCheck);
                ((TextView) textAll.getChildAt(n)).setBackgroundResource(R.drawable.baidupano_floortab_arrow);
                ((TextView) textAll.getChildAt(n)).setPadding(ScreenUtils.dip2px(18, getContext()), 0, ScreenUtils.dip2px(18, getContext()), 0);
            } else {

                if (textAll.getChildAt(n).getTag() != null && textAll.getChildAt(n).getTag().equals("Gap")) {
                    ((TextView) textAll.getChildAt(n)).setTextColor(Color.TRANSPARENT);
                } else {
                    ((TextView) textAll.getChildAt(n)).setTextColor(tabTextColor);
                }
                ((TextView) textAll.getChildAt(n)).setBackgroundResource(R.drawable.baidupano_floortab_line);
                ((TextView) textAll.getChildAt(n)).setPadding(ScreenUtils.dip2px(18, getContext()), 0, ScreenUtils.dip2px(18, getContext()), 0);
            }
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 控制遮罩的显示
        if (onScrollStopListner != null) {
            onScrollStopListner.onScrollStoped();
        }
    }

    //	@Override
    //	public boolean onTouchEvent(MotionEvent ev) {
    //		getParent().requestDisallowInterceptTouchEvent(true);
    //		return super.onTouchEvent(ev);
    //
    //	}

    //	@Override
    //	public boolean onInterceptTouchEvent(MotionEvent ev) {
    //		getParent().requestDisallowInterceptTouchEvent(true);
    //		return super.onInterceptTouchEvent(ev);
    //	}

    //	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    //
    //		if (velocityX <= 0) {
    //			// hack - send event to simulate right key press
    //			KeyEvent rightKey = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT);
    //			this.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, rightKey);
    //
    //			rightKey = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_RIGHT);
    //			this.onKeyUp(KeyEvent.KEYCODE_DPAD_RIGHT, rightKey);
    //
    //		} else {
    //			// hack - send event to simulate left key press
    //			KeyEvent leftKey = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT);
    //			this.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, leftKey);
    //
    //			leftKey = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT);
    //			this.onKeyUp(KeyEvent.KEYCODE_DPAD_LEFT, leftKey);
    //		}
    //
    //		return true;
    //	}

    public void setTabsStrListener(LinkedHashMap<String, OnClickListener> tabsStrListener) {
        this.tabsStrListener = tabsStrListener;
        notifyDataSetChanged();
    }

    public LinkedHashMap<String, OnClickListener> getTabsStrings() {
        return tabsStrListener;
    }

    public void setOnScrollStopListner(OnScrollStopListner listner) {
        onScrollStopListner = listner;
    }
}
