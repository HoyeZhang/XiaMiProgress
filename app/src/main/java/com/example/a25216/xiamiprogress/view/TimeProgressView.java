package com.example.a25216.xiamiprogress.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.a25216.xiamiprogress.R;

/**
 * Created by 25216 on 2018/2/28.
 */

public class TimeProgressView extends View {
    /**
     * 总时间
     */
    private int mTotalTime;
    /**
     * 目前时间
     */
    private int mCurrentTime;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 背景的颜色
     */
    private int mBgColor;
    /**
     * 线段渐变起始颜色
     */
    private int mLineStartColor;
    /**
     * 线段渐变终止颜色
     */
    private int mLineEndColor;
    /**
     * 文本的大小
     */
    private int mTextSize;
    private String text;

    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;
    /**
     * 绘制时控制文本绘制的范围
     */
    private boolean isDraw = true;

    private static final int DEFAULT_TOTAL_TIME = 0;
    private static final int DEFAULT_CURRENT_TIME = 0;
    private static final int DEFAULT_TEXT_SIZE = 15;//sp
    private static final int DEFAULT_COLOR = Color.WHITE;
    private static final int DEFAULT_BGCOLOR = Color.BLACK;
    private static final int DEFAULT_STARTCOLOR = Color.WHITE;
    private static final int DEFAULT_ENDCOLOR = Color.YELLOW;
    private static final int DEFAULT_PADDINGBOTTOM = 13; //dp
    private static final int DEFAULT_PADDINGTOP = 13; //dp
    private static final int DEFAULT_PADDINGLEFT = 18; //dp
    private static final int DEFAULT_PADDINGRIGHT = 18; //dp

    /*
    初始位置
     */
    private  int position = 0; //dp


    public TimeProgressView(Context context) {
        this(context, null);
    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TimeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeProgressView, defStyleAttr, 0);
        mTotalTime = a.getInt(R.styleable.TimeProgressView_totalTime, DEFAULT_TOTAL_TIME);
        mCurrentTime = a.getInt(R.styleable.TimeProgressView_currentTime, DEFAULT_CURRENT_TIME);
        mBgColor = a.getColor(R.styleable.TimeProgressView_bgColor, DEFAULT_BGCOLOR);
        mTextColor = a.getColor(R.styleable.TimeProgressView_textColor, DEFAULT_COLOR);
        mLineStartColor = a.getColor(R.styleable.TimeProgressView_lineStartColor, DEFAULT_STARTCOLOR);
        mLineEndColor =  a.getColor(R.styleable.TimeProgressView_lineEndColor, DEFAULT_ENDCOLOR);
        mTextSize = a.getDimensionPixelSize(R.styleable.TimeProgressView_textSize,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        DEFAULT_TEXT_SIZE,
                        getResources().getDisplayMetrics()));

        a.recycle();
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ;
        mPaint.setTextSize(mTextSize);

        threadDraw();

    }

    private void threadDraw() {
        // 绘图线程
        new Thread() {
            public void run() {
                while (isDraw) {

                    changePosition();  //点的位置计算
                    postInvalidate();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }.start();

    }

    /*
    控件位置改变计算
     */
    private void changePosition() {
        if (mBound != null) {
            if (mCurrentTime <= mTotalTime) {
                position = (getMeasuredWidth() - (mBound.width() + DEFAULT_PADDINGLEFT + DEFAULT_PADDINGRIGHT)) * (mCurrentTime) / mTotalTime;
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect rect = getTextRect();
        int textWidth = rect.width();
        int textHeight = rect.height();
        int width = measureWidth(widthMeasureSpec, textWidth);
        int height = measureHeight(heightMeasureSpec, textHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawText(canvas);
        drawLine(canvas);

    }


    private void drawText(Canvas canvas) {
        //画时间文字
        int viewHeight = getMeasuredHeight();
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int x = position + DEFAULT_PADDINGLEFT;
        int y = (int) (viewHeight / 2 +
                (fontMetrics.descent - fontMetrics.ascent) / 2
                - fontMetrics.descent);
        mPaint.setColor(mTextColor);
        canvas.drawText(intToTime(mCurrentTime) + "/" + intToTime(mTotalTime), x, y, mPaint);


        if (mCurrentTime < mTotalTime) {
            //当前时间递加*
            mCurrentTime++;
            if (mCurrentTime == 600) {

                //刷新大小及界面
                requestLayout();
            }
          if (isDraw ==false){
              setDraw(isDraw);
                isDraw=!isDraw;
                setDraw(isDraw);
          }
        } else {
            isDraw = false;
        }

    }

    private void drawBg(Canvas canvas) {
        //画时间背景
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        int viewHeight = getMeasuredHeight();
        if (position + mBound.width()> getMeasuredWidth()){
            //对控件位置进行控制
            position = getMeasuredWidth() -mBound.width()- DEFAULT_PADDINGRIGHT -DEFAULT_PADDINGLEFT;
        }
        RectF ovalRect = new RectF(position, 0, mBound.width() + DEFAULT_PADDINGLEFT + DEFAULT_PADDINGRIGHT + position, viewHeight);
        canvas.drawRoundRect(ovalRect, viewHeight / 2, viewHeight / 2, mPaint);
    }

    private void drawLine(Canvas canvas) {
        //画线段
        Paint linePaint = new Paint();
        linePaint.setStrokeWidth(getMeasuredHeight()/7);
        Point startPoint = new Point(0,getMeasuredHeight()/2);
        Point endPoint = new Point(position,getMeasuredHeight()/2);

        LinearGradient lg = new LinearGradient(
                startPoint.x, startPoint.y, endPoint.x, endPoint.y,
                mLineStartColor, mLineEndColor, Shader.TileMode.CLAMP);
       linePaint.setShader(lg);
        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y,linePaint);

    }
    /**
     *
     */
    private Rect getTextRect() {
        mBound = new Rect();
        intToTime(mTotalTime);
        text = intToTime(mCurrentTime) + "/" + intToTime(mTotalTime);
        mPaint.getTextBounds(text, 0, text.length(), mBound);
        return mBound;
    }

    private String intToTime(int time) {
        int minute = time / 60;
        int second = time % 60;
        if (second / 10 == 0) {
            return minute + ":0" + second;
        }

        return minute + ":" + second;
    }


    private int measureWidth(int widthMeasureSpec, int textWidth) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int width = 0;

        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else if (mode == MeasureSpec.AT_MOST) {

            width = textWidth + DEFAULT_PADDINGRIGHT + DEFAULT_PADDINGLEFT;
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec, int textHeight) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        //无论什么模式大小都只受字体大小影响
        if (mode == MeasureSpec.EXACTLY) {
            height = textHeight + DEFAULT_PADDINGTOP + DEFAULT_PADDINGBOTTOM;
        } else if (mode == MeasureSpec.AT_MOST) {
            height = textHeight + DEFAULT_PADDINGTOP + DEFAULT_PADDINGBOTTOM;
        }
        return height;
    }

    @Override
    public void setBackground(Drawable background) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x =(int) event.getX();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setPosition(x);
                break;
            case MotionEvent.ACTION_MOVE:
                setPosition(x);
                break;
            case MotionEvent.ACTION_UP:
                setPosition(x);
                break;
            default:
                break;
        }
        return true;
    }

    /*
    set
     */

    public void setPosition(int position) {
        this.position = position ;
        if (position + mBound.width()< getMeasuredWidth()){
            mCurrentTime = (int)((float)position / (getMeasuredWidth()- mBound.width()) * (float) mTotalTime);
        }

       postInvalidate();

    }

    public void setmCurrentTime(int mCurrentTime) {
        if (mCurrentTime >mTotalTime){
            return;
        }
        this.mCurrentTime = mCurrentTime;
        requestLayout();
    }

    public void setmTotalTime(int mTotalTime) {
        if (mCurrentTime >mTotalTime){
            return;
        }
        this.mTotalTime = mTotalTime;
        requestLayout();
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        requestLayout();
    }

    public void setDraw(boolean isDraw) {
        this.isDraw = isDraw;
        if (isDraw) {
            threadDraw();
        }
    }

    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
        postInvalidate();
    }

    public void setmLineStartColor(int mLineStartColor) {
        this.mLineStartColor = mLineStartColor;
        postInvalidate();
    }

    public void setmLineEndColor(int mLineEndColor) {
        this.mLineEndColor = mLineEndColor;
        postInvalidate();
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        postInvalidate();
    }
}
