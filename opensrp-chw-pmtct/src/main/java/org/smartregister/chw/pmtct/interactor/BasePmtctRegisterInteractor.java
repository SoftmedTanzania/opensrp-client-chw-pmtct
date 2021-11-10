package org.smartregister.chw.pmtct.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.pmtct.contract.PmtctRegisterContract;
import org.smartregister.chw.pmtct.util.AppExecutors;
import org.smartregister.chw.pmtct.util.PmtctUtil;

public class BasePmtctRegisterInteractor implements PmtctRegisterContract.Interactor {

    private AppExecutors appExecutors;

    @VisibleForTesting
    BasePmtctRegisterInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BasePmtctRegisterInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void saveRegistration(final String jsonString, final PmtctRegisterContract.InteractorCallBack callBack) {

        Runnable runnable = () -> {
            try {
                PmtctUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            appExecutors.mainThread().execute(() -> callBack.onRegistrationSaved());
        };
        appExecutors.diskIO().execute(runnable);
    }
}
