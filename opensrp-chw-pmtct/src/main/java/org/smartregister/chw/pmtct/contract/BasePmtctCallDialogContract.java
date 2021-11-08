package org.smartregister.chw.pmtct.contract;

import android.content.Context;

public interface BasePmtctCallDialogContract {

    interface View {
        void setPendingCallRequest(Dialer dialer);
        Context getCurrentContext();
    }

    interface Dialer {
        void callMe();
    }
}
