# AndroidGroupShadow
一种在android平台实现阴影的方式，没有padding，没有margin，超简单，超实用的实现方式。

## 前言
这个迭代，UI在给了几张带阴影的图片，那种阴影范围很大，实际内容却只有一点的图片。  
效果类似这样。
![](https://user-gold-cdn.xitu.io/2019/6/27/16b99329afde43f4?w=657&h=544&f=png&s=28185)

不知道这张图有没有表达清楚，就是那种图片之间阴影需要重叠才能使内容对其，阴影还有颜色的效果。  
Android 5.0后才支持elevation属性，还不支持阴影颜色的设定。IOS同事笑了，他们说直接把阴影效果给他们，不要带阴影的图片，他们天然支持阴影，可以直接绘制。  

于是，上网搜索，发现目前Andorid 平台实现阴影大概有这么几种方式  
**1、使用.9图 https://inloop.github.io/shadow4android/**    
**2、CardView 不支持阴影颜色**  
**3、开源库ShadowLayout**  
**4、模仿FloatingActionButton 实现阴影**等等。   
这些方式是可以实现阴影的显示，但是基本都是将阴影作为控件的一部分去实现的。这样，就需要给控件设置一些padding值，才能让阴影显示出来。这种方式使得布局很不方便对其。 

## 我的解决方案
先上效果看看

![](https://user-gold-cdn.xitu.io/2019/6/27/16b99746d7cc84f9?w=467&h=960&f=jpeg&s=15441)


既然将阴影作为控件的一部分去实现不利于控件的布局和对其，那就咱就在ViewGroup里去实现阴影。绘制的时候根据子view的位置绘制出阴影，这样就不会影响控件的布局和对其了。  

**其实我觉得控件的阴影天然就应该在父布局去实现，就像现实中的阴影那样。**

## 实现思路

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

3、2那种绘制方式边缘有些整齐，可能会不满足需求。这里还有一种方式，为 Paint设置MaskFilter。 具体可以看看这篇文章 https://www.cnblogs.com/tianzhijiexian/p/4297734.html

## 我的实现
我个人比较喜欢使用ConstraintLayout等等，所以继承ConstraintLayout实现了一个demo，感觉效果还不错。

**使用起来大概像这样。**
![](https://user-gold-cdn.xitu.io/2019/6/27/16b9998cf04439a2?w=782&h=637&f=png&s=98231)


**项目地址 https://github.com/ZhangHao555/AndroidGroupShadow**

## 需要改进
1、绘制阴影的时候有没有更好、更逼真的绘制方式？  
2、项目中使用自定义属性 lint会报错 我只是强制屏蔽了。
![](https://user-gold-cdn.xitu.io/2019/6/27/16b99970b2f76a3c?w=640&h=661&f=png&s=92177)
有没有更好的解决方式？

**希望大家能分享分享！**


## 另外 附上另一篇文章 RecyclerView实现banner 滑动缩放、循环滑动效果   https://juejin.im/post/5d05df5ef265da1bac4015ca




