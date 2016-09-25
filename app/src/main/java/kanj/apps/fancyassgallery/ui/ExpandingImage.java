package kanj.apps.fancyassgallery.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.android.volley.toolbox.NetworkImageView;

import static android.view.MotionEvent.INVALID_POINTER_ID;

/**
 * Created by voldemort on 25/9/16.
 */

public class ExpandingImage extends NetworkImageView {
    private static final String TAG = "kanj-ExpandingImage";

    private float mBeginTouchX, mBeginTouchY, dx, dy;
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
                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Remember where we started (for dragging)
                mBeginTouchX = x;
                mBeginTouchY = y;
                // Save the ID of this pointer (for dragging)
                mActivePointerId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.v(TAG, "action move");
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        event.findPointerIndex(mActivePointerId);

                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                // Calculate the distance moved
                dx = x - mBeginTouchX;
                dy = y - mBeginTouchY;

                invalidate();

                /*// Remember this touch position for the next move event
                mBeginTouchX = x;
                mBeginTouchY = y;*/

                break;
            }
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "action up");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.v(TAG, "action cancel");
                mActivePointerId = INVALID_POINTER_ID;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG, "action pointer up");
                mActivePointerId = INVALID_POINTER_ID;
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        float mScaleFactor = (mBeginTouchY + dy)/mBeginTouchY;
        Log.v(TAG, "try to scale by " + mScaleFactor);
        mScaleFactor = Math.min(1.0f, mScaleFactor);
        canvas.scale(mScaleFactor, mScaleFactor);
        canvas.restore();
    }
}
