package kanj.apps.fancyassgallery.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by voldemort on 25/9/16.
 */

public class ExpandingImage extends NetworkImageView {
    private static final String TAG = "kanj-ExpandingImage";

    private float mBeginTouchX, mBeginTouchY, dx, dy;
    private int initialHeight, initialWidth;
    private int mActivePointerId;

    public ExpandingImage(Context context) {
        super(context);
    }

    public ExpandingImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandingImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case MotionEvent.ACTION_DOWN: {
                Log.v(TAG, "action down");
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final float x = event.getRawX();
                final float y = event.getRawY();

                // Remember where we started (for dragging)
                mBeginTouchX = x;
                mBeginTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = event.getPointerId(0);

                initialHeight = getHeight();
                initialWidth = getWidth();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        event.findPointerIndex(mActivePointerId);

                final float x = event.getRawX();
                final float y = event.getRawY();
                Log.v(TAG, "action move " + y);

                // Calculate the distance moved
                dx = x - mBeginTouchX;
                dy = y - mBeginTouchY;

                float mScaleFactor = (mBeginTouchY - dy)/mBeginTouchY;
                Log.v(TAG, "try to scale by " + mScaleFactor);
                scaleImage(mScaleFactor);

                /*// Remember this touch position for the next move event
                mBeginTouchX = x;
                mBeginTouchY = y;*/

                break;
            }
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "action up");
                mActivePointerId = INVALID_POINTER_ID;
                revertScale();
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.v(TAG, "action cancel");
                mActivePointerId = INVALID_POINTER_ID;
                revertScale();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG, "action pointer up");
                mActivePointerId = INVALID_POINTER_ID;
                break;
        }

        return true;
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float mScaleFactor = (mBeginTouchY - dy)/mBeginTouchY;
        Log.v(TAG, "try to scale by " + mScaleFactor);
        mScaleFactor = Math.min(1.0f, mScaleFactor);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.restore();
    }*/

    private void scaleImage(float factor) {
        ViewGroup.LayoutParams params = getLayoutParams();
        int newHeight = (int) ((float)initialHeight * factor);
        int newWidth = (int) ((float)initialWidth * factor);
        Log.v(TAG, "initial " + initialHeight + " by " + initialWidth + " new " + newHeight + " by " + newWidth);
        params.height = newHeight;
        params.width = newWidth;
        requestLayout();
    }

    private void revertScale() {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = (int) convertDpToPixel(150, getContext());
        params.width = -2;
        requestLayout();
    }

    private float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }
}
