package com.bhullnatik.gonioview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GonioView extends View {

    private Paint mHandlePaint;
    private Paint mTextPaint;

    private Point mHandles[];

    private float mHandleSize = 42f;

    private OnAngleSelectedListener mListener = null;

    public GonioView(Context context) {
        super(context);

        initialize();
    }

    public GonioView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize();
    }

    private void initialize() {
        int color = Color.BLUE;
        mHandlePaint = new Paint();
        mTextPaint = new Paint();

        mHandlePaint.setColor(color);
        mHandlePaint.setAntiAlias(true);
        mHandlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mHandlePaint.setStrokeWidth(mHandleSize / 4);

        mTextPaint.setTextSize(64f);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);

        mHandles = new Point[3];
        mHandles[0] = new Point(700, 250);
        mHandles[1] = new Point(450, 250);
        mHandles[2] = new Point(500, 475);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLines(canvas);
        drawHandles(canvas);
        drawAngle(canvas);
    }

    private void drawHandles(Canvas canvas) {
        for (Point handle : mHandles) {
            canvas.drawCircle(handle.x, handle.y, mHandleSize, mHandlePaint);
        }
    }

    private void drawLines(Canvas canvas) {
        Point middleHandle = mHandles[1];

        canvas.drawLine(mHandles[0].x, mHandles[0].y, middleHandle.x, middleHandle.y, mHandlePaint);
        canvas.drawLine(middleHandle.x, middleHandle.y, mHandles[2].x, mHandles[2].y, mHandlePaint);
    }

    private void drawAngle(Canvas canvas) {
        String textAngle = getCurrentAngle() + "˚";
        int x = (mHandles[0].x + mHandles[1].x + mHandles[2].x) / 3;
        int y = (mHandles[0].y + mHandles[1].y + mHandles[2].y) / 3;

        canvas.drawRect(x - 20, y - 64, x + mTextPaint.measureText(textAngle) + 20, y + 16, mHandlePaint);
        canvas.drawText(textAngle, x, y, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return onDown(event);
            case MotionEvent.ACTION_MOVE:
                return onMove(event);
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentHandle = null;
                if (mListener != null) {
                    mListener.onAngleSelected();
                }
                return true;
        }
        return false;
    }

    private Point mCurrentHandle = null;

    private boolean onDown(MotionEvent event) {
        invalidate();
        Point handle = checkIfHandleTouch(event.getX(), event.getY());

        if (handle == null) {
            return false;
        }
        mCurrentHandle = handle;
        return true;
    }

    private boolean onMove(MotionEvent event) {
        invalidate();
        if (mCurrentHandle != null && checkInBounds(event.getX(), event.getY())) {
            mCurrentHandle.set((int) event.getX(), (int) event.getY());
            return true;
        }
        return false;
    }

    private Point checkIfHandleTouch(float x, float y) {
        for (Point handle : mHandles) {
            if (Math.sqrt(Math.pow(x - handle.x, 2) + Math.pow(y - handle.y, 2)) < mHandleSize) {
                return handle;
            }
        }
        return null;
    }

    private boolean checkInBounds(float x, float y) {
        return ((mHandleSize / 2 < x && x < getRight() - mHandleSize / 2) &&
                (mHandleSize / 2 < y && y < getBottom() - mHandleSize / 2));
    }

    public int getCurrentAngle() {
        Point AB = new Point(mHandles[1].x - mHandles[0].x, mHandles[1].y - mHandles[0].y);
        Point CB = new Point(mHandles[1].x - mHandles[2].x, mHandles[1].y - mHandles[2].y);

        double dot = (AB.x * CB.x + AB.y * CB.y);
        double cross = (AB.x * CB.y - AB.y * CB.x);

        double alpha = Math.atan2(cross, dot);

        return (int) Math.abs(Math.floor(alpha * 180.0 / Math.PI + 0.5));
    }

    public void setOnAngleSelectedListener(OnAngleSelectedListener listener) {
        mListener = listener;
    }

    public void setColor(int color) {
        mHandlePaint.setColor(color);
        invalidate();
    }

    public void setHandleSize(int handleSize) {
        mHandleSize = handleSize;
        mHandlePaint.setStrokeWidth(mHandleSize / 4);
    }

    public interface OnAngleSelectedListener {
        void onAngleSelected();
    }
}