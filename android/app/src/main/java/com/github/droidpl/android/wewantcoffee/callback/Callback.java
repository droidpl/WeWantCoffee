package com.github.droidpl.android.wewantcoffee.callback;

import android.support.annotation.Nullable;

public interface Callback<T> {

    /**
     * The data received from the operation.
     *
     * @param data The response data.
     */
    void onSuccess(@Nullable T data);

    /**
     * The error launched from the operation.
     *
     * @param exception The error produced.
     */
    void onFailure(@Nullable Throwable exception);
}
