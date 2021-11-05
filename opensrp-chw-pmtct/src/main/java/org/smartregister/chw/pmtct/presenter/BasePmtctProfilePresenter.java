package org.smartregister.chw.pmtct.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import org.smartregister.chw.pmtct.contract.PmtctProfileContract;
import org.smartregister.chw.pmtct.domain.MemberObject;

import java.lang.ref.WeakReference;

import timber.log.Timber;


public class BasePmtctProfilePresenter implements PmtctProfileContract.Presenter {
    protected WeakReference<PmtctProfileContract.View> view;
    protected MemberObject memberObject;
    protected PmtctProfileContract.Interactor interactor;
    protected Context context;

    public BasePmtctProfilePresenter(PmtctProfileContract.View view, PmtctProfileContract.Interactor interactor, MemberObject memberObject) {
        this.view = new WeakReference<>(view);
        this.memberObject = memberObject;
        this.interactor = interactor;
    }

    @Override
    public void fillProfileData(MemberObject memberObject) {
        if (memberObject != null && getView() != null) {
            getView().setProfileViewWithData();
        }
    }

    @Override
    public void recordPmtctButton(@Nullable String visitState) {
        if (getView() == null) {
            return;
        }
        if (("OVERDUE").equals(visitState) || ("DUE").equals(visitState)) {
            if (("OVERDUE").equals(visitState)) {
                getView().setOverDueColor();
            }
        } else {
            getView().hideView();
        }
    }

    @Override
    @Nullable
    public PmtctProfileContract.View getView() {
        if (view != null && view.get() != null)
            return view.get();

        return null;
    }

    @Override
    public void refreshProfileBottom() {
        interactor.refreshProfileInfo(memberObject, getView());
    }

    @Override
    public void saveForm(String jsonString) {
        try {
            interactor.saveRegistration(jsonString, getView());
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
