package ru.tebloev.keyboardanimation;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Класс-утилита для работы с клавиатурой.
 * Содержит методы для показа и скрытия клавиатуры.
 *
 * @author Alexander Gontarenko
 */
public final class KeyboardUtils {

    private static final String TAG = KeyboardUtils.class.toString();

    private KeyboardUtils() {
    }

    /**
     * Показать клавиатуру
     *
     * @param context контекст
     * @param view    view с фокусом
     */
    public static void showKeyboard(final Context context, final View view) {
        view.post(new Runnable() {

            @Override
            public void run() {
                if (view.requestFocus()) {
                    final InputMethodManager imm = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });
    }

    /**
     * Показать клавиатуру
     *
     * @param context      контекст
     * @param view         view с фокусом
     * @param restartInput требуется ли перезапуск клавиатуры
     */
    public static void showKeyboard(final Context context, final View view, boolean restartInput) {
        if (restartInput) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.restartInput(view);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        } else {
            showKeyboard(context, view);
        }
    }

    /**
     * Спрятать клавиатуру
     *
     * @param context
     */
    public static void hideKeyboard(Context context) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(new View(context).getWindowToken(), 0);
    }

    /**
     * Спрятать клавиатуру
     *
     * @param activity активити
     */
    public static void hideKeyboard(Activity activity) {
        if (activity == null) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        IBinder binder = null;
        if (view != null) {
            binder = view.getWindowToken();
        } else if (activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            binder = activity.getWindow().getDecorView().getWindowToken();
        }
        if (binder == null) {
            return;
        }
        try {
            imm.hideSoftInputFromWindow(binder, 0);
        } catch (Exception ignored) {
        }
    }

    /**
     * Спрятать клавиатуру
     *
     * @param context контекст
     * @param v       view
     */
    public static void hideKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * Спрятать клавиатуру
     *
     * @param context     контекст
     * @param windowToken токен окна, которое выполняет запрос
     */
    public static void hideKeyboard(Context context, IBinder windowToken) {
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(windowToken, 0);
    }

    /**
     * Спрятать клавиатуру
     *
     * @param rootView Если rootView null - ничего не сработает, лучше использовать @link{hideKeyboard(Activity activity)}
     */
    public static void hideKeyboard(View rootView) {
        if (rootView == null || rootView.getContext() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) rootView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }
}
