package ru.tebloev.keyboardanimation;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

/**
 * @author Tebloev Vladimir
 */
public class ContentResizer {

    private ValueAnimator mGlobalLayoutHeightAnimator = new ObjectAnimator();

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
        mGlobalLayoutHeightAnimator.cancel();
        mGlobalLayoutHeightAnimator = ObjectAnimator.ofInt(event.getContentHeightBeforeResize(), event.getContentHeight());
        mGlobalLayoutHeightAnimator.setInterpolator(new LinearInterpolator());
        mGlobalLayoutHeightAnimator.setDuration(100);
        mGlobalLayoutHeightAnimator.addUpdateListener(animation -> setHeight((int) animation.getAnimatedValue(), v));
        mGlobalLayoutHeightAnimator.start();
    }

    private void setHeight(int height, ViewGroup viewGroup) {
        ViewGroup.LayoutParams params = viewGroup.getLayoutParams();
        params.height = height;
        viewGroup.setLayoutParams(params);
    }
}
