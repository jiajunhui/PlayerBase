package com.kk.taurus.playerbase.assist;

import android.os.Bundle;

/**
 * This interface is used to handle the basic playback
 * operation event issued by the caller. Such as pause,
 * fast forward and other operations.
 *
 * @param <T> Play master controller, maybe AVPlayer or AssistPlay or other packaging classes.
 *
 */
public interface OnEventAssistHandler<T> {

    void onAssistHandle(T assist, int eventCode, Bundle bundle);

    void requestPause(T assist, Bundle bundle);
    void requestResume(T assist, Bundle bundle);
    void requestSeek(T assist, Bundle bundle);
    void requestStop(T assist, Bundle bundle);
    void requestReset(T assist, Bundle bundle);
    void requestRetry(T assist, Bundle bundle);
    void requestReplay(T assist, Bundle bundle);

}
