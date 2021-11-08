package org.smartregister.chw.pmtct.listener;


import android.view.View;

import org.smartregister.chw.pmtct.fragment.BasePmtctCallDialogFragment;
import org.smartregister.chw.pmtct.util.PmtctUtil;
import org.smartregister.pmtct.R;

import timber.log.Timber;

public class BasePmtctCallWidgetDialogListener implements View.OnClickListener {

    private BasePmtctCallDialogFragment callDialogFragment;

    public BasePmtctCallWidgetDialogListener(BasePmtctCallDialogFragment dialogFragment) {
        callDialogFragment = dialogFragment;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.malaria_call_close) {
            callDialogFragment.dismiss();
        } else if (i == R.id.malaria_call_head_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                PmtctUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        } else if (i == R.id.call_malaria_client_phone) {
            try {
                String phoneNumber = (String) v.getTag();
                PmtctUtil.launchDialer(callDialogFragment.getActivity(), callDialogFragment, phoneNumber);
                callDialogFragment.dismiss();
            } catch (Exception e) {
                Timber.e(e);
            }
        }
    }
}
