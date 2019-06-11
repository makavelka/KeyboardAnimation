package ru.tebloev.keyboardanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Фрагмент, служащий контейнером для всей нижней части в чате
 *
 * @author Tebloev Vladimir
 */
public class ChatBottomContainerFragment extends Fragment implements ChatBottomView, ContentCloseListener,
        ContentOpenListener, TextFieldButtonListener {

    public static final String IS_MESSENGER_CLIENT = "IS_MESSENGER_CLIENT";

    private View mPrimaryCategoriesContainer;
    private View mSecondaryCategoriesContainer;
    private ViewGroup mTextContainer;
    private ViewGroup mContentContainer;
    private View mCategoriesWithTextContainer;
    private ViewGroup mCategoriesLayout;
    private ViewGroup mNotClientContainer;
    private Fragment mContentFragment;

    private AnimatorSet mAnimatorSet;

    private boolean mIsFirstOpen = true;
    private int mOriginalHeigth = 0;

    private boolean mIsMessengerClient;
    private ContentCategoriesFragment mFragment1;

    private ContentCategoriesFragment mFragment2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat_bottom_container, container, false);
        initBundle();
        mAnimatorSet = new AnimatorSet();
        initViews(v);
        initClientFragments();
        return v;
    }

    @Override
    public void showCategoriesContainer() {
        mCategoriesWithTextContainer.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.GONE);
        KeyboardUtils.hideKeyboard(getActivity());
        mCategoriesLayout.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openContent(@NonNull ContentCategory contentCategory) {
    }

    @Override
    public void hideCategoriesContainer() {
        mCategoriesLayout.setVisibility(View.GONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showContentFragment(@NonNull ContentCategory contentCategory, long conversationId, int conversationType) {
        mCategoriesWithTextContainer.setVisibility(View.GONE);
        mContentContainer.setVisibility(View.VISIBLE);
        mNotClientContainer.setVisibility(View.GONE);
        mContentFragment = ContentFragment.newInstance(contentCategory, conversationId, conversationType);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.content_with_header_container, mContentFragment)
                .commit();
    }

    @Override
    public void onClose() {
        if (mContentFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .remove(mContentFragment)
                    .commit();
            mCategoriesWithTextContainer.setVisibility(View.VISIBLE);
        }
        if (!mIsMessengerClient) {
            mNotClientContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessOperation() {

    }

    @Override
    public void openCategories() {
        mCategoriesWithTextContainer.setVisibility(View.VISIBLE);
        if (mOriginalHeigth > 0) {
            initAnimatorSetOpen();
        } else {
            mCategoriesLayout.setVisibility(View.VISIBLE);
        }
        mContentContainer.setVisibility(View.GONE);
    }

    @Override
    public void closeCategories() {
//        mCategoriesLayout.setVisibility(View.GONE);
//        initAnimatorSetClose();
        initAnimatorSetClose();
    }

    @Override
    public void onOpenCategory(@NonNull ContentCategory category) {
    }


    private void initBundle() {
        if (getArguments() != null) {
            mIsMessengerClient = getArguments().getBoolean(IS_MESSENGER_CLIENT, false);
        }
    }

    private void initViews(@NonNull View v) {
        mPrimaryCategoriesContainer = v.findViewById(R.id.primary_categories_container);
        mSecondaryCategoriesContainer = v.findViewById(R.id.secondary_categories_container);
        mContentContainer = v.findViewById(R.id.content_with_header_container);
        mCategoriesWithTextContainer = v.findViewById(R.id.categories_with_text_container);
        mCategoriesLayout = v.findViewById(R.id.categories_layout);
        mTextContainer = v.findViewById(R.id.text_field_container);
        mNotClientContainer = v.findViewById(R.id.not_client_container);
        new ContentResizer().listen(getActivity());
    }

    private void initClientFragments() {
        mCategoriesWithTextContainer.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.VISIBLE);
        mNotClientContainer.setVisibility(View.GONE);
        mFragment1 = ContentCategoriesFragment.newInstance(true);
        mFragment2 = ContentCategoriesFragment.newInstance(false);
        getChildFragmentManager().beginTransaction()
                .add(R.id.primary_categories_container, mFragment1)
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.secondary_categories_container, mFragment2)
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.text_field_container, TextFieldFragment.newInstance())
                .commit();
    }

    private void initAnimatorSetClose() {
        ObjectAnimator zoomY = ObjectAnimator.ofFloat(mCategoriesLayout, View.SCALE_Y, 1, 0);
        ObjectAnimator zoomX = ObjectAnimator.ofFloat(mCategoriesLayout, View.SCALE_X, 1, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mCategoriesLayout, View.ALPHA, 1, 0);
        ViewGroup.LayoutParams params = mCategoriesLayout.getLayoutParams();
        if (mIsFirstOpen) {
            mOriginalHeigth = mCategoriesLayout.getMeasuredHeight();
        }
        ValueAnimator value = ValueAnimator.ofInt(mCategoriesLayout.getMeasuredHeight(), 0);
        value.addUpdateListener(animation -> {
            params.height = (int) animation.getAnimatedValue();
            mCategoriesLayout.setLayoutParams(params);
            mFragment1.notifyData();
            mFragment2.notifyData();
        });
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(200);
        mAnimatorSet.playTogether(zoomX, zoomY, alpha, value);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsFirstOpen = false;
                mCategoriesLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.start();
    }

    private void initAnimatorSetOpen() {
        ObjectAnimator zoomY = ObjectAnimator.ofFloat(mCategoriesLayout, View.SCALE_Y, 0, 1f);
        ObjectAnimator zoomX = ObjectAnimator.ofFloat(mCategoriesLayout, View.SCALE_X, 0, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mCategoriesLayout, View.ALPHA, 0, 1);
        ViewGroup.LayoutParams params = mCategoriesLayout.getLayoutParams();
        ValueAnimator value = ValueAnimator.ofInt(0, mOriginalHeigth);
        value.addUpdateListener(animation -> {
            params.height = (int) animation.getAnimatedValue();
            mCategoriesLayout.setLayoutParams(params);
            mFragment1.notifyData();
            mFragment2.notifyData();
        });
        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCategoriesLayout.setVisibility(View.VISIBLE);
                KeyboardUtils.hideKeyboard(getActivity());
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mCategoriesLayout.postInvalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.setDuration(200);
        mAnimatorSet.playTogether(zoomX, zoomY, alpha, value);
        mAnimatorSet.start();
    }
}

