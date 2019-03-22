package ru.tebloev.keyboardanimation;

/**
 * @author Tebloev Vladimir
 */
public class KeyboardVisibilityEvent {
    private boolean mIsVisible;
    private int mContentHeight;
    private int mContentHeightBeforeResize;

    public KeyboardVisibilityEvent(boolean isVisible, int contentHeight, int contentHeightBeforeResize) {
        mIsVisible = isVisible;
        mContentHeight = contentHeight;
        mContentHeightBeforeResize = contentHeightBeforeResize;
    }

    public boolean isVisible() {
        return mIsVisible;
    }

    public int getContentHeight() {
        return mContentHeight;
    }

    public int getContentHeightBeforeResize() {
        return mContentHeightBeforeResize;
    }
}
