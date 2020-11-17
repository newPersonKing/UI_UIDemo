package com.pengyeah.switchbutton.widget;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

import androidx.annotation.Nullable;

import com.pengyeah.switchbutton.utils.BitmapUtils;
import com.pengyeah.switchbutton.R;


/**
 * Created by pulan on 2017/8/24.
 * 性别选择控件
 */

public class SexSwitch extends View {

    private static final String TAG = "SexSwitch";
    Context mContext;

    boolean status = false;//当前开关状态,true==开=女，false==关=男

    float width, height;//控件宽高
    float indicatorW, indicatorH;//指示器宽高
    float indicatorX, indicatorY;//指示器坐标
    float indicatorStartX, indicatorStartY;//指示器初始坐标
    float indicatorEndX, indicatorEndY;//指示器最终目标坐标
    float shadowW, shadowH;//阴影宽高
    float shadowX, shadowY;
    Bitmap bmShadow;//指示器阴影
    float bkgBarW, bkgBarH;//背景条长宽

    //手指滑动操作
    float startX, detaX;
    float detaXMAX;//手指可横向滑动的最大距离

    ValueAnimator animatorOn, animatorOff;//动画插值计算器
    ValueAnimator animatorColorOn, animatorColorOff;//动画插值计算器
    AnimatorSet animOnSet, animOffSet;//false==>true动画合集

    Paint bkgBarPaint, indicatorPaint, shaowPaint, textPaint;
    float textTop, textBottom;
        int sex_blue = Color.parseColor("#ff0042FF");
    int sex_red = Color.parseColor("#ffFF0084");
//    int sex_blue = Color.parseColor("#ff00B64F");
//    int sex_red = Color.parseColor("#ffFF6700");

    public SexSwitch(Context context) {
        this(context, null);
    }

    public SexSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SexSwitch);
        width = a.getLayoutDimension(R.styleable.SexSwitch_android_layout_width, 236);
        height = a.getLayoutDimension(R.styleable.SexSwitch_android_layout_height, 104);
        indicatorW = width * 3 / 7;
        indicatorH = (7f / 13f) * height;
        shadowW = indicatorW + (height - indicatorH);
        shadowH = indicatorH + (height - indicatorH);
        bkgBarW = (3f / 5f) * width;
        bkgBarH = (1f / 7f) * height;

        indicatorStartX = (height - indicatorH) / 2;
        indicatorStartY = (height - indicatorH) / 2;
        indicatorX = indicatorStartX;
        indicatorY = indicatorStartY;
        indicatorEndX = width - (height - indicatorH) / 2 - indicatorW;
        indicatorEndY = indicatorStartY;
        indicatorY = indicatorStartY;

        //手指可滑动的最大距离
        detaXMAX = width - (indicatorW + (height - indicatorH));

        bkgBarPaint = new Paint();
        bkgBarPaint.setColor(sex_red);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(sex_blue);

        shaowPaint = new Paint();
        //字体
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(indicatorH / 2);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        textTop = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        textBottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        bmShadow = BitmapFactory.decodeResource(getResources(), R.drawable.img_shadow_rect_blue);
        bmShadow = BitmapUtils.replacePixelColor(bmShadow, sex_blue);

        //开启动画
        animatorColorOn = new ValueAnimator();
        animatorColorOn.setIntValues(sex_blue, sex_red);
        animatorColorOn.setEvaluator(new ArgbEvaluator());
        animatorColorOn.setDuration(500);
        animatorColorOn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                indicatorPaint.setColor(color);
            }
        });

        animatorOn = ValueAnimator.ofFloat(indicatorStartX, indicatorEndX);
        animatorOn.setDuration(500);
        animatorOn.setInterpolator(new BounceInterpolator());
        animatorOn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        animOnSet = new AnimatorSet();
        animOnSet.playTogether(animatorOn, animatorColorOn);

        //关闭动画
        animatorColorOff = new ValueAnimator();
        animatorColorOff.setIntValues(sex_red, sex_blue);
        animatorColorOff.setEvaluator(new ArgbEvaluator());
        animatorColorOff.setDuration(500);
        animatorColorOff.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                indicatorPaint.setColor(color);
            }
        });
        animatorOff = ValueAnimator.ofFloat(indicatorEndX, indicatorStartX);
        animatorOff.setDuration(500);
        animatorOff.setInterpolator(new BounceInterpolator());
        animatorOff.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indicatorX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animOffSet = new AnimatorSet();
        animOffSet.playTogether(animatorColorOff, animatorOff);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animOnSet != null || animOffSet != null) {
            if (animOnSet.isRunning() || animOffSet.isRunning()) {
                return true;
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
//                detaX = event.getX() - startX;
//                if (detaX >= detaXMAX) {
//                    detaX = detaXMAX;
//                }
//                Log.i(TAG, "detaX===>" + detaX);
                Log.i(TAG, "move===");
                break;
            case MotionEvent.ACTION_UP:
                //判断是否是click事件
                //如果是点击事件,改变控件开关状态
                if (status == false) {
                    status = true;
                    animOnSet.start();
                    //改变bkgBar颜色
                    bkgBarPaint.setColor(sex_blue);
                    //改变背景阴影
                    bmShadow.recycle();
                    bmShadow = BitmapFactory.decodeResource(getResources(), R.drawable.img_shadow_rect_red);
                    bmShadow = BitmapUtils.replacePixelColor(bmShadow, sex_red);
                } else {
                    status = false;
                    animOffSet.start();
                    //改变bkgBar颜色
                    bkgBarPaint.setColor(sex_red);
                    //改变背景阴影
                    bmShadow.recycle();
                    bmShadow = BitmapFactory.decodeResource(getResources(), R.drawable.img_shadow_rect_blue);
                    bmShadow = BitmapUtils.replacePixelColor(bmShadow, sex_blue);
                }
                if (checkListener != null) {
                    checkListener.onCheckedChange(status);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景条
        RectF bkgRect = new RectF((width - bkgBarW) / 2f, height / 2 - (bkgBarH / 2), (width - bkgBarW) / 2f + bkgBarW, height / 2 + (bkgBarH / 2));
        canvas.drawRoundRect(bkgRect, bkgBarH / 4, bkgBarH / 4, bkgBarPaint);

        //画阴影
        RectF shadowRect = new RectF(
                indicatorX - (height - indicatorH) / 2, 0, shadowW + indicatorX - (height - indicatorH) / 2, shadowH
        );
        canvas.drawBitmap(bmShadow, null, shadowRect, shaowPaint);

        //画指示器
        RectF indicatorRect = new RectF(
                indicatorX,
                indicatorY,
                indicatorW + indicatorX,
                (height - indicatorH) / 2 + indicatorH
        );
        canvas.drawRoundRect(indicatorRect, indicatorH / 6, indicatorH / 6, indicatorPaint);

        //画图标
        int baseLineY = (int) (indicatorRect.centerY() - textTop / 2 - textBottom / 2);//基线中间点的y轴计算公式
        if (status == false) {
            canvas.drawText("♂", indicatorRect.centerX(), baseLineY, textPaint);
        } else {
            canvas.drawText("♀", indicatorRect.centerX(), baseLineY, textPaint);
        }

        postInvalidate();
    }

    //状态监听
    OnCheckedChangeListener checkListener;

    public boolean isChecked() {
        return status;
    }

    public interface OnCheckedChangeListener {
        public boolean onCheckedChange(boolean flag);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        this.checkListener = listener;
    }
}
