package ru.tebloev.keyboardanimation;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import java.io.Serializable;

/**
 * Базовый интерфейс для категорий контента в чате
 *
 * @author Tebloev Vladimir
 */
public interface ContentCategory extends Serializable {

    /**
     * Получение имени категории
     *
     * @return имя категории
     */
    @StringRes
    int getName();

    /**
     * Получение изображения категории
     *
     * @return изображение категории
     */
    @DrawableRes
    int getImage();
}
