package com.lunsol.surveyiorsimple

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.MotionEvent
import org.linccy.graffiti.GraffitiView as OriginalGraffitiView
/**
 * Created by abcd7 on 2018/04/07.
 */
class GraffitiView : OriginalGraffitiView, OriginalGraffitiView.OnLoadFinishListener {
//    protected var mLineView: LineView? = null

    var onGraffitiViewOnClickListener: OriginalGraffitiView.OnGraffitiViewOnClickListener? = null

    override fun OnLoadFinish() {
        TODO("がんばれ")
    }

    constructor(context: Context?) : super(context) {
        super.setOnLoadFinishListener(this)
        super.setOnGraffitiViewOnClick(onGraffitiViewOnClickListener)
    }

    fun setImageBitmap(bitmap: Bitmap) {
        setCutoutImage(bitmap)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("ACTION_DOWN","ログ")
                mIsClick = true
                mOnACTION_DOWN_TIME = System.currentTimeMillis()
                return mLineView.onTouchEvent(event)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                Log.d("ACTION_POINTER_DOWN","ログ")
                mIsDrag = true
                mIsClick = false
                mOldDistance = spacingOfTwoFinger(event)
                mOldPointer = middleOfTwoFinger(event)
            }

            MotionEvent.ACTION_MOVE -> {
                Log.d("ACTION_MOVE","ログ")
                mIsClick = false
                val on_move_time = System.currentTimeMillis()
                if (on_move_time - mOnACTION_DOWN_TIME <= TO_CANVAS_TIME) {
                    mIsClick = true
                    return true
                }
//                if (!mIsDrag) return mLineView.onTouchEvent(event)
                if (event.pointerCount != 2) return true
                val newDistance = spacingOfTwoFinger(event)
                var scaleFactor = newDistance / mOldDistance
                scaleFactor = checkingScale(mShowView.scaleX, scaleFactor)
                //                if (startScale) {
                mShowView.scaleX = mShowView.scaleX * scaleFactor
                mShowView.scaleY = mShowView.scaleY * scaleFactor
                //                }
                mOldDistance = newDistance

                val newPointer = middleOfTwoFinger(event)
                mShowView.x = mShowView.x + newPointer.x - mOldPointer.x
                mShowView.y = mShowView.y + newPointer.y - mOldPointer.y
                mOldPointer = newPointer
                checkingGraffiti()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                Log.d("ACTION_POINTER_UP","ログ")
            }

            MotionEvent.ACTION_UP -> {
                Log.d("ACTION_UP","ログ")
                if (mIsClick) {
                    if (mOnGraffitiViewOnClick != null) {
                        mOnGraffitiViewOnClick.onGraffitiClick()
                        return true
                    }
                }
                if (!mIsDrag) return mLineView.onTouchEvent(event)
                mShowView.matrix.getValues(mMatrixValues)
                mLineView.setScaleAndOffset(mShowView.scaleX, mMatrixValues[2], mMatrixValues[5])
                mIsDrag = false
            }
        }
        return true

    }


}