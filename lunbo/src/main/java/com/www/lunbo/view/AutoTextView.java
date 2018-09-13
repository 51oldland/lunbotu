package com.www.lunbo.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.graphics.Camera;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

/**
 * Created by wwp
 * DATE: 2018/5/3:18:55
 * Copyright: 中国自主招生网 All rights reserved
 * Description:
 */

public class AutoTextView extends TextSwitcher implements
        ViewSwitcher.ViewFactory {

    private float mHeight = 10;
    private Context mContext;
    //mInUp,mOutUp分别构成向下翻页的进出动画
    private Animation mInUp;
    private Animation mOutUp;

    //mInDown,mOutDown分别构成向下翻页的进出动画
    private Animation mInDown;
    private Animation mOutDown;

    public AutoTextView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        mContext = context;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        setFactory(this);
        mInUp = createAnim(0, 0 , true, true);
        mOutUp = createAnim(0, 0, false, true);
        mInDown = createAnim(90, 0 , true , false);
        mOutDown = createAnim(0, -90, false, false);
        setInAnimation(mInUp);
        setOutAnimation(mOutUp);
    }

    private Animation createAnim(float start, float end, boolean turnIn, boolean turnUp){
        final Animation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(1000);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }

    //这里返回的TextView，就是我们看到的View
    @Override
    public View makeView() {
        // TODO Auto-generated method stub
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.CENTER_VERTICAL);
        t.setTextSize(mHeight);
        t.setMaxLines(1);
        return t;
    }
    //定义动作，向下滚动翻页
    public void previous(){
        if(getInAnimation() != mInDown){
            setInAnimation(mInDown);
        }
        if(getOutAnimation() != mOutDown){
            setOutAnimation(mOutDown);
        }
    }
    //定义动作，向上滚动翻页
    public void next(){
        if(getOutAnimation() != mOutUp){
            setOutAnimation(mOutUp);
        }
        if(getInAnimation() != mInUp){
            setInAnimation(mInUp);
        }
    }

    class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;

        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = getHeight();
            mCenterX = getWidth() / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

            final float centerX = mCenterX ;
            final float centerY = mCenterY ;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1: -1;

            final Matrix matrix = t.getMatrix();

            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();

            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
