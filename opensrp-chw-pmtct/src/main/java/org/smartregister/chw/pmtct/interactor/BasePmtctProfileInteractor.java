package org.smartregister.chw.pmtct.interactor;

import androidx.annotation.VisibleForTesting;

import org.smartregister.chw.pmtct.contract.PmtctProfileContract;
import org.smartregister.chw.pmtct.domain.MemberObject;
import org.smartregister.chw.pmtct.util.AppExecutors;
import org.smartregister.chw.pmtct.util.PmtctUtil;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public class BasePmtctProfileInteractor implements PmtctProfileContract.Interactor {
    protected AppExecutors appExecutors;

    @VisibleForTesting
    BasePmtctProfileInteractor(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
    }

    public BasePmtctProfileInteractor() {
        this(new AppExecutors());
    }

    @Override
    public void refreshProfileInfo(MemberObject memberObject, PmtctProfileContract.InteractorCallBack callback) {
        Runnable runnable = () -> appExecutors.mainThread().execute(() -> {
            callback.refreshFamilyStatus(AlertStatus.normal);
            callback.refreshMedicalHistory(true);
            callback.refreshUpComingServicesStatus("Pmtct Registration", AlertStatus.normal, new Date());
        });
        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRegistration(final String jsonString, final PmtctProfileContract.InteractorCallBack callback) {

        Runnable runnable = () -> {
            try {
                PmtctUtil.saveFormEvent(jsonString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        };
        appExecutors.diskIO().execute(runnable);
    }
}
