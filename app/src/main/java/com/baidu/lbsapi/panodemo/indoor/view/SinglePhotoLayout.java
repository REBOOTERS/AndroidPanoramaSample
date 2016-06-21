package com.baidu.lbsapi.panodemo.indoor.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.lbsapi.panodemo.indoor.util.SysUtil;

/**
 * 每张缩略图布局
 */
public class SinglePhotoLayout extends LinearLayout {

    private AsyncImageView mImageView;
    private TextView mTextView, img_stroke;
    private String mName;
    public boolean mIsHighlighted = false;

    public SinglePhotoLayout(Context context) {
        super(context);
    }

    public SinglePhotoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AsyncImageView getImageView() {
        return mImageView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mImageView = (AsyncImageView) findViewById(R.id.asyncImage);
        mTextView = (TextView) findViewById(R.id.textAsync);
        img_stroke = (TextView) findViewById(R.id.img_stroke);
    }

    public void setImageUrl(String url) {
        if (mImageView != null)
            mImageView.loadImage(url);
    }

    public void setImageResource(int resId) {
        if (mImageView != null) {
            mImageView.setImageResource(resId);
        }
    }

    public void setName(String name) {
        this.mName = "";
        if (name.length() > 5) {
            int charCount = 0;
            for (int i = 0; i < name.length(); i++) {
                if (charCount > 9) {
                    break;
                }
                char temp = name.charAt(i);
                try {
                    if (SysUtil.isChinese(temp)) {
                        charCount += 2;
                    } else {
                        charCount++;
                    }
                    mName += temp;
                } catch (UnsupportedEncodingException e) {
                }
            }
            try {
                char temp = mName.charAt(mName.length() - 1);
                if (SysUtil.isChinese(temp)) {
                    mName = mName.substring(0, mName.length() - 1);
                } else {
                    mName = mName.substring(0, mName.length() - 2);
                }
                mName += "...";
            } catch (UnsupportedEncodingException e) {
            }

        } else {
            mName = name;
        }
        mTextView.setText(mName);
    }

    public void setHighLight(boolean current) {
        mIsHighlighted = current;
        if (mIsHighlighted) {
            img_stroke.setBackgroundResource(R.drawable.baidupano_shape_photocase_selc);
        } else {
            img_stroke.setBackgroundResource(R.drawable.baidupano_shape_photocase);
        }
    }
}
