package com.krak.tourismask.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
/*
* Просто анимации для красивого исчезновения/появления
*/
public class CustomAnimations {
    public static void showAccordion(View view){
        view.setTranslationY(0);
        view.setAlpha(1);
        view.setVisibility(View.VISIBLE);
        view.bringToFront();
    }

    public static void hideAccordion(View view){
        view.animate()
                .translationY(view.getHeight())
                .alpha(0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private static boolean isVisible(View view){
        return view.getVisibility() == View.VISIBLE;
    }

    // Меняем видимость view. indicator - треугольник, как правило.
    // В разных состояниях видимости layout он повернут на разный градус
    public static void toggleVisibility(View view, View indicator){
        if (isVisible(view)){
            hideAccordion(view);
            indicator.setRotation(30);
        } else {
            showAccordion(view);
            indicator.setRotation(0);
        }
    }

    // То же что и выше, только без indicator
    public static void toggleVisibility(View view){
        if (isVisible(view)){
            hideAccordion(view);
        } else {
            showAccordion(view);
        }
    }
}
