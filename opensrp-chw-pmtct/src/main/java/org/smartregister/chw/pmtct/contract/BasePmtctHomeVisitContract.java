package org.smartregister.chw.pmtct.contract;

import android.content.Context;

import com.vijay.jsonwizard.domain.Form;

import org.json.JSONObject;
import org.smartregister.chw.pmtct.domain.MemberObject;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BasePmtctHomeVisitContract {

    interface View extends VisitView {

        BasePmtctHomeVisitContract.Presenter presenter();

        Form getFormConfig();

        void startForm(org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction);

        void startFormActivity(JSONObject jsonForm);

        void startFragment(org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction);

        void redrawHeader(MemberObject memberObject);

        void redrawVisitUI();

        void displayProgressBar(boolean state);

        Map<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> getPmtctHomeVisitActions();

        void close();

        void submittedAndClose();

        Presenter getPresenter();

        /**
         * Save the received data into the events table
         * Start aggregation of all events and persist results into the events table
         */
        void submitVisit();

        void initializeActions(LinkedHashMap<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> map);

        Context getContext();

        void displayToast(String message);

        Boolean getEditMode();

        void onMemberDetailsReloaded(MemberObject memberObject);
    }

    interface VisitView {

        /**
         * Results action when a dialog is opened and returns a payload
         *
         * @param jsonString
         */
        void onDialogOptionUpdated(String jsonString);

        Context getMyContext();
    }

    interface Presenter {

        void startForm(String formName, String memberID, String currentLocationId);

        /**
         * Recall this method to redraw ui after every submission
         *
         * @return
         */
        boolean validateStatus();

        /**
         * Preload header and visit
         */
        void initialize();

        void submitVisit();

        void reloadMemberDetails(String memberID);
    }

    interface Model {

        JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception;

    }

    interface Interactor {

        void reloadMemberDetails(String memberID, InteractorCallBack callBack);

        MemberObject getMemberClient(String memberID);

        void saveRegistration(String jsonString, boolean isEditMode, final BasePmtctHomeVisitContract.InteractorCallBack callBack);

        void calculateActions(View view, MemberObject memberObject, BasePmtctHomeVisitContract.InteractorCallBack callBack);

        void submitVisit(boolean editMode, String memberID, Map<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> map, InteractorCallBack callBack);
    }

    interface InteractorCallBack {

        void onMemberDetailsReloaded(MemberObject memberObject);

        void onRegistrationSaved(boolean isEdit);

        void preloadActions(LinkedHashMap<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> map);

        void onSubmitted(boolean successful);
    }
}