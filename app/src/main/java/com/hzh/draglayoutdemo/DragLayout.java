package com.hzh.draglayoutdemo;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class DragLayout extends FrameLayout{
    private ViewDragHelper mViewDragHelper;
    private View mDragView;
    private Context mContext;
    private OnClickListener mOnClickListener;
    public DragLayout(Context context) {
        this(context,null);
    }

    public DragLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if(left <0){
                    //限制左边界
                    left = 0;
                }else if (left > (getMeasuredWidth() - child.getMeasuredWidth())){
                    //限制右边界
                    left = getMeasuredWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                if(top <0){
                    //限制上边界
                    top = 0;
                }else if (top > (getMeasuredHeight() - child.getMeasuredHeight())){
                    //限制下边界
                    top = getMeasuredHeight() - child.getMeasuredHeight();
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mDragView){
                    mViewDragHelper.smoothSlideViewTo(releasedChild,getMeasuredWidth() - releasedChild.getMeasuredWidth(),releasedChild.getTop());
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mDragView.layout(getWidth()-mDragView.getWidth(),getHeight()-mDragView.getHeight(),getWidth(),getHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(DragLayout.this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mDragView = getChildAt(1);
        mDragView = LayoutInflater.from(mContext).inflate(R.layout.kefu, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DensityUtil.dip2px(mContext,52), DensityUtil.dip2px(mContext,52));
        this.addView(mDragView,params);
        mDragView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onClick(v);
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
