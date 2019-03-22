package ru.tebloev.keyboardanimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * @author Tebloev Vladimir
 */
public class ContentResizer {

    private ValueAnimator mHeightAnimator = new ObjectAnimator();

    public void listen(Activity activity) {
        ActivityViewHolder holder = ActivityViewHolder.Companion.createFrom(activity);
        KeyboardVisibilityDetector.listen(holder, visibilityChanged -> animateHeight(holder, visibilityChanged));
    }

    public void listen(Activity activity, ViewGroup v) {
        ActivityViewHolder holder = ActivityViewHolder.Companion.createFrom(activity);
        KeyboardVisibilityDetector.listen(holder, visibilityChanged -> runAnimator(v, visibilityChanged));
    }

    private void animateHeight(ActivityViewHolder viewHolder, KeyboardVisibilityEvent event) {
        ViewGroup contentView = viewHolder.getContentView();
        setHeight(event.getContentHeightBeforeResize(), contentView);
        runAnimator(contentView, event);
    }

    private void runAnimator(ViewGroup v, KeyboardVisibilityEvent event) {
        mHeightAnimator.cancel();
        mHeightAnimator = ObjectAnimator.ofInt(event.getContentHeightBeforeResize(), event.getContentHeight());
        mHeightAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mHeightAnimator.setDuration(1500);
        mHeightAnimator.addUpdateListener(animation -> setHeight((int) animation.getAnimatedValue(), v));
        mHeightAnimator.start();
    }

    private void setHeight(int height, ViewGroup viewGroup) {
        ViewGroup.LayoutParams params = viewGroup.getLayoutParams();
        params.height = height;
        viewGroup.setLayoutParams(params);
    }
}
