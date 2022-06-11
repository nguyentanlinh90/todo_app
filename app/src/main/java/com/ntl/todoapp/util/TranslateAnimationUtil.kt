package com.ntl.todoapp.util

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.ntl.todoapp.R


class TranslateAnimationUtil(context: Context?, view: View?) :
    OnTouchListener {
    private val gestureDetector: GestureDetector
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    inner class SimpleGestureDetector(private val mViewAnimation: View?) :
        SimpleOnGestureListener() {
        private var isFinished = true
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (distanceY > 0) {
                hiddenView()
            } else {
                showView()
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        private fun hiddenView() {
            if (mViewAnimation == null || mViewAnimation.visibility == View.GONE) {
                return
            }
            val animationDown = AnimationUtils.loadAnimation(
                mViewAnimation.context, R.anim.bottom_down
            )
            animationDown.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mViewAnimation.visibility = View.VISIBLE
                    isFinished = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    mViewAnimation.visibility = View.GONE
                    isFinished = true
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            if (isFinished) {
                mViewAnimation.startAnimation(animationDown)
            }
        }

        private fun showView() {
            if (mViewAnimation == null || mViewAnimation.visibility == View.VISIBLE) {
                return
            }
            val animationUp = AnimationUtils.loadAnimation(
                mViewAnimation.context, R.anim.bottom_up
            )
            animationUp.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    mViewAnimation.visibility = View.VISIBLE
                    isFinished = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    isFinished = true
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            if (isFinished) {
                mViewAnimation.startAnimation(animationUp)
            }
        }
    }

    init {
        gestureDetector = GestureDetector(context, SimpleGestureDetector(view))
    }
}
