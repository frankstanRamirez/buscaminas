package com.miuniversidad.buscaminas;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * Utilidades para animaciones de UI
 */
public class AnimationUtils {

    /**
     * Animación de escala: crece y se encoge
     */
    public static void pulseAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.2f, 1f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.setInterpolator(new OvershootInterpolator());
        scaleY.setInterpolator(new OvershootInterpolator());
        scaleX.start();
        scaleY.start();
    }

    /**
     * Animación de rotación para victoria
     */
    public static void celebrateAnimation(View view) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        rotation.setDuration(1000);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotation.start();
    }

    /**
     * Animación de shake (vibración) para derrota
     */
    public static void shakeAnimation(View view) {
        ObjectAnimator translateX = ObjectAnimator.ofFloat(view, "translationX", 0, 25, -25, 25, -25, 0);
        translateX.setDuration(500);
        translateX.start();
    }

    /**
     * Animación de fade in
     */
    public static void fadeIn(View view, long duration) {
        view.setAlpha(0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        alpha.setDuration(duration);
        alpha.start();
    }

    /**
     * Animación de fade out
     */
    public static void fadeOut(View view, long duration, Runnable onComplete) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        alpha.setDuration(duration);
        alpha.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (onComplete != null) {
                    onComplete.run();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        alpha.start();
    }

    /**
     * Animación de elevación (z-index) con sombra
     */
    public static void raiseAnimation(View view) {
        ObjectAnimator elevation = ObjectAnimator.ofFloat(view, "elevation", 2f, 8f, 2f);
        elevation.setDuration(600);
        elevation.setInterpolator(new OvershootInterpolator());
        elevation.start();
    }

    /**
     * Animación de escala rápida (como click)
     */
    public static void clickAnimation(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f, 1f);
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        scaleX.start();
        scaleY.start();
    }
}
