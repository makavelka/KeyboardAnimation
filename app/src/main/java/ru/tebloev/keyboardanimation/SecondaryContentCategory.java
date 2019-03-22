package ru.tebloev.keyboardanimation;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Перечисление категорий контента, которые доступны на экране чата для отправки
 *
 * @author Tebloev Vladimir
 */
public enum SecondaryContentCategory implements ContentCategory {

    //TODO вставить адекватные иконки

    STICKER(R.string.content_sticker, R.drawable.ic_sticker_category_32dp),
    CAMERA(R.string.content_camera, R.drawable.ic_camera_category_32dp),
    GALLERY(R.string.content_gallery, R.drawable.ic_gallery_category_32dp),
    FILE(R.string.content_file, R.drawable.ic_file_category_32dp);

    //TODO добавить все категории

    private int mName;
    private int mImage;

    SecondaryContentCategory(@StringRes int stringRes, @DrawableRes int imageRes) {
        mName = stringRes;
        mImage = imageRes;
    }

    @Override
    public int getName() {
        return mName;
    }

    @Override
    public int getImage() {
        return mImage;
    }
}
