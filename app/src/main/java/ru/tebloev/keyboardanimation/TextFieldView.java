package ru.tebloev.keyboardanimation;

import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Интерфейс view поля ввода сообщений
 *
 * @author Krapivin Eric
 * @since 27.02.19.
 */
@StateStrategyType(AddToEndSingleStrategy.class)
public interface TextFieldView extends MvpView {

    /**
     * Устанавливает видимость кнопки отправки сообщения
     *
     * @param isVisible
     */
    @StateStrategyType(SkipStrategy.class)
    void setSendMessageButtonVisible(boolean isVisible);

    /**
     * Отправка сообщения
     *
     * @param text текст сообщения
     */
    @StateStrategyType(SkipStrategy.class)
    void sendMessage(@NonNull String text);

    /**
     * Показываем ранее сохраненный, набранный но не отправленный текст
     *
     * @param text текст который нужно отобразить
     */
    @StateStrategyType(SkipStrategy.class)
    void showTypedSavedText(@NonNull String text);

    /**
     * Отобразить кнопку отправки сообщения, скрывая кнопку открытия меню
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSendMessageEnabled();

    /**
     * Скрыть кнопку отправки сообщения, отображая кнопку открытия меню
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSendMessageDisabled();

}
