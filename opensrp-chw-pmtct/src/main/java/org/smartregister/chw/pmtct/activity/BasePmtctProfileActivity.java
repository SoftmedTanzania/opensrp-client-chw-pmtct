package org.smartregister.chw.pmtct.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.pmtct.contract.PmtctProfileContract;
import org.smartregister.chw.pmtct.custom_views.BasePmtctFloatingMenu;
import org.smartregister.chw.pmtct.dao.PmtctDao;
import org.smartregister.chw.pmtct.domain.MemberObject;
import org.smartregister.chw.pmtct.interactor.BasePmtctProfileInteractor;
import org.smartregister.chw.pmtct.presenter.BasePmtctProfilePresenter;
import org.smartregister.chw.pmtct.util.Constants;
import org.smartregister.chw.pmtct.util.PmtctUtil;
import org.smartregister.domain.AlertStatus;
import org.smartregister.helper.ImageRenderHelper;
import org.smartregister.pmtct.R;
import org.smartregister.view.activity.BaseProfileActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


public class BasePmtctProfileActivity extends BaseProfileActivity implements PmtctProfileContract.View, PmtctProfileContract.InteractorCallBack {
    protected MemberObject memberObject;
    protected PmtctProfileContract.Presenter profilePresenter;
    protected CircleImageView imageView;
    protected TextView textViewName;
    protected TextView textViewMotherName;
    protected TextView textViewGender;
    protected TextView textViewLocation;
    protected TextView textViewUniqueID;
    protected TextView textViewClientRegNumber;
    protected TextView textViewRecordPmtct;
    protected TextView textViewRecordEac;
    protected TextView textViewRecordAnc;
    protected TextView textViewNextVisit;
    protected View view_last_visit_row;
    protected View view_most_due_overdue_row;
    protected TextView riskLabel;
    protected View view_family_row;
    protected View view_positive_date_row;
    protected View view_hvl_results_row;
    protected View view_baseline_results_row;
    protected RelativeLayout rlLastVisit;
    protected RelativeLayout rlNextVisit;
    protected RelativeLayout rlUpcomingServices;
    protected RelativeLayout rlFamilyServicesDue;
    protected RelativeLayout visitStatus;
    protected RelativeLayout rlHvlResults;
    protected RelativeLayout rlBaselineResults;
    protected ImageView imageViewCross;
    protected TextView textViewUndo;
    protected TextView textViewVisitDone;
    protected RelativeLayout visitDone;
    protected LinearLayout recordVisits;
    protected TextView textViewVisitDoneEdit;
    protected TextView textViewRecordAncNotDone;
    protected BasePmtctFloatingMenu basePmtctFloatingMenu;
    private TextView tvUpComingServices;
    private TextView tvFamilyStatus;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
    private ProgressBar progressBar;

    public static void startProfileActivity(Activity activity, String baseEntityId) {
        Intent intent = new Intent(activity, BasePmtctProfileActivity.class);
        intent.putExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID, baseEntityId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreation() {
        setContentView(R.layout.activity_pmtct_profile);
        Toolbar toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        String baseEntityId = getIntent().getStringExtra(Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
            upArrow.setColorFilter(getResources().getColor(R.color.text_blue), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        toolbar.setNavigationOnClickListener(v -> BasePmtctProfileActivity.this.finish());
        appBarLayout = this.findViewById(R.id.collapsing_toolbar_appbarlayout);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider(null);
        }

        textViewName = findViewById(R.id.textview_name);
        textViewMotherName = findViewById(R.id.textview_name_mother);
        textViewGender = findViewById(R.id.textview_gender);
        textViewLocation = findViewById(R.id.textview_address);
        textViewUniqueID = findViewById(R.id.textview_id);
        view_last_visit_row = findViewById(R.id.view_last_visit_row);
        view_most_due_overdue_row = findViewById(R.id.view_most_due_overdue_row);
        view_family_row = findViewById(R.id.view_family_row);
        view_positive_date_row = findViewById(R.id.view_positive_date_row);
        imageViewCross = findViewById(R.id.tick_image);
        tvUpComingServices = findViewById(R.id.textview_name_due);
        tvFamilyStatus = findViewById(R.id.textview_family_has);
        textViewNextVisit = findViewById(R.id.tv_next_visit);
        textViewClientRegNumber = findViewById(R.id.client_reg_number);

        riskLabel = findViewById(R.id.risk_label);

        rlLastVisit = findViewById(R.id.rlLastVisit);
        rlNextVisit = findViewById(R.id.rlNextVisit);
        rlUpcomingServices = findViewById(R.id.rlUpcomingServices);
        rlFamilyServicesDue = findViewById(R.id.rlFamilyServicesDue);
        textViewVisitDone = findViewById(R.id.textview_visit_done);
        visitStatus = findViewById(R.id.record_visit_not_done_bar);
        visitDone = findViewById(R.id.visit_done_bar);
        recordVisits = findViewById(R.id.record_visits);
        progressBar = findViewById(R.id.progress_bar);
        textViewRecordAncNotDone = findViewById(R.id.textview_record_anc_not_done);
        textViewVisitDoneEdit = findViewById(R.id.textview_edit);
        textViewRecordPmtct = findViewById(R.id.textview_record_pmtct);
        textViewRecordEac = findViewById(R.id.textview_record_eac);
        textViewRecordAnc = findViewById(R.id.textview_record_anc);
        textViewUndo = findViewById(R.id.textview_undo);
        imageView = findViewById(R.id.imageview_profile);
        view_hvl_results_row = findViewById(R.id.view_hvl_results_row);
        view_baseline_results_row = findViewById(R.id.view_baseline_results_row);
        rlBaselineResults = findViewById(R.id.rlBaselineInvestigationResults);
        rlHvlResults = findViewById(R.id.rlHvlResults);

        textViewVisitDoneEdit.setOnClickListener(this);
        textViewVisitDoneEdit.setVisibility(View.GONE);

        textViewRecordAncNotDone.setOnClickListener(this);
        rlHvlResults.setOnClickListener(this);
        rlBaselineResults.setOnClickListener(this);
        rlLastVisit.setOnClickListener(this);
        rlUpcomingServices.setOnClickListener(this);
        rlFamilyServicesDue.setOnClickListener(this);
        textViewRecordPmtct.setOnClickListener(this);
        textViewRecordAnc.setOnClickListener(this);
        textViewUndo.setOnClickListener(this);

        imageRenderHelper = new ImageRenderHelper(this);
        memberObject = PmtctDao.getMember(baseEntityId);
        initializePresenter();
        profilePresenter.fillProfileData(memberObject);
        setupViews();
        initializeFloatingMenu();
    }

    @Override
    protected void setupViews() {
        recordAnc(memberObject);
        recordPnc(memberObject);
    }

    @Override
    public void recordAnc(MemberObject memberObject) {
        //implement
    }

    @Override
    public void recordPnc(MemberObject memberObject) {
        //implement
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.title_layout) {
            onBackPressed();
        } else if (id == R.id.rlLastVisit) {
            this.openMedicalHistory();
        } else if (id == R.id.rlUpcomingServices) {
            this.openUpcomingService();
        } else if (id == R.id.rlFamilyServicesDue) {
            this.openFamilyDueServices();
        } else if (id == R.id.rlHvlResults) {
            this.openHvlResultsHistory();
        } else if (id == R.id.rlBaselineInvestigationResults) {
            this.openBaselineInvestigationResults();
        }
    }

    @Override
    protected void initializePresenter() {
        showProgressBar(true);
        profilePresenter = new BasePmtctProfilePresenter(this, new BasePmtctProfileInteractor(), memberObject);
        fetchProfileData();
        profilePresenter.refreshProfileBottom();
    }

    public void initializeFloatingMenu() {
        if (StringUtils.isNotBlank(memberObject.getPhoneNumber())) {
            basePmtctFloatingMenu = new BasePmtctFloatingMenu(this, memberObject);
            basePmtctFloatingMenu.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(basePmtctFloatingMenu, linearLayoutParams);
        }
    }

    @Override
    public void hideView() {
        textViewRecordPmtct.setVisibility(View.GONE);
    }

    @Override
    public void showDone() {
        visitDone.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDone() {
        visitDone.setVisibility(View.GONE);
    }

    @Override
    public void showNextDue() {
        rlNextVisit.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNextDue() {
        rlNextVisit.setVisibility(View.GONE);
    }

    @Override
    public void setDueDays(String dueDays) {
        textViewNextVisit.setText(getString(R.string.next_visit_date, dueDays));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void setProfileViewWithData() {
        int age = memberObject.getAge();
        textViewName.setText(String.format("%s %s %s, %d", memberObject.getFirstName(),
                memberObject.getMiddleName(), memberObject.getLastName(), age));
        textViewGender.setText(PmtctUtil.getGenderTranslated(this, memberObject.getGender()));
        textViewLocation.setText(memberObject.getAddress());
        textViewUniqueID.setText(memberObject.getUniqueId());
    }

    public void showRiskLabel(String riskLevel) {
        if (riskLabel != null && StringUtils.isNotBlank(riskLevel)) {
            int labelTextColor;
            int background;
            String labelText;
            switch (riskLevel) {
                case Constants.RISK_LEVELS.RISK_LOW:
                    labelTextColor = context().getColorResource(R.color.low_risk_text_green);
                    background = R.drawable.low_risk_label;
                    labelText = getString(R.string.low_risk);
                    break;
                case Constants.RISK_LEVELS.RISK_MEDIUM:
                    labelTextColor = context().getColorResource(R.color.medium_risk_text_orange);
                    background = R.drawable.medium_risk_label;
                    labelText = getString(R.string.medium_risk);
                    break;
                case Constants.RISK_LEVELS.RISK_HIGH:
                    labelTextColor = context().getColorResource(R.color.high_risk_text_red);
                    background = R.drawable.high_risk_label;
                    labelText = getString(R.string.high_risk);
                    break;
                default:
                    labelTextColor = context().getColorResource(R.color.default_risk_text_black);
                    background = R.drawable.risk_label;
                    labelText = getString(R.string.low_risk);
                    break;
            }
            riskLabel.setVisibility(View.VISIBLE);
            riskLabel.setText(labelText);
            riskLabel.setTextColor(labelTextColor);
            riskLabel.setBackgroundResource(background);
        }
    }

    @Override
    public void setOverDueColor() {
        textViewRecordPmtct.setBackground(getResources().getDrawable(R.drawable.record_btn_selector_overdue));
    }

    @Override
    protected ViewPager setupViewPager(ViewPager viewPager) {
        return null;
    }

    @Override
    protected void fetchProfileData() {
        //fetch profile data
    }

    @Override
    public void showProgressBar(boolean status) {
        progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void refreshMedicalHistory(boolean hasHistory) {
        showProgressBar(false);
        rlLastVisit.setVisibility(hasHistory ? View.VISIBLE : View.GONE);
    }

    @Override
    public void refreshUpComingServicesStatus(String service, AlertStatus status, Date date) {
        showProgressBar(false);
        if (status == AlertStatus.complete)
            return;
        view_most_due_overdue_row.setVisibility(View.VISIBLE);
        rlUpcomingServices.setVisibility(View.VISIBLE);

        if (status == AlertStatus.upcoming) {
            tvUpComingServices.setText(PmtctUtil.fromHtml(getString(R.string.vaccine_service_upcoming, service, dateFormat.format(date))));
        } else {
            tvUpComingServices.setText(PmtctUtil.fromHtml(getString(R.string.vaccine_service_due, service, dateFormat.format(date))));
        }
    }

    @Override
    public void refreshFamilyStatus(AlertStatus status) {
        showProgressBar(false);
        if (status == AlertStatus.complete) {
            setFamilyStatus(getString(R.string.family_has_nothing_due));
        } else if (status == AlertStatus.normal) {
            setFamilyStatus(getString(R.string.family_has_services_due));
        } else if (status == AlertStatus.urgent) {
            tvFamilyStatus.setText(PmtctUtil.fromHtml(getString(R.string.family_has_service_overdue)));
        }
    }

    private void setFamilyStatus(String familyStatus) {
        view_family_row.setVisibility(View.VISIBLE);
        rlFamilyServicesDue.setVisibility(View.VISIBLE);
        tvFamilyStatus.setText(familyStatus);
    }

    @Override
    public void openMedicalHistory() {
        //implement
    }

    @Override
    public void openUpcomingService() {
        //implement
    }

    @Override
    public void openFamilyDueServices() {
        //implement
    }

    @Override
    public void openHvlResultsHistory() {
        //implement
    }

    @Override
    public void openBaselineInvestigationResults() {
        //implement
    }

    @Nullable
    private String formatTime(Date dateTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            return formatter.format(dateTime);
        } catch (Exception e) {
            Timber.d(e);
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_GET_JSON && resultCode == RESULT_OK) {
            profilePresenter.saveForm(data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON));
            finish();
        }
    }
}
