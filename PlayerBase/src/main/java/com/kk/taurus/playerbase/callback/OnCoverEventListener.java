package com.kk.taurus.playerbase.callback;

import android.os.Bundle;

/**
 * Created by Taurus on 2017/3/27.
 */

public interface OnCoverEventListener {

    int EVENT_CODE_ON_PLAYER_CONTROLLER_SHOW = 50001;
    int EVENT_CODE_ON_PLAYER_CONTROLLER_HIDDEN = 50002;

    int EVENT_CODE_ON_PLAYER_ERROR_SHOW = 50003;
    int EVENT_CODE_ON_PLAYER_ERROR_HIDDEN = 50004;

    void onCoverEvent(int eventCode, Bundle bundle);
}
