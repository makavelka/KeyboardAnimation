package ru.tebloev.keyboardanimation;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Адаптер для показа кнопок с информацией о передаваемом контенте
 *
 * @author Tebloev Vladimir
 */
public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> {

    private List<ContentCategory> mCategory;
    private ChatBottomView mChatBottomView;

    public ContentAdapter(List<ContentCategory> category,
                          ChatBottomView view) {
        mCategory = category;
        mChatBottomView = view;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int layout) {
        View itemView = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.chat_content_item, viewGroup, false);
        return new ContentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder viewHolder, int position) {
        viewHolder.mNameTextView.setText(mCategory.get(position).getName());
        viewHolder.mImageView.setImageResource(mCategory.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return mCategory.size();
    }

    /**
     * Устанавливает список всех категорий для конкретного чата
     *
     * @param categories список категорий
     */
    public void setAllCategories(@NonNull List<ContentCategory> categories) {
        mCategory.clear();
        mCategory.addAll(categories);
        notifyDataSetChanged();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mNameTextView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.content_image_view);
            mNameTextView = itemView.findViewById(R.id.content_name_text_view);
        }
    }
}
