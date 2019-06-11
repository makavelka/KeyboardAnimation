package ru.tebloev.keyboardanimation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;


/**
 * Фрагмент для отображения списка возможного контента для отправки в чат
 *
 * @author Tebloev Vladimir
 */
public class ContentCategoriesFragment extends Fragment {

    public static final String IS_PRIMARY_TAG = "IS_PRIMARY_TAG";
    private static final String TYPE_CONVERSATION = "TYPE_CONVERSATION";
    private static final String IS_ADMIN = "IS_ADMIN";
    private static final String IS_SBER_CHAT_FILES_ENABLED = "IS_SBER_CHAT_FILES_ENABLED";

    private RecyclerView mRecyclerView;
    private ChatBottomView mChatBottomView;
    private ContentAdapter mAdapter;

    /**
     * Возвращает новый экземпляр данного фрагмента
     *
     * @param isPrimary              какая строка в списке категорий (первичная, вторичная)
     * @return фрагмент
     */
    public static ContentCategoriesFragment newInstance(boolean isPrimary) {
        Bundle args = new Bundle();
        args.putBoolean(IS_PRIMARY_TAG, isPrimary);
        ContentCategoriesFragment fragment = new ContentCategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mChatBottomView = (ChatBottomView) getParentFragment();
    }

    public void notifyData() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.content_recycler_view);
        initRecyclerView();
        return v;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        boolean isPrimary = getArguments().getBoolean(IS_PRIMARY_TAG);
        mAdapter = new ContentAdapter(Arrays.asList(isPrimary ? PrimaryContentCategory.values() : SecondaryContentCategory.values()), mChatBottomView);
        mRecyclerView.setAdapter(mAdapter);
    }
}
