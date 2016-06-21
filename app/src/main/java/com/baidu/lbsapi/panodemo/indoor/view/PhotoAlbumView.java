package com.baidu.lbsapi.panodemo.indoor.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.lbsapi.panodemo.R;
import com.baidu.lbsapi.panodemo.indoor.model.AlbumPicInfo;

/**
 * 相册控件
 */
public class PhotoAlbumView extends HorizontalScrollView {

    private static final int WIFI_BUFFER = 10;
    private static final int MOBILE_BUFFER = 3;

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));//减速率

    private static final int ALBUM_FINGER_UP = 0;
    private static final int PHOTOALBUM_STOP_SRCOLL = 1;
    /* X轴范围最大超出距离 */
    private static final int MAX_X_OVERSCROLL_DISTANCE = 200;
    /* 从属Activity */ Activity mContext;
    /* SSPhotoAlbumView中的横向布局 */
    public LinearLayout mLayout;
    /* 当前X轴位置 */
    private int mCurrentX;
    /* 允许最大滑动距离，以像素为单位 */
    private int mMaxOverScrollDistance;
    /* 第一张图片宽度，第二章图片宽度，上次载入前左边图片的index，上次载入前右边图片的index */
    private int mFirstWidth, mSecondWidth, mLastLeft, mLastRight;
    /* 物理系数 */
    private float mPhysicalCoeff;
    /* 判断移动方向true=向左移动,false=向右移动 */
    private boolean mIsMoveToLeft;
    /* 是否以计算过子控件宽度 */
    private boolean mIsWidthCalculated = false;
    /* 摩擦力 */
    private float mFlingFriction = ViewConfiguration.getScrollFriction();
    private Handler mHandler = new PhotoAlbumScrollStopedHandler(this);

    private int mInitialPosition = 0;

    private Bitmap mDefaultBitmap;
    /*所有照片的总点击监听器*/
    private SinglePhotoLayoutOnClickListener mSinglePhotoLayoutOnClickListener = new SinglePhotoLayoutOnClickListener();

    private List<AlbumPicInfo> mLstPic = new ArrayList<AlbumPicInfo>();

    private PhotoAlbumOnClickListener mOnClickListener;

    public PhotoAlbumView(Context context) {
        super(context);
        mContext = (Activity) context;
    }

    public PhotoAlbumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;
        final float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
        mPhysicalCoeff = SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * ppi * 0.84f; // look and feel tuning
        mDefaultBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.baidupano_photo_default);
    }

    public void setOnPhotoClickListener(PhotoAlbumOnClickListener listener) {
        mOnClickListener = listener;
    }

    private String formUrlString(String sid) {
        return "http://pcsv1.map.bdimg.com/scape/?qt=pdata&sid=" + sid + "&pos=0_0&z=0";
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        //记录当前X轴位置
        mCurrentX = l;
        //判断滑动方向
        if (oldl - l < 0) {
            mIsMoveToLeft = true;
        } else if (oldl - l > 0) {
            mIsMoveToLeft = false;
        }

        mHandler.removeMessages(PHOTOALBUM_STOP_SRCOLL);
        mHandler.sendEmptyMessageDelayed(PHOTOALBUM_STOP_SRCOLL, 50);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLayout = (LinearLayout) findViewById(R.id.horizontal_layout);
        mMaxOverScrollDistance = (int) mContext.getResources().getDisplayMetrics().density * MAX_X_OVERSCROLL_DISTANCE;

    }

    private void resetPhotoAlbum() {
        mLayout.removeAllViews();
        smoothScrollTo(0, 0);
        mCurrentX = 0;
        mLastLeft = 0;
        mLastRight = 0;
    }

    public void updatePhotoAlbum(ArrayList<HashMap<String, String>> lstPic) {

    }

    public void updateUi(List<AlbumPicInfo> lstPic) {
        mLstPic.clear();
        mLstPic.addAll(lstPic);
        /* 将相册中的旧图片清除 */
        resetPhotoAlbum();
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        for (int i = 0; i < mLstPic.size(); i++) {
            AlbumPicInfo picInfo = mLstPic.get(i);
            SinglePhotoLayout singlePhotoLayout = (SinglePhotoLayout) mInflater.inflate(R.layout.baidupano_singlephoto_layout, null);
            singlePhotoLayout.setOnClickListener(mSinglePhotoLayoutOnClickListener);

            if (picInfo.isExit()) {
                singlePhotoLayout.setHighLight(false);
                //                LinearLayout photoframeLayout = (LinearLayout) singlePhotoLayout.findViewById(R.id.photoFrame);
                //                photoframeLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

                ImageView maskImg = (ImageView) singlePhotoLayout.findViewById(R.id.mask_img);
                maskImg.setVisibility(View.VISIBLE);

                TextView mTextView = (TextView) singlePhotoLayout.findViewById(R.id.textAsync);
                mTextView.setVisibility(View.GONE);
                //                singlePhotoLayout.setName(mLstPic.get(i).get(
                //                        StreetscapeConst.SS_PHOTO_NAME_KEY));
                singlePhotoLayout.setImageUrl(this.formUrlString(mLstPic.get(i).getPid()));
            } else {
                singlePhotoLayout.setHighLight(false);
                ImageView maskImg = (ImageView) singlePhotoLayout.findViewById(R.id.mask_img);
                maskImg.setVisibility(View.GONE);

                singlePhotoLayout.setName(mLstPic.get(i).getInfo());
                singlePhotoLayout.setImageUrl(this.formUrlString(mLstPic.get(i).getPid()));
            }
            mLayout.addView(singlePhotoLayout);
        }

        if (!mIsWidthCalculated) {
            mLayout.post(new Runnable() {
                @Override
                public void run() {
                    if (mLstPic.size() > 1) {
                        mFirstWidth = mLayout.getChildAt(0).getWidth();
                        mSecondWidth = mLayout.getChildAt(1).getWidth();
                        mLastLeft = getCurrentLeft();
                        mLastRight = getCurrentRight();
                        mIsWidthCalculated = true;
                    }
                }
            });
        } else {
        }

    }

    private int getCurrentLeft() {
        if (mLayout != null && mLstPic.size() > 1) {
            if (mCurrentX < mFirstWidth) {
                return 0;
            } else {
                if (mSecondWidth == 0) {
                    return 0;
                } else {
                    int temp = (mCurrentX - mFirstWidth) / mSecondWidth;
                    return temp + 1;
                }
            }
        }
        return 0;
    }

    private int getCurrentRight() {
        if (mLayout != null && mLayout.getMeasuredWidth() <= this.getMeasuredWidth()) {
            return mLstPic.size() - 1;
        } else if (mLstPic.size() > 1) {
            int temp = Math.min(mLayout.getMeasuredWidth(), mCurrentX + this.getMeasuredWidth());
            if (mSecondWidth == 0) {
                return 0;
            }
            int offset = ((temp - mFirstWidth) % mSecondWidth) == 0 ? 1 : 2;
            return ((temp - mFirstWidth) / mSecondWidth) + offset;
        }
        return 0;
    }

    private static class PhotoAlbumScrollStopedHandler extends Handler {

        private final WeakReference<PhotoAlbumView> mView;

        private PhotoAlbumScrollStopedHandler(PhotoAlbumView view) {
            mView = new WeakReference<PhotoAlbumView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            PhotoAlbumView photoAlbumView = mView.get();
            if (photoAlbumView == null) {
                return;
            }
            switch (msg.what) {
                case ALBUM_FINGER_UP:
                    //得到停止滚动消息后，求第一张图宽度
                    int firstWidth = photoAlbumView.mLayout.getChildAt(0).getWidth();
                    int secondWidth = 0;
                    if (photoAlbumView.mLstPic.size() > 1) {
                        //求第二章图宽度
                        secondWidth = photoAlbumView.mLayout.getChildAt(1).getWidth();
                        //求偏移量
                        int offset;
                        if (secondWidth == 0) {
                            offset = 0;
                        } else {
                            offset = (photoAlbumView.mCurrentX - firstWidth) % secondWidth;
                        }
                        if (photoAlbumView.mIsMoveToLeft) {
                            //向右滑动左对齐
                            if (photoAlbumView.mCurrentX <= firstWidth) {
                                if (firstWidth == 0) {
                                    offset = 0;
                                } else {
                                    offset = photoAlbumView.mCurrentX % firstWidth;
                                }
                                photoAlbumView.smoothScrollBy(firstWidth - offset, 0);
                            } else if (photoAlbumView.getWidth() + photoAlbumView.mCurrentX < photoAlbumView.mLayout.getWidth()) {
                                photoAlbumView.smoothScrollBy(secondWidth - offset, 0);
                            }
                        } else {
                            if (photoAlbumView.mCurrentX <= firstWidth) {
                                //如到达第一张图片则滑动到起始位置
                                photoAlbumView.smoothScrollTo(0, 0);
                            } else {
                                //向左滑动左对齐
                                photoAlbumView.smoothScrollBy(-offset, 0);
                            }

                        }
                    }
                    break;
                case PHOTOALBUM_STOP_SRCOLL:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    }

    /**
     * 重写fling方法达到左对齐效果
     *
     * @param velocityX 加速度
     */
    @Override
    public void fling(int velocityX) {
        //为防止滑动过快，原加速度除4
        int newVelocityX = velocityX / 4;
        //如监听到fling则移除相册停止消息
        mHandler.removeMessages(ALBUM_FINGER_UP);

        //得到第一张图片宽度
        int firstWidth = mLayout.getChildAt(0).getWidth();
        int secondWidth = 0;
        if (mLstPic.size() > 1) {

            //得到现在X轴上位置
            final int currentX = this.mCurrentX;
            //得到第二张图片宽度
            secondWidth = mLayout.getChildAt(1).getWidth();
            int expDistance;

            // 计算在当前加速度下滑动距离
            int flingDistance = (int) getSplineFlingDistance(newVelocityX);
            if (velocityX > 0) {
                // 向左滑动时的预计停止位置
                expDistance = currentX + flingDistance;
                // 求出当前停止位置超出单张图片的距离
                int offset;
                if (secondWidth == 0) {
                    offset = 0;
                } else {
                    offset = (expDistance - firstWidth) % secondWidth;
                }
                if (offset != 0) {
                    // 求出达到左对齐时的滑动距离
                    int newDistance;
                    if (expDistance <= firstWidth) {
                        newDistance = firstWidth - currentX;
                    } else {
                        newDistance = flingDistance + (secondWidth - Math.abs(offset));
                    }
                    newVelocityX = getVelocity(newDistance);
                }
            } else {
                // 向右滑动同上
                expDistance = currentX - flingDistance;
                int offset;
                if (secondWidth == 0) {
                    offset = 0;
                } else {
                    offset = (expDistance - firstWidth) % secondWidth;
                }
                if (offset != 0) {
                    int newDistance;
                    if (expDistance <= firstWidth) {
                        newDistance = flingDistance + expDistance;
                    } else {
                        newDistance = flingDistance + Math.abs(offset);
                    }
                    newVelocityX = -getVelocity(newDistance);
                }
            }
        }
        super.fling(newVelocityX);
    }

    /**
     * 获得弹簧减速率
     *
     * @param velocity 加速度
     * @return
     */
    private double getSplineDeceleration(int velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    /**
     * 获得滑动距离
     *
     * @param velocity 速度
     * @return
     */
    private double getSplineFlingDistance(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }

    /**
     * 根据距离求出应有的速度
     *
     * @param distance
     * @return
     */
    private int getVelocity(double distance) {
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        double l = (decelMinusOne * Math.log(distance / (mFlingFriction * mPhysicalCoeff))) / DECELERATION_RATE;
        return (int) (Math.exp(l) * mFlingFriction * mPhysicalCoeff / INFLEXION);
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        //重新定义最远滚动距离
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxOverScrollDistance, maxOverScrollY, isTouchEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                //手指离开100微妙后发出停止滚动消息
                Message msg = Message.obtain(mHandler, ALBUM_FINGER_UP, (int) ev.getRawX());
                mHandler.sendMessageDelayed(msg, 100);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setSinglePhotoHighLight(int index) {
        for (int i = 0; i < mLayout.getChildCount(); i++) {
            if (i == index) {
                ((SinglePhotoLayout) mLayout.getChildAt(i)).setHighLight(true);
            } else if (((SinglePhotoLayout) mLayout.getChildAt(i)).mIsHighlighted) {
                ((SinglePhotoLayout) mLayout.getChildAt(i)).setHighLight(false);
            }
        }
        if (index != 0) {
            mInitialPosition = index;
        }
    }

    public void smoothScrollToPosition(final int pos) {
        post(new Runnable() {
            @Override
            public void run() {
                smoothScrollTo(mFirstWidth + (pos - 1) * mSecondWidth, 0);
            }
        });
    }

    public void scrollToInitialPosition() {
        if (mInitialPosition != 0) {
            if (mInitialPosition == 1) {
                scrollTo(mFirstWidth, 0);
            } else {
                int length = mFirstWidth + (mInitialPosition - 1) * mSecondWidth;
                scrollTo(length, 0);
            }
            mInitialPosition = 0;
        }
    }

    class SinglePhotoLayoutOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int index = 0;
            for (int j = 0; j < mLayout.getChildCount(); j++) {
                if (v == mLayout.getChildAt(j)) {
                    index = j;
                    break;
                }
            }
            checkByIndex(index);
        }
    }

    private void checkByIndex(int index) {
        for (int j = 0; j < mLayout.getChildCount(); j++) {
            if (j == index) {
                if (mLstPic != null && mLstPic.size() > 0 && mLstPic.get(j) != null) {
                    AlbumPicInfo picInfo = mLstPic.get(j);
                    if (picInfo != null) {
                        if (picInfo.isExit()) {
                            mOnClickListener.onItemClicked(picInfo);
                        } else {
                            ((SinglePhotoLayout) mLayout.getChildAt(j)).setHighLight(true);
                            mOnClickListener.onItemClicked(picInfo);
                        }
                    }
                }
            } else {
                if (((SinglePhotoLayout) mLayout.getChildAt(j)).mIsHighlighted) {
                    ((SinglePhotoLayout) mLayout.getChildAt(j)).setHighLight(false);
                }
            }
        }
    }
}
