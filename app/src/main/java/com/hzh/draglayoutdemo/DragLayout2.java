package com.hzh.draglayoutdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/12/27 0027.
 */

public class DragLayout2 extends FrameLayout {
    private Context mContext;
    private View mDragView;
    private float downX;
    private float downY;
    private OnClickListener mOnClickListener;
    private boolean isMove = false;
    public DragLayout2(Context context) {
        this(context,null);
    }

    public DragLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private void init() {
        mDragView.layout(getWidth()-mDragView.getWidth(),getHeight()-mDragView.getHeight(),getWidth(),getHeight());
        mDragView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        isMove = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float xDistance = event.getX() - downX;
                        float yDistance = event.getY() - downY;
                        if (xDistance != 0 && yDistance != 0) {
                            int l = (int) (v.getLeft() + xDistance);
                            int r = (int) (v.getRight() + xDistance);
                            int t = (int) (v.getTop() + yDistance);
                            int b = (int) (v.getBottom() + yDistance);
                            if (l < 0){
                                l = 0;
                                r = mDragView.getWidth();
                            }
                            if (r > getWidth()){
                                r = getWidth();
                                l = r - mDragView.getWidth();
                            }
                            if (t < 0){
                                t = 0;
                                b = mDragView.getHeight();
                            }
                            if (b > getHeight()){
                                b = getHeight();
                                t = b - mDragView.getHeight();
                            }
                            v.layout(l, t, r, b);
                            isMove = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        v.layout(getWidth()-mDragView.getWidth(),v.getTop(),getWidth(),v.getBottom());
                        if (isMove){
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        init();
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
}
