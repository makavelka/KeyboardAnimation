package ru.tebloev.keyboardanimation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Фрагмент, содержащий текстовое поле для ввода сообщения или суммы перевода
 *
 * @author Tebloev Vladimir
 */
public class TextFieldFragment extends Fragment {

    private static final String EMPTY_TEXT = "";
    private static final int DEFAULT_ANIMATION_TIME = 350;
    private static final int ROTATION_START_DEGREE = 0;
    private static final int ROTATION_END_DEGREE = 45;

    private EditText mMessageEditText;
    private ImageButton mSendMessageButton;

    private TextFieldButtonListener mListener;

    private boolean mIsCategoriesVisible = false;

    public static TextFieldFragment newInstance() {
        Bundle args = new Bundle();
        TextFieldFragment fragment = new TextFieldFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (TextFieldButtonListener) getParentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text_field_send_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initListeners();
    }

    private void initViews(View view) {
        mMessageEditText = view.findViewById(R.id.message_edit_text);
        mSendMessageButton = view.findViewById(R.id.send_message_button);
    }

    private void initListeners() {
        mSendMessageButton.setOnClickListener(new SendMessageListener());
        mMessageEditText.setOnTouchListener((v, event) -> {
            mMessageEditText.post(() -> closeCategories());
            return true;
        });
    }

    private void animateButton(int startDegree, int endDegree) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mSendMessageButton, View.ROTATION, startDegree, endDegree);
        animator.setDuration(DEFAULT_ANIMATION_TIME);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }

    private void closeCategories() {
        KeyboardUtils.showKeyboard(getActivity(), mMessageEditText);
        if (mIsCategoriesVisible) {
            mListener.closeCategories();
            animateButton(ROTATION_END_DEGREE, ROTATION_START_DEGREE);
            mIsCategoriesVisible = false;
        }
    }

    private void openCategories() {
        if (!mIsCategoriesVisible) {
            mListener.openCategories();
            animateButton(ROTATION_START_DEGREE, ROTATION_END_DEGREE);
            mIsCategoriesVisible = true;
        }
    }

    private class SendMessageListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (mIsCategoriesVisible) {
                closeCategories();
            } else {
                KeyboardUtils.hideKeyboard(mMessageEditText);
                openCategories();
            }
        }
    }
}
