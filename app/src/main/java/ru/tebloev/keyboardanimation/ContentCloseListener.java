package ru.tebloev.keyboardanimation;

/**
 * Слушатель закрытия контейнера с контентом
 *
 * @author Tebloev Vladimir
 */
public interface ContentCloseListener {

    /**
     * Событие того, что пользователь хочет закрыть фрагмент с контентом
     */
    void onClose();

    /**
     * Событие того, что операция произошла успешно.
     */
    void onSuccessOperation();
}
