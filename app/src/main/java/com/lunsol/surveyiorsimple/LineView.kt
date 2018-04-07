package com.lunsol.surveyiorsimple

import android.content.Context
import android.graphics.PointF
import android.view.MotionEvent
import org.linccy.graffiti.LineView as GraffitiLineView

/**
 * Created by abcd7 on 2018/04/07.
 */
class LineView(context: Context?) : GraffitiLineView(context) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mCurrentPoint = PointF((event.x - mOffset.x) / mScale, (event.y - mOffset.y) / mScale)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsDoubleTouch = false
                mCurrentPath = MarkPath.newMarkPath(mCurrentPoint)
                mCurrentPath.currentMarkType = mCurrentType
                mCurrentPath.setWidth(mCurrentLineWidth)
                invalidate()
            }

            MotionEvent.ACTION_POINTER_DOWN -> mIsDoubleTouch = true

            MotionEvent.ACTION_MOVE -> {
                if (mCurrentPath == null || mIsDoubleTouch) return true
                mCurrentPath.addMarkPointToPath(mCurrentPoint)
                postInvalidateDelayed(40)
            }

            MotionEvent.ACTION_UP -> {
                if (mCurrentPath != null && !mIsDoubleTouch) {
//                    mCurrentPath.addMarkPointToPath(mCurrentPoint)
                    mCurrentPath.mPath.moveTo(mCurrentPoint.x, mCurrentPoint.y)
                    mCurrentPath.mPath.lineTo(mCurrentPoint.x, mCurrentPoint.y)
                    //如果是点击了撤销后，撤销的笔画移出栈，并将新的笔画压入栈
                    if (mPathCount < mFinishedPaths.size) {
                        val oldSize = mFinishedPaths.size
                        for (i in oldSize downTo mPathCount + 1) {
                            mFinishedPaths.removeAt(i - 1)
                        }
                    }
                    mFinishedPaths.add(mCurrentPath)

                    mPathCount++
                }

                mIsDoubleTouch = false
                mCurrentPath = null
                invalidate()
            }
        }//			invalidate();
        return true
    }
}