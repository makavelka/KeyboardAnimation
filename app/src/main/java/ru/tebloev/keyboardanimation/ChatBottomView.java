package ru.tebloev.keyboardanimation;


import android.support.annotation.NonNull;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Интерфейс для работы контейнера нижней части в чате.
 * Необходим для изоляции логики из ChatActivity
 *
 * @author Tebloev Vladimir
 */
public interface ChatBottomView extends MvpView {

    /**
     * Открыть список категорий контента, который можно отправить
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showCategoriesContainer();

    /**
     * Открыть конкретный инструмент для отправки контента
     *
     * @param ContentCategory категория контента
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void openContent(@NonNull ContentCategory ContentCategory);

    /**
     * Закрыть список категорий
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideCategoriesContainer();

    /**
     * Показать контент во фрагменте
     *
     * @param contentCategory категория контента
     * @param conversationId id диалога
     * @param conversationType тип диалога
     */
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showContentFragment(@NonNull ContentCategory contentCategory, long conversationId, int conversationType);

}
