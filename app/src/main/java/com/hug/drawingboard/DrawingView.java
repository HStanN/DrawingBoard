package com.hug.drawingboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HStan on 2017/6/12.
 */

public class DrawingView extends View {
    private final float DEFAULT_STROKE = 20;

    private int mDrawColor;
    private int mDrawStyle;
    private int mPenStyle;
    private float mDrawStroke;
    private float mEraserSize;

    private Paint mPaint;
    private Path mPath;

    private float mLastX;
    private float mLastY;

    private Xfermode mClearMode;

    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    private List<CachePath> mDrawPathCaches;
    private List<CachePath> mRemovedPathCaches;

    private int SAVE_CACHE_SIZE = 10;

    public static final int MODE_PEN = 0;
    public static final int MODE_TEXT = 1;
    public static final int MODE_ERASER = 2;
    public static final int PENMODE_CIRCLE = 0;
    public static final int PENMODE_RECTANGLE = 1;

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.DrawingView, 0, 0);
        mDrawColor = typedArray.getColor(R.styleable.DrawingView_color, Color.BLACK);
        mDrawStyle = typedArray.getInt(R.styleable.DrawingView_style, MODE_PEN);
        mPenStyle = typedArray.getInt(R.styleable.DrawingView_penstyle, PENMODE_CIRCLE);
        mDrawStroke = typedArray.getFloat(R.styleable.DrawingView_stroke, DEFAULT_STROKE);
        mEraserSize = 50;
        typedArray.recycle();
    }

    private void initBuffer() {
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDrawStroke);
        mPaint.setColor(mDrawColor);
        switch (mPenStyle) {
            case PENMODE_CIRCLE:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case PENMODE_RECTANGLE:
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            default:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
        }
        mClearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                if (mDrawStyle == MODE_ERASER) {
                    break;
                }
                mBufferCanvas.drawPath(mPath, mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mDrawPathCaches == null) {
                    mDrawPathCaches = new ArrayList<>(SAVE_CACHE_SIZE);
                } else if (mDrawPathCaches.size() == SAVE_CACHE_SIZE) {
                    mDrawPathCaches.remove(0);
                }
                CachePath cachePath = new CachePath();
                cachePath.setPath(new Path(mPath));
                cachePath.setPaint(new Paint(mPaint));
                cachePath.setCanvas(mBufferCanvas);
                mDrawPathCaches.add(cachePath);
                mPath.reset();
                break;
        }
        return true;
    }

    public void setMode(int mode) {
        switch (mode) {
            case MODE_PEN:
                mPaint.setXfermode(null);
                mPaint.setStrokeWidth(mDrawStroke);
                break;
            case MODE_TEXT:
                break;
            case MODE_ERASER:
                mPaint.setXfermode(mClearMode);
                mPaint.setStrokeWidth(mEraserSize);
                break;
        }
    }

    /**
     * 设置画笔粗细
     *
     * @param stroke
     */
    public void setStroke(float stroke) {
        mDrawStroke = stroke;
        mPaint.setStrokeWidth(stroke);
    }

    /**
     * 设置风格，风格分描线和文字两种（暂定）
     *
     * @param style
     */
    public void setDrawStyle(int style) {
        mDrawStyle = style;
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    public void setDrawColorById(@ColorRes int color) {
        mDrawColor = color;
        mPaint.setColor(getResources().getColor(color));
    }

    public void setDrawColor(@ColorInt int color) {
        mPaint.setColor(color);
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    public void setDrawColor(String color) {
        mPaint.setColor(Color.parseColor(color));
    }

    /**
     * 设置画笔风格，风格分为圆形和矩形
     *
     * @param penStyle
     */
    public void setPenStyle(int penStyle) {
        mPenStyle = penStyle;
        switch (mPenStyle) {
            case PENMODE_CIRCLE:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
            case PENMODE_RECTANGLE:
                mPaint.setStrokeCap(Paint.Cap.SQUARE);
                break;
            default:
                mPaint.setStrokeCap(Paint.Cap.ROUND);
                break;
        }
    }

    public void undo() {
        int size = mDrawPathCaches == null ? 0 : mDrawPathCaches.size();
        if (size  > 0){
            CachePath cachePath = mDrawPathCaches.remove(size - 1);
            if (mRemovedPathCaches == null){
                mRemovedPathCaches = new ArrayList<>(SAVE_CACHE_SIZE);
            }
            mRemovedPathCaches.add(cachePath);
            reDraw();

        }
    }

    public void redo() {
        int size = mRemovedPathCaches == null ? 0 : mRemovedPathCaches.size();
        if (size > 0){
            CachePath cachePath = mRemovedPathCaches.remove(size - 1);
            if (mDrawPathCaches == null){
                mDrawPathCaches = new ArrayList<>(SAVE_CACHE_SIZE);
            }
            mDrawPathCaches.add(cachePath);
            reDraw();
        }
    }

    private void reDraw(){
        if (mDrawPathCaches != null){
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            for (CachePath cachePath : mDrawPathCaches){
                cachePath.draw();
            }
            invalidate();
        }
    }

    public void clear() {
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        mDrawPathCaches.clear();
        mRemovedPathCaches.clear();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}
