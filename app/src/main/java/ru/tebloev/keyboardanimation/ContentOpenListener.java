package ru.tebloev.keyboardanimation;

import android.support.annotation.NonNull;

/**
 * Слушатель закрытия контейнера с контентом
 *
 * @author Tebloev Vladimir
 */
public interface ContentOpenListener {

    /**
     * Событие того, что пользователь хочет открыть фрагмент с контентом
     *
     * @param category категория контента
     */
    void onOpenCategory(@NonNull ContentCategory category);
}
