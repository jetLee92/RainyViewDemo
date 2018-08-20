# RainyViewDemo
Android 仿七夕爱心雨，单身狗雨

前几天七夕情人节，街上满是狗粮，连电影选座也来蹭热点，被单身狗雨和爱心雨刷屏了。这效果看着不错，实现起来也不难，决定自己仿一波。

开始之前先上效果图

![完整效果图](https://upload-images.jianshu.io/upload_images/5596129-46fb3939e4300892.gif?imageMogr2/auto-orient/strip)

#### 细节拆分

1、每一个view从顶部落到底部，然后从底部消失。

2、每一个view在X轴上的初始随机位置开始落下。

3、每一个view随机初始角度，并且落下过程中旋转。

4、每一个view初始落下的高度随机，但最终都会从底部消失。

#### 实现步骤

首先，实现一个view的下落过程，这里我内置了一个属性动画。

	// 效果系数
    private float rainyFraction;

	public void startAnimator() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "rainyFraction", 0, 1);
            objectAnimator.setDuration(6000);
        }
        objectAnimator.start();
    }

    public float getRainyFraction() {
        return rainyFraction;
    }

    public void setRainyFraction(float rainyFraction) {
        this.rainyFraction = rainyFraction;
        invalidate();
    }

然后重写onSizeChanged和onDraw方法，得到整个view的宽高，再来绘制bitmap

    float dx；

	@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

	@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        // 重置matrix，必须重置不然会叠加
        matrix.reset();
		dx = width/2；
        float dy = height * rainyFraction;
        matrix.postTranslate(width/2, dy);
        canvas.drawBitmap(bitmap, matrix, paint);
        canvas.restore();
    }

![下落效果](https://upload-images.jianshu.io/upload_images/5596129-d80037e62ad3e354.gif?imageMogr2/auto-orient/strip)

落下的效果有了，现在考虑在X轴随机位置的问题，获取初始X轴位置的方法如下：

	// 减去图片大小是为了不超出
	float dx = (float) ((width - imageSize) * Math.random())；

把matrix的dx替换就行了，下来图片旋转可以用matrix.postRotate(degrees, px, py)这个方法，计算出初始角度就ok了

	// dx和dy都在上面计算过，把这两句代码加上去就好
	int angle = (int) (Math.random() * 360)；
	// 加上落下的系数，就可以实现落下并旋转了
	matrix.postRotate（180 * rainyFraction + angle, imageSize / 2 + dx, imageSize / 2 + dy）；

最后把随机初始高度的计算也简单

	// 距离顶部的最大高度设为自身高度，获取随机值。
	float tempHeight = (float) (height * Math.random());
	// 这里的最大高度，就是在顶部的上面的距离，最后图片的高度，而这个高度+height就是整个落下的总高度，后面做多个图落下的时候用到
	maxHeight = Math.max(tempHeight, maxHeight);

![下落并旋转效果](https://upload-images.jianshu.io/upload_images/5596129-bf658559c9dd138a.gif?imageMogr2/auto-orient/strip)

好了，到这里一个图的落下就完成了。接下来就是多个图的效果，怎么做？for循环！这里我是准备了三个list保存对应的初始X位置、Y位置以及初始的角度，看代码：

	public void initCount() {
        widthArr.clear();
        heightArr.clear();
        angles.clear();
        for (int i = 0; i < num; i++) {
            // 随机横向x的位置
            widthArr.add((float) ((width - imageSize) * Math.random()));
            float tempHeight = (float) (height * Math.random());
            // 随机的距离顶部的高度，是负值。加上imageSize是为了保证从顶部开始落下
            heightArr.add(-(imageSize + tempHeight));
            maxHeight = Math.max(tempHeight, maxHeight);
            // 随机初始角度
            angles.add((int) (Math.random() * 360));
        }
    }

同样在**onDraw**方法中也是for循坏使用这些值

	for (int i = 0; i < num; i++) {
        // 重置matrix，必须重置不然会叠加
        if (num != widthArr.size()) {
            break;
        }
        matrix.reset();
        // 当前的高度 = 随机的初始高度（负值） + （view的高度 + 最高的狗头所在的高度 == 整个动画的高度）*系数
        // 加上2 * imageSize是为了保证狗头都落下底部外
        float dy = heightArr.get(i) + (maxHeight + height + 2 * imageSize) * rainyFraction;
        matrix.postTranslate(widthArr.get(i), dy);
        // 以狗头为中心旋转
        matrix.postRotate(angles.get(i) + 180 * rainyFraction, imageSize / 2 + widthArr.get(i), imageSize / 2 + dy);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

最后在activity使用RainyView

	rainyView.initCount();
	rainyView.startAnimator();

这个效果的完成到这里就结束了。

##### 写在最后

这是GitHub地址[**单身狗雨**](https://github.com/jetLee92/RainyViewDemo)，完整代码可以从上面fork下来。喜欢的给个star呗，谢谢。

另外说一下，这个下落的效果很粗暴，哈哈。而且没有把落下的速度计算出来，每个view的速度都一样的。不过这些都没关系，重要的是学习到里面的精髓。