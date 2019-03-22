package ru.tebloev.keyboardanimation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * Фрагмент, служащий контейнером для всей нижней части в чате
 *
 * @author Tebloev Vladimir
 */
public class ChatBottomContainerFragment extends Fragment implements ChatBottomView, ContentCloseListener,
        ContentOpenListener, TextFieldButtonListener {

    public static final String CONVERSATION_ID_ARG = "conversation_id_arg";
    public static final String PHONE_ARG = "phone_arg";

    public static final String CONVERSATION_ID = "CONVERSATION_ID";
    public static final String CONVERSATION_TYPE = "CONVERSATION_TYPE";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String RECEIVER_ID = "RECEIVER_ID";
    public static final String IS_FROM_ADDRESS_BOOK = "IS_FROM_ADDRESS_BOOK";
    public static final String IS_MESSENGER_CLIENT = "IS_MESSENGER_CLIENT";
    public static final String IS_ADMIN = "IS_ADMIN";
    public static final String IS_SBER_CHAT_FILES_ENABLED = "IS_SBER_CHAT_FILES_ENABLED";
    public static final String IS_FROM_DEEPLINK = "IS_FROM_DEEPLINK";
    private static final String EMPTY_STRING = "";
    private static final long UNDEFINED_CONVERSATION_ID = -1;
    private static final int UNDEFINED_CONVERSATION_TYPE = -1;
    private static final long UNDEFINED_RECEIVER_ID = -1;

    private View mPrimaryCategoriesContainer;
    private View mSecondaryCategoriesContainer;
    private ViewGroup mTextContainer;
    private ViewGroup mContentContainer;
    private ViewGroup mCategoriesWithTextContainer;
    private ViewGroup mCategoriesLayout;
    private ViewGroup mNotClientContainer;
    private View mRoot;
    private Fragment mContentFragment;

    private AnimatorSet mAnimatorSet;

    private boolean mIsFirstOpen = true;
    private int mOriginalHeigth = 0;

    private boolean mIsMessengerClient;


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
        mCategoriesLayout.setVisibility(View.VISIBLE);
//        initAnimatorSetOpen();
    }

    @Override
    public void closeCategories() {
        mCategoriesLayout.setVisibility(View.GONE);
//        initAnimatorSetClose();
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
        mRoot = v.findViewById(R.id.root);
        new ContentResizer().listen(getActivity());
    }

    private void initClientFragments() {
        mCategoriesWithTextContainer.setVisibility(View.VISIBLE);
        mContentContainer.setVisibility(View.VISIBLE);
        mNotClientContainer.setVisibility(View.GONE);
        getChildFragmentManager().beginTransaction()
                .add(R.id.primary_categories_container, ContentCategoriesFragment.newInstance(true))
                .commit();
        getChildFragmentManager().beginTransaction()
                .add(R.id.secondary_categories_container, ContentCategoriesFragment.newInstance(false))
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
            mCategoriesLayout.postOnAnimation(() -> params.height = (int) animation.getAnimatedValue());
        });

        mAnimatorSet.setDuration(350);
        mAnimatorSet.playTogether(zoomX, zoomY, alpha, value);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

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
        });
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mCategoriesLayout.setVisibility(View.VISIBLE);
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
        mAnimatorSet.setDuration(350);
        mAnimatorSet.playTogether(zoomX, zoomY, alpha, value);
        mAnimatorSet.start();
    }
}

