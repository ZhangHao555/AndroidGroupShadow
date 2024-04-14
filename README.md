
### 1、继承ViewGoup

Android 中有FrameLayout、LinearLayout、RelativeLayout、ConstraintLayout等等，这些layout是为了进行子view布局而设计的，如果不进行背景色的设置，默认是不走ondraw方法的。

我们可以继承这些ViewGroup，设置setWillNotDraw(false)标志位强制进行绘制，这样我们就既拥有了布局的功能，也拥有了绘制的功能。然后就能在onDraw方法中，根据子view的位置绘制背景了。

### 2、复写onDraw方法绘制子view背景
这一步很简单，一个循环遍历子view就好了

#### 如何绘制
1、绘制之前 需要设置 setLayerType(View.LAYER_TYPE_SOFTWARE, null)关闭硬件加速，因为一些高级绘制方法可能不支持硬件加速。
2、为paint设置 shadowLayer
```
  paint.setShadowLayer(shadowLayoutParams.shadowRadius, shadowLayoutParams.xOffset
                            , shadowLayoutParams.yOffset, shadowLayoutParams.shadowColor);
```

### 3、最后的代码

```
public class ShadowConstraintLayout extends ConstraintLayout {
    Paint paint;
    RectF rectF = new RectF();

    public ShadowConstraintLayout(Context context) {
        this(context, null);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 关键点，使ViewGroup调用onDraw方法
        setWillNotDraw(false);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int childCount = getChildCount();

        // 根据参数在父布局绘制背景
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
                }
            }
        }
    }

    public static class LayoutParams extends ConstraintLayout.LayoutParams {
        private float xOffset;
        private float yOffset;
        // 阴影颜色
        private int shadowColor;
        // 阴影大小
        private float shadowRadius;
        // 阴影圆角大小
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
            TypedArray attributes = c.obtainStyledAttributes(attrs, R.styleable.ShadowConstraintLayout_Layout);
            xOffset = attributes.getDimension(R.styleable.ShadowConstraintLayout_Layout_layout_xOffset, 0);
            yOffset = attributes.getDimension(R.styleable.ShadowConstraintLayout_Layout_layout_yOffset, 0);
            shadowRadius = attributes.getDimension(R.styleable.ShadowConstraintLayout_Layout_layout_shadowRadius, 0);
            shadowColor = attributes.getColor(R.styleable.ShadowConstraintLayout_Layout_layout_shadowColor, 0);
            shadowRoundRadius = attributes.getDimension(R.styleable.ShadowConstraintLayout_Layout_layout_shadowRoundRadius, 0);
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

    public ShadowConstraintLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ShadowConstraintLayout.LayoutParams(this.getContext(), attrs);
    }

    protected ShadowConstraintLayout.LayoutParams generateDefaultLayoutParams() {
        return new ShadowConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new ShadowConstraintLayout.LayoutParams(p);
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof ShadowConstraintLayout.LayoutParams;
    }
}
```

**使用起来大概像这样。**
```
<com.ahao.shadowlayout.ShadowConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto">


    <TextView
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:layout_centerHorizontal="true"
        android:background="@drawable/blue_round_bg"
        app:layout_shadowColor="#805468A5"
        app:layout_shadowRadius="8dp"
        app:layout_shadowRoundRadius="5dp"
        app:layout_yOffset="2dp" />
</com.ahao.shadowlayout.ShadowConstraintLayout>
```



### 目前已实现 FrameLayout、LinearLayout、RelativeLayout、ConstraintLayout等等
**项目地址 https://github.com/ZhangHao555/AndroidGroupShadow**
### 添加依赖     implementation 'com.github.ZhangHao555:AndroidGroupShadow:v1.0'




