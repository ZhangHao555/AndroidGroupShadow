package com.ahao.shadow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ShadowLinearLayout extends LinearLayout {
    Paint paint;
    RectF rectF = new RectF();
    Matrix matrix;

    public ShadowLinearLayout(Context context) {
        this(context, null);
    }

    public ShadowLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
            if (layoutParams instanceof ShadowLinearLayout.LayoutParams) {
                ShadowLinearLayout.LayoutParams shadowLayoutParams = (LayoutParams) layoutParams;
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
                    paint.setShadowLayer(0, 0, 0, 0);
                }
            }
        }
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        private float xOffset;
        private float yOffset;
        private int shadowColor;
        private float shadowRadius;
        private float shadowRoundRadius;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ShadowLinearLayout.LayoutParams source) {
            super(source);
            xOffset = source.getXOffset();
            yOffset = source.getYOffset();
            shadowColor = source.getShadowColor();
            shadowRadius = source.getShadowRadius();
            shadowRoundRadius = source.getShadowRoundRadius();
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray attributes = c.obtainStyledAttributes(attrs, R.styleable.ShadowLinearLayout_Layout);
            xOffset = attributes.getDimension(R.styleable.ShadowLinearLayout_Layout_layout_xOffset, 0);
            yOffset = attributes.getDimension(R.styleable.ShadowLinearLayout_Layout_layout_yOffset, 0);
            shadowRadius = attributes.getDimension(R.styleable.ShadowLinearLayout_Layout_layout_shadowRadius, 0);
            shadowColor = attributes.getColor(R.styleable.ShadowLinearLayout_Layout_layout_shadowColor, 0);
            shadowRoundRadius = attributes.getDimension(R.styleable.ShadowLinearLayout_Layout_layout_shadowRoundRadius, 0);
            attributes.recycle();
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

    public ShadowLinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ShadowLinearLayout.LayoutParams(this.getContext(), attrs);
    }

    protected ShadowLinearLayout.LayoutParams generateDefaultLayoutParams() {
        if (getOrientation() == HORIZONTAL) {
            return new ShadowLinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else if (getOrientation() == VERTICAL) {
            return new ShadowLinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        return null;
    }

    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new ShadowLinearLayout.LayoutParams(p);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ShadowLinearLayout.LayoutParams;
    }
}
