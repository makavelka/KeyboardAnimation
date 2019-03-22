package ru.tebloev.keyboardanimation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Контейнер содержащий фрагмент с конкретным контентом, например стикеры, деньги.
 * Содержит заголовок с названием контента
 *
 * @author Tebloev Vladimir
 */
public class ContentFragment extends Fragment implements ContentCloseListener {

    public static final String CATEGORY_TAG = "CATEGORY_TAG";
    public static final String CONVERSATION_ID_TAG = "CONVERSATION_ID_TAG";
    public static final String CONVERSATION_TYPE_TAG = "CONVERSATION_TYPE_TAG";
    private static final long UNDEFINED_CONVERSATION_ID = -1;
    private static final int UNDEFINED_CONVERSATION_TYPE = -1;

    private TextView mTitleTextView;
    private ImageView mCloseImageView;

    /**
     * Создать новый фрагмент с конкретным контентом, например стикеры, деньги.
     *
     * @param category категория контента
     * @param conversationId id диалога
     * @param conversationType тип диалога
     * @return новый фрагмент с конкретным контентом, например стикеры, деньги.
     */
    public static ContentFragment newInstance(@NonNull ContentCategory category, long conversationId, int conversationType) {
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_TAG, category);
        args.putLong(CONVERSATION_ID_TAG, conversationId);
        args.putInt(CONVERSATION_TYPE_TAG, conversationType);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_container_fragment, container, false);
        mTitleTextView = v.findViewById(R.id.content_title_text_view);
        mCloseImageView = v.findViewById(R.id.close_image_view);
        initListeners();
        initContent();
        return v;
    }

    @Override
    public void onClose() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            getChildFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
     }

    @Override
    public void onSuccessOperation() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            getChildFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
        ((ContentCloseListener) getParentFragment()).onSuccessOperation();
    }

    private void initListeners() {
        mCloseImageView.setOnClickListener(v -> {
            onClose();
        });
    }

    private void initContent() {
        ContentCategory contentCategory = (ContentCategory) getArguments().getSerializable(CATEGORY_TAG);
        long conversationId = getArguments().getLong(CONVERSATION_ID_TAG, UNDEFINED_CONVERSATION_ID);
        int type = getArguments().getInt(CONVERSATION_TYPE_TAG, UNDEFINED_CONVERSATION_TYPE);
        if (contentCategory != null) {
            mTitleTextView.setText(contentCategory.getName());
        }

    }
}
