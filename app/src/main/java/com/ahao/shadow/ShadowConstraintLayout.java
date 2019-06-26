package com.ahao.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ShadowConstraintLayout extends ConstraintLayout {

    Paint paint;
    RectF rectF = new RectF();
    Matrix matrix;

    public ShadowConstraintLayout(Context context) {
        this(context, null);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            if (layoutParams instanceof ShadowConstraintLayout.LayoutParams) {
                ShadowConstraintLayout.LayoutParams shadowLayoutParams = (LayoutParams) layoutParams;
                if (shadowLayoutParams.shadowRadius > 0 && shadowLayoutParams.shadowColor != Color.TRANSPARENT) {
                    rectF.left = child.getLeft();
                    rectF.right = child.getRight();
                    rectF.top = child.getTop();
                    rectF.bottom = child.getBottom();
                    paint.setStyle(Paint.Style.FILL);
                    paint.setShadowLayer(shadowLayoutParams.shadowRadius, shadowLayoutParams.xOffset
                            , shadowLayoutParams.yOffset, shadowLayoutParams.shadowColor);
                    paint.setColor(shadowLayoutParams.shadowColor);
                    canvas.drawRoundRect(rectF, shadowLayoutParams.shadowRoundRadius, shadowLayoutParams.shadowRoundRadius, paint);
                    paint.setShadowLayer(0,0,0,0);

                    rectF.left = child.getLeft() - shadowLayoutParams.shadowRadius;
                    rectF.right = child.getRight() + shadowLayoutParams.shadowRadius;
                    rectF.top = child.getTop() - shadowLayoutParams.shadowRadius;
                    rectF.bottom = child.getBottom() + shadowLayoutParams.shadowRadius;
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRoundRect(rectF, shadowLayoutParams.shadowRoundRadius, shadowLayoutParams.shadowRoundRadius, paint);

                }
            }
        }
    }


    public static class LayoutParams extends ConstraintLayout.LayoutParams {

        private float xOffset;
        private float yOffset;
        private int shadowColor;
        private float shadowRadius;
        private float shadowRoundRadius;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ShadowConstraintLayout.LayoutParams source) {
            super(source);
            xOffset = source.getXOffset();
            yOffset = source.getYOffset();
            shadowColor = source.getShadowColor();
            shadowRadius = source.getShadowRadius();
            shadowRoundRadius = source.getShadowRoundRadius();
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray attributes = c.obtainStyledAttributes(attrs, R.styleable.ShadowConstraintLayout);
            xOffset = attributes.getDimension(R.styleable.ShadowConstraintLayout_xOffset, 0);
            yOffset = attributes.getDimension(R.styleable.ShadowConstraintLayout_yOffset, 0);
            shadowRadius = attributes.getDimension(R.styleable.ShadowConstraintLayout_shadowRadius, 0);
            shadowColor = attributes.getColor(R.styleable.ShadowConstraintLayout_shadowColor, 0);
            shadowRoundRadius = attributes.getDimension(R.styleable.ShadowConstraintLayout_shadowRoundRadius, 0);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public float getXOffset() {
            return xOffset;
        }

        public void setXOffset(float xOffset) {
            this.xOffset = xOffset;
        }

        public float getYOffset() {
            return yOffset;
        }

        public void setYOffset(float yOffset) {
            this.yOffset = yOffset;
        }

        public int getShadowColor() {
            return shadowColor;
        }

        public void setShadowColor(int shadowColor) {
            this.shadowColor = shadowColor;
        }

        public float getShadowRadius() {
            return shadowRadius;
        }

        public void setShadowRadius(float shadowRadius) {
            this.shadowRadius = shadowRadius;
        }

        public float getShadowRoundRadius() {
            return shadowRoundRadius;
        }

        public void setShadowRoundRadius(float shadowRoundRadius) {
            this.shadowRoundRadius = shadowRoundRadius;
        }
    }

    public ShadowConstraintLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ShadowConstraintLayout.LayoutParams(this.getContext(), attrs);
    }

    protected ShadowConstraintLayout.LayoutParams generateDefaultLayoutParams() {
        return new ShadowConstraintLayout.LayoutParams(-2, -2);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new ShadowConstraintLayout.LayoutParams(p);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof ShadowConstraintLayout.LayoutParams;
    }
}