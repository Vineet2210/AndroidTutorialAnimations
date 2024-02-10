package com.vineet.visithealthassesment.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.FrameLayout
import androidx.core.animation.doOnStart
import com.vineet.visithealthassesment.presentation.IAnimationCallbackListener

class AnimationUtils {
    private lateinit var iAnimationCallbackListener: IAnimationCallbackListener

    fun setListeners(listener: IAnimationCallbackListener) {
        iAnimationCallbackListener = listener
    }

    fun animateView(targetView: View) {
        targetView.apply {
            val parent = parent as FrameLayout
            val translationX = (parent.width - width) / 2f - x
            val translationY = (parent.height - height) / 2f - y
            scaleX = SCALE_DOWN
            scaleY = SCALE_DOWN
            this.translationX += parent.width - width * scaleX - SCREEN_OFFSET
            this.translationY += -height * scaleY * VERTICAL_THRESHOLD
            val scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, SCALE_DOWN, ORIGINAL_SCALE)
            val scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, SCALE_DOWN, ORIGINAL_SCALE)
            val animX = ObjectAnimator.ofFloat(
                this, View.TRANSLATION_X, this.translationX, translationX
            )
            val animY = ObjectAnimator.ofFloat(
                this, View.TRANSLATION_Y, this.translationY, translationY
            )
            AnimatorSet().apply {
                playTogether(scaleX, scaleY, animX, animY)
                duration = ANIMATION_DURATION
                start()
            }
        }
        iAnimationCallbackListener.onAnimationEnd()
    }

    fun reverseAnimateView(targetView: View) {
        targetView.apply {
            scaleX = SCALE_UP
            scaleY = SCALE_UP
            post {
                val startX = (-width) * 0.5f
                val startY = (-height) * 0.5f
                translationX = startX - width * 1.5f
                translationY = startY - height * -2f
                val endX = 0f
                val endY = 0f
                val scaleX = ObjectAnimator.ofFloat(
                    this, View.SCALE_X, SCALE_UP, ORIGINAL_SCALE
                )
                val scaleY = ObjectAnimator.ofFloat(
                    this, View.SCALE_Y, SCALE_UP, ORIGINAL_SCALE
                )
                val animX = ObjectAnimator.ofFloat(
                    this, View.TRANSLATION_X, this.translationX, endX
                )
                val animY = ObjectAnimator.ofFloat(
                    this, View.TRANSLATION_Y, this.translationY, endY
                )
                AnimatorSet().apply {
                    doOnStart {
                        iAnimationCallbackListener.onAnimationStart()
                    }
                    playTogether(scaleX, scaleY, animX, animY)
                    duration = ANIMATION_DURATION
                    start()
                }
            }
        }
    }
    companion object{
        const val ANIMATION_DURATION:Long=200
        const val SCALE_DOWN:Float=0.2f
        const val SCALE_UP:Float=5f
        const val ORIGINAL_SCALE:Float=1f
        const val SCREEN_OFFSET:Int=300
        const val VERTICAL_THRESHOLD:Float=3.5f
    }
}