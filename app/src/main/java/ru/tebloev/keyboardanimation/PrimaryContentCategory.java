package ru.tebloev.keyboardanimation;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Перечисление категорий контента, которые доступны на экране чата для отправки
 *
 * @author Tebloev Vladimir
 */
public enum PrimaryContentCategory implements ContentCategory {

    //TODO вставить адекватные иконки

    MONEY(R.string.content_money, R.drawable.ic_money_category_32dp),
    POSTCARD(R.string.content_postcard, R.drawable.ic_postcard_category_32dp),
    GIFT(R.string.content_gift, R.drawable.ic_giftcard_category_32dp),
    BOOK(R.string.content_book, R.drawable.ic_book_category_32dp);

    //TODO добавить все категории

    private int mName;
    private int mImage;

    PrimaryContentCategory(@StringRes int stringRes, @DrawableRes int imageRes) {
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
