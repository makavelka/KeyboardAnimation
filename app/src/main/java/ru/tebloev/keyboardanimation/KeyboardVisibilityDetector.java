package ru.tebloev.keyboardanimation;

import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import kotlin.Unit;

/**
 * @author Tebloev Vladimir
 */
public class KeyboardVisibilityDetector {

    static void listen(ActivityViewHolder holder, KeyboardEventListener listener) {
        Detector detector = new Detector(holder, listener);
        holder.getNonResizableLayout().getViewTreeObserver().dispatchOnDraw();
        holder.getNonResizableLayout().getViewTreeObserver().addOnPreDrawListener(detector);
        holder.getNonResizableLayout().getViewTreeObserver().addOnGlobalLayoutListener(detector);
        holder.onDetach(() -> {
            holder.getNonResizableLayout().getViewTreeObserver().removeOnPreDrawListener(detector);
            holder.getNonResizableLayout().getViewTreeObserver().removeOnGlobalLayoutListener(detector);
            return Unit.INSTANCE;
        });
    }

    private static class Detector implements ViewTreeObserver.OnPreDrawListener, ViewTreeObserver.OnGlobalLayoutListener {

        private KeyboardEventListener mListener;
        private ActivityViewHolder mViewHolder;
        KeyboardVisibilityEvent mChanged;
        private int previousHeight = -1;

        public Detector(ActivityViewHolder viewHolder, KeyboardEventListener listener) {
            mViewHolder = viewHolder;
            mListener = listener;
        }

        public Detector(ViewGroup v, KeyboardEventListener listener) {

        }

        @Override
        public boolean onPreDraw() {
            boolean b = detect();
            return true;
        }

        private boolean detect() {
            int contentHeight = mViewHolder.getResizableLayout().getHeight();
            if (contentHeight == previousHeight) {
                return false;
            }
            if (previousHeight != -1) {
                int statusBarHeight = mViewHolder.getResizableLayout().getTop();
                boolean isKeyboardVisible = contentHeight < mViewHolder.getNonResizableLayout().getHeight() - statusBarHeight ;
                mChanged = new KeyboardVisibilityEvent(isKeyboardVisible, contentHeight - statusBarHeight, previousHeight);
                mListener.onChange(mChanged);
                Log.d("Detekkt", "prev: " + previousHeight);
                Log.d("Detekkt", "new: " + contentHeight);
                Log.d("Detekkt", "status " + statusBarHeight);

            }
            previousHeight = contentHeight;
            return true;
        }

        private boolean detect(ViewGroup viewGroup) {
            int contentHeight = viewGroup.getHeight();
            if (contentHeight == previousHeight) {
                return false;
            }
            if (previousHeight != -1) {
                boolean isKeyboardVisible = contentHeight < mViewHolder.getNonResizableLayout().getHeight();
                mChanged = new KeyboardVisibilityEvent(isKeyboardVisible, contentHeight, previousHeight);
                mListener.onChange(mChanged);
            }
            previousHeight = contentHeight;
            return true;
        }

        @Override
        public void onGlobalLayout() {

        }
    }

    public interface KeyboardEventListener {
        void onChange(KeyboardVisibilityEvent visibilityChanged);
    }
}
