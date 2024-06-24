package org.smartregister.chw.pmtct.contract;

import org.jetbrains.annotations.Nullable;
import org.smartregister.chw.pmtct.domain.MemberObject;
import org.smartregister.domain.AlertStatus;

import java.util.Date;

public interface PmtctProfileContract {
    interface View extends InteractorCallBack {

        void setProfileViewWithData();

        void setOverDueColor();

        void openMedicalHistory();

        void openUpcomingService();

        void openFamilyDueServices();

        void openHvlResultsHistory();

        void openBaselineInvestigationResults();

        void showProgressBar(boolean status);

        void recordAnc(MemberObject memberObject);

        void recordPnc(MemberObject memberObject);

        void hideView();

        void showDone();

        void hideDone();

        void showNextDue();

        void hideNextDue();

        void setDueDays(String dueDays);
    }

    interface Presenter {

        void fillProfileData(@Nullable MemberObject memberObject);

        void saveForm(String jsonString);

        @Nullable
        View getView();

        void refreshProfileBottom();

        void recordPmtctButton(String visitState);

        void visitRow(String visitState);

        void nextRow(String visitState, String visitDue);
    }

    interface Interactor {

        void refreshProfileInfo(MemberObject memberObject, InteractorCallBack callback);

        void saveRegistration(String jsonString, final PmtctProfileContract.InteractorCallBack callBack);
    }


    interface InteractorCallBack {

        void refreshMedicalHistory(boolean hasHistory);

        void refreshUpComingServicesStatus(String service, AlertStatus status, Date date);

        void refreshFamilyStatus(AlertStatus status);

    }
}