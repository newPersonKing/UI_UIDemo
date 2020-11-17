package com.pengyeah.switchbutton.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class SmartisianSwitchButton extends View {

    final static String TAG = SmartisianSwitchButton.class.getSimpleName();

    /**
     * 控件宽高
     */
    int width, height;

    /**
     * 阴影颜色
     */
    @ColorInt
    int shadowColor = Color.GRAY;

    /**
     * 指示器阴影大小
     */
    int indicatorShadowSize = 6;

    /**
     * 指示器半径
     */
    int indicatorR;

    /**
     * 打开、关闭标识颜色
     */
    @ColorInt
    int onColor = Color.parseColor("#ff9ab9ff");
    @ColorInt
    int offColor = Color.parseColor("#fff5f5f5");

    /**
     * 指示器颜色
     */
    @ColorInt
    int indicatorColor = Color.WHITE;

    /**
     * 指示器阴影距离
     */
    int indicatorShadowDistance = 24;

    /**
     * 背景圆角矩形的内阴影距离
     */
    int backgroundAreaShadowDistance = 48;

    /**
     * 背景圆角矩形的内阴影大小
     */
    int backgroundAreaShadowSize = 24;

    /**
     * 背景圆角矩形的宽高
     */
    int backgroundAreaW, backgroundAreaH;

    @ColorInt
    int backgroundAreaColor = Color.WHITE;

    int indicatorX;
    int indicatorXOffset;


    Paint indicatorPaint, backgroundAreaPaint, flagPaint;

    /**
     * 阴影偏移量
     */
    int shadowOffset;

    /**
     * 开关状态
     */
    boolean isChecked;

    public SmartisianSwitchButton(Context context) {
        this(context, null);
    }

    public SmartisianSwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setAntiAlias(true);

        backgroundAreaPaint = new Paint();
        backgroundAreaPaint.setColor(backgroundAreaColor);
        backgroundAreaPaint.setAntiAlias(true);

        flagPaint = new Paint();
        flagPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;

        this.backgroundAreaW = this.width * 3 / 4;
        this.backgroundAreaH = this.height / 2;

        this.indicatorR = backgroundAreaH / 2;

        indicatorX = ((width - backgroundAreaW) / 2) + indicatorR;

        Log.i(TAG, "indicatorR==>" + indicatorR);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        drawFlag(canvas);

        drawBackgroundArea(canvas);

        drawIndicator(canvas);

    }

    /**
     * 绘制当前是on还是off的圆圈标识
     *
     * @param canvas
     */
    void drawFlag(Canvas canvas) {

        //首先裁剪出背景圆角矩形画布
        canvas.save();
        RectF rectF = new RectF((width - backgroundAreaW) / 2, (height - backgroundAreaH) / 2, (width - backgroundAreaW) / 2 + backgroundAreaW, (height - backgroundAreaH) / 2 + backgroundAreaH);
        Path bgAreaPath = new Path();
        bgAreaPath.addRoundRect(rectF, backgroundAreaH / 2, backgroundAreaH / 2, Path.Direction.CW);

        canvas.clipPath(bgAreaPath);


        //绘制on flag
        flagPaint.setStyle(Paint.Style.FILL);
        flagPaint.setColor(onColor);
        flagPaint.clearShadowLayer();
        canvas.drawCircle(indicatorX + indicatorXOffset - backgroundAreaW * 3 / 5, height / 2, indicatorR / 4, flagPaint);

        //内阴影
        flagPaint.setStyle(Paint.Style.STROKE);
        int onStrokeW = indicatorR / 4;
        flagPaint.setStrokeWidth(onStrokeW);
        flagPaint.setShadowLayer(onStrokeW, -onStrokeW, onStrokeW, onColor);

        Path onPath = new Path();
        onPath.addCircle(indicatorX + indicatorXOffset - backgroundAreaW * 3 / 5, height / 2, indicatorR / 4 + onStrokeW / 2, Path.Direction.CW);

        canvas.save();

        canvas.clipPath(onPath);
        canvas.drawPath(onPath, flagPaint);

        flagPaint.clearShadowLayer();

        canvas.restore();

        //绘制off flag
        flagPaint.setStyle(Paint.Style.FILL);
        flagPaint.setColor(offColor);
        canvas.drawCircle(indicatorX + indicatorXOffset + backgroundAreaW * 3 / 5, height / 2, indicatorR / 4, flagPaint);

        //内阴影
        flagPaint.setStyle(Paint.Style.STROKE);
        int offStrokeW = indicatorR / 4;
        flagPaint.setStrokeWidth(offStrokeW);
        flagPaint.setShadowLayer(offStrokeW, -offStrokeW, offStrokeW, offColor);

        Path offPath = new Path();
        offPath.addCircle(indicatorX + indicatorXOffset + backgroundAreaW * 3 / 5, height / 2, indicatorR / 4 + offStrokeW / 2, Path.Direction.CW);

        canvas.save();

        canvas.clipPath(offPath);
        canvas.drawPath(offPath, flagPaint);

        canvas.restore();

        canvas.restore();

    }

    void drawBackgroundArea(Canvas canvas) {

        //绘制边框及内阴影
        canvas.save();

        backgroundAreaPaint.setStyle(Paint.Style.STROKE);
        int strokeW = indicatorR / 2;
        backgroundAreaPaint.setStrokeWidth(strokeW);
        backgroundAreaPaint.setColor(Color.parseColor("#ffbcbcbc"));
        backgroundAreaShadowSize = backgroundAreaH / 4;
        backgroundAreaShadowDistance = backgroundAreaH / 12;
        backgroundAreaPaint.setShadowLayer(backgroundAreaShadowSize + shadowOffset, 0, backgroundAreaShadowDistance, Color.GRAY);

        RectF strokeRectF = new RectF(-strokeW + (width - backgroundAreaW) / 2, -strokeW + (height - backgroundAreaH) / 2, strokeW + (width - backgroundAreaW) / 2 + backgroundAreaW, strokeW + (height - backgroundAreaH) / 2 + backgroundAreaH);
        Path strokePath = new Path();
        strokePath.addRoundRect(strokeRectF, (backgroundAreaH + strokeW) / 2, (backgroundAreaH + strokeW) / 2, Path.Direction.CW);

        RectF rectF = new RectF((width - backgroundAreaW) / 2, (height - backgroundAreaH) / 2, (width - backgroundAreaW) / 2 + backgroundAreaW, (height - backgroundAreaH) / 2 + backgroundAreaH);
        Path path = new Path();
        path.addRoundRect(rectF, backgroundAreaH / 2, backgroundAreaH / 2, Path.Direction.CW);
        canvas.clipPath(path);

        canvas.drawPath(strokePath, backgroundAreaPaint);

        backgroundAreaPaint.setStrokeWidth(2);
        backgroundAreaPaint.clearShadowLayer();
        canvas.drawPath(path, backgroundAreaPaint);

        canvas.restore();


    }

    void drawIndicator(Canvas canvas) {
        //绘制外阴影
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStyle(Paint.Style.FILL);
        indicatorShadowSize = indicatorR / 3;
        indicatorShadowDistance = indicatorShadowSize / 2;
        indicatorPaint.setShadowLayer(indicatorShadowSize - shadowOffset, 0, indicatorShadowDistance, Color.parseColor("#ffc1c1c1"));
        canvas.drawCircle(indicatorX + indicatorXOffset, (height - backgroundAreaH) / 2 + indicatorR, indicatorR, indicatorPaint);

        //绘制内阴影
        canvas.save();

        indicatorPaint.setColor(Color.parseColor("#ffbcbcbc"));
        int strokeW = indicatorR / 2;
        indicatorPaint.setStrokeWidth(strokeW);
        indicatorPaint.setStyle(Paint.Style.STROKE);
        indicatorPaint.setShadowLayer(indicatorR / 3, -indicatorR / 6, -indicatorR / 6, Color.parseColor("#fff1f1f1"));

        Path strokePath = new Path();
        strokePath.addCircle(indicatorX + indicatorXOffset, (height - backgroundAreaH) / 2 + indicatorR, indicatorR + strokeW / 2, Path.Direction.CW);

        Path path = new Path();
        path.addCircle(indicatorX + indicatorXOffset, (height - backgroundAreaH) / 2 + indicatorR, indicatorR, Path.Direction.CW);
        canvas.clipPath(path);

        canvas.drawPath(strokePath, indicatorPaint);

        indicatorPaint.setStrokeWidth(2);
        indicatorPaint.clearShadowLayer();
        canvas.drawPath(path, indicatorPaint);

        canvas.restore();
    }

    int downX;

    ValueAnimator shadowAnimator, translateAnimator;

    /**
     * @param isChecked true==>移动至on状态；false==>移动至off状态
     */
    void startTranslateAnim(boolean isChecked) {
        if (translateAnimator != null) {
            translateAnimator.cancel();
        }
        if (isChecked == true) {
            translateAnimator = ValueAnimator.ofInt(indicatorX + indicatorXOffset, width - (width - backgroundAreaW) / 2 - indicatorR);
        } else {
            translateAnimator = ValueAnimator.ofInt(indicatorX + indicatorXOffset, (width - backgroundAreaW) / 2 + indicatorR);
        }
        translateAnimator.setDuration(200L);
        translateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                indicatorX = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        translateAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                //开始阴影变化动画
                if (shadowAnimator != null) {
                    shadowAnimator.cancel();
                }
                shadowAnimator = ValueAnimator.ofInt(0, indicatorR / 4);
                shadowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        shadowOffset = (int) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                shadowAnimator.setDuration(200L);
                shadowAnimator.start();
                break;
            case MotionEvent.ACTION_UP:
                downX = 0;
                indicatorX = indicatorX + indicatorXOffset;
                //开始阴影变化动画
                if (shadowAnimator != null) {
                    shadowAnimator.cancel();
                }
                shadowAnimator = ValueAnimator.ofInt(indicatorR / 4, 0);
                shadowAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        shadowOffset = (int) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                shadowAnimator.setDuration(200L);
                shadowAnimator.start();
                //移动动画,切换动画
                if (Math.abs(indicatorXOffset) <= 20) {
                    //视作点击
                    isChecked = !isChecked;
                    startTranslateAnim(isChecked);
                } else if ((indicatorXOffset > 0 && indicatorXOffset >= (backgroundAreaW - 2 * indicatorR) / 2) || (indicatorXOffset < 0 && indicatorXOffset > -(backgroundAreaW - 2 * indicatorR) / 2)) {
                    indicatorXOffset = 0;
                    //切换状态:ON
                    isChecked = true;
                    startTranslateAnim(true);
                } else if ((indicatorXOffset > 0 && indicatorXOffset < (backgroundAreaW - 2 * indicatorR) / 2) || (indicatorXOffset < 0 && indicatorXOffset <= -(backgroundAreaW - 2 * indicatorR) / 2)) {
                    indicatorXOffset = 0;
                    //切换状态:OFF
                    isChecked = false;
                    startTranslateAnim(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                indicatorXOffset = (int) (event.getX() - downX);
                //边界判断
                if (indicatorX + indicatorXOffset <= (width - backgroundAreaW) / 2 + indicatorR) {
                    indicatorXOffset = (width - backgroundAreaW) / 2 + indicatorR - indicatorX;
                } else if (indicatorX + indicatorXOffset >= width - (width - backgroundAreaW) / 2 - indicatorR) {
                    indicatorXOffset = width - (width - backgroundAreaW) / 2 - indicatorR - indicatorX;
                }
                break;
        }
        postInvalidate();
        return true;
    }
}
