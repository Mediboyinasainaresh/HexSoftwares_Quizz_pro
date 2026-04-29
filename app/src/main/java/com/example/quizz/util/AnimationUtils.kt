package com.example.quizz.util

import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation

object AnimationUtils {

    fun press(view: View, onEnd: () -> Unit) {
        view.animate()
            .translationX(4f)
            .translationY(4f)
            .setDuration(50)
            .withEndAction {
                view.animate()
                    .translationX(0f)
                    .translationY(0f)
                    .setDuration(50)
                    .withEndAction {
                        onEnd()
                    }
                    .start()
            }
            .start()
    }

    fun shake(view: View) {
        val shake = TranslateAnimation(0f, 10f, 0f, 0f)
        shake.duration = 500
        shake.interpolator = CycleInterpolator(5f)
        view.startAnimation(shake)
    }

    fun bounce(view: View) {
        view.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
}