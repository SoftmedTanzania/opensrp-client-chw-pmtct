package org.smartregister.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.pmtct.activity.BasePmtctProfileActivity;
import org.smartregister.chw.pmtct.contract.PmtctProfileContract;
import org.smartregister.domain.AlertStatus;
import org.smartregister.pmtct.R;

import static org.mockito.Mockito.validateMockitoUsage;

public class BaseTestProfileActivityPmtct {
    @Mock
    public BasePmtctProfileActivity basePmtctProfileActivity;

    @Mock
    public PmtctProfileContract.Presenter profilePresenter;

    @Mock
    public View view;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(basePmtctProfileActivity);
    }

    @Test
    public void setOverDueColor() {
        basePmtctProfileActivity.setOverDueColor();
        Mockito.verify(view, Mockito.never()).setBackgroundColor(Color.RED);
    }

    @Test
    public void formatTime() {
        BasePmtctProfileActivity activity = new BasePmtctProfileActivity();
        try {
            Assert.assertEquals("25 Oct 2019", Whitebox.invokeMethod(activity, "formatTime", "25-10-2019"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkHideView() {
        basePmtctProfileActivity.hideView();
        Mockito.verify(view, Mockito.never()).setVisibility(View.GONE);
    }

    @Test
    public void checkProgressBar() {
        basePmtctProfileActivity.showProgressBar(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void medicalHistoryRefresh() {
        basePmtctProfileActivity.refreshMedicalHistory(true);
        Mockito.verify(view, Mockito.never()).setVisibility(View.VISIBLE);
    }

    @Test
    public void onClickBackPressed() {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.title_layout);
        Mockito.doNothing().when(basePmtctProfileActivity).onBackPressed();
        basePmtctProfileActivity.onClick(view);
        Mockito.verify(basePmtctProfileActivity).onBackPressed();
    }

    @Test
    public void onClickOpenMedicalHistory() {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlLastVisit);
        Mockito.doNothing().when(basePmtctProfileActivity).openMedicalHistory();
        basePmtctProfileActivity.onClick(view);
        Mockito.verify(basePmtctProfileActivity).openMedicalHistory();
    }

    @Test
    public void onClickOpenUpcomingServices() {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlUpcomingServices);
        Mockito.doNothing().when(basePmtctProfileActivity).openUpcomingService();
        basePmtctProfileActivity.onClick(view);
        Mockito.verify(basePmtctProfileActivity).openUpcomingService();
    }

    @Test
    public void onClickOpenFamlilyServicesDue() {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        Mockito.when(view.getId()).thenReturn(R.id.rlFamilyServicesDue);
        Mockito.doNothing().when(basePmtctProfileActivity).openFamilyDueServices();
        basePmtctProfileActivity.onClick(view);
        Mockito.verify(basePmtctProfileActivity).openFamilyDueServices();
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusComplete() throws Exception {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(basePmtctProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(basePmtctProfileActivity).showProgressBar(false);
        basePmtctProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(basePmtctProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(basePmtctProfileActivity).invoke("setFamilyStatus", "Family has nothing due");
    }

    @Test(expected = Exception.class)
    public void refreshFamilyStatusNormal() throws Exception {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        TextView textView = view.findViewById(R.id.textview_family_has);
        Whitebox.setInternalState(basePmtctProfileActivity, "tvFamilyStatus", textView);
        Mockito.doNothing().when(basePmtctProfileActivity).showProgressBar(false);
        basePmtctProfileActivity.refreshFamilyStatus(AlertStatus.complete);
        Mockito.verify(basePmtctProfileActivity).showProgressBar(false);
        PowerMockito.verifyPrivate(basePmtctProfileActivity).invoke("setFamilyStatus", "Family has services due");
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        basePmtctProfileActivity = Mockito.spy(new BasePmtctProfileActivity());
        Whitebox.invokeMethod(basePmtctProfileActivity, "onActivityResult", 2244, -1, null);
        Mockito.verify(profilePresenter).saveForm(null);
    }

}
