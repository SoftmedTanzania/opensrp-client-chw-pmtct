package org.smartregister.chw.pmtct.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.Form;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.chw.pmtct.PmtctLibrary;
import org.smartregister.chw.pmtct.adapter.BasePmtctHomeVisitAdapter;
import org.smartregister.chw.pmtct.contract.BasePmtctHomeVisitContract;
import org.smartregister.chw.pmtct.domain.MemberObject;
import org.smartregister.chw.pmtct.interactor.BasePmtctHomeVisitInteractor;
import org.smartregister.chw.pmtct.presenter.BasePmtctHomeVisitPresenter;
import org.smartregister.chw.pmtct.util.Constants;
import org.smartregister.pmtct.R;
import org.smartregister.view.activity.SecuredActivity;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

import static org.smartregister.chw.pmtct.util.Constants.ACTIVITY_PAYLOAD.BASE_ENTITY_ID;
import static org.smartregister.chw.pmtct.util.Constants.ACTIVITY_PAYLOAD.EDIT_MODE;
import static org.smartregister.chw.pmtct.util.Constants.ACTIVITY_PAYLOAD.MEMBER_PROFILE_OBJECT;

public class BasePmtctHomeVisitActivity extends SecuredActivity implements BasePmtctHomeVisitContract.View, View.OnClickListener {

    private static final String TAG = org.smartregister.chw.pmtct.activity.BasePmtctHomeVisitActivity.class.getCanonicalName();
    protected Map<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> actionList = new LinkedHashMap<>();
    protected org.smartregister.chw.pmtct.contract.BasePmtctHomeVisitContract.Presenter presenter;
    protected MemberObject memberObject;
    protected String baseEntityID;
    protected Boolean isEditMode = false;
    protected RecyclerView.Adapter mAdapter;
    protected ProgressBar progressBar;
    protected TextView tvSubmit;
    protected TextView tvTitle;
    protected String current_action;
    protected String confirmCloseTitle;
    protected String confirmCloseMessage;

    public static void startMe(Activity activity, String baseEntityID, Boolean isEditMode) {
        Intent intent = new Intent(activity, org.smartregister.chw.pmtct.activity.BasePmtctHomeVisitActivity.class);
        intent.putExtra(BASE_ENTITY_ID, baseEntityID);
        intent.putExtra(EDIT_MODE, isEditMode);
        activity.startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_pmtct_homevisit);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            memberObject = (MemberObject) getIntent().getSerializableExtra(MEMBER_PROFILE_OBJECT);
            isEditMode = getIntent().getBooleanExtra(EDIT_MODE, false);
            baseEntityID = getIntent().getStringExtra(BASE_ENTITY_ID);
        }

        confirmCloseTitle = getString(R.string.confirm_form_close);
        confirmCloseMessage = getString(R.string.confirm_form_close_explanation);
        setUpView();
        displayProgressBar(true);
        registerPresenter();
        if (presenter != null) {
            if (StringUtils.isNotBlank(baseEntityID)) {
                presenter.reloadMemberDetails(baseEntityID);
            } else {
                presenter.initialize();
            }
        }
    }

    public void setUpView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.close).setOnClickListener(this);
        tvSubmit = findViewById(R.id.customFontTextViewSubmit);
        tvSubmit.setOnClickListener(this);
        tvTitle = findViewById(R.id.customFontTextViewName);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new BasePmtctHomeVisitAdapter(this, this, (LinkedHashMap) actionList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        redrawVisitUI();
    }

    protected void registerPresenter() {
        presenter = new BasePmtctHomeVisitPresenter(memberObject, this, new BasePmtctHomeVisitInteractor());
    }

    @Override
    public void initializeActions(LinkedHashMap<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> map) {
        for (Map.Entry<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> entry : map.entrySet()) {
            actionList.put(entry.getKey(), entry.getValue());
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        displayProgressBar(false);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Boolean getEditMode() {
        return isEditMode;
    }

    @Override
    public void onMemberDetailsReloaded(MemberObject memberObject) {
        this.memberObject = memberObject;
        presenter.initialize();
        redrawHeader(memberObject);
    }

    @Override
    protected void onCreation() {
        Timber.v("Empty onCreation");
    }

    @Override
    protected void onResumption() {
        Timber.v("Empty onResumption");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.close) {
            displayExitDialog(() -> close());
        } else if (v.getId() == R.id.customFontTextViewSubmit) {
            submitVisit();
        }
    }

    @Override
    public BasePmtctHomeVisitContract.Presenter presenter() {
        return presenter;
    }

    @Override
    public Form getFormConfig() {
        return null;
    }

    @Override
    public void startForm(org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction) {
        current_action = pmtctHomeVisitAction.getTitle();

        if (StringUtils.isNotBlank(pmtctHomeVisitAction.getJsonPayload())) {
            try {
                JSONObject jsonObject = new JSONObject(pmtctHomeVisitAction.getJsonPayload());
                startFormActivity(jsonObject);
            } catch (Exception e) {
                Timber.e(e);
                String locationId = PmtctLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
                presenter().startForm(pmtctHomeVisitAction.getFormName(), memberObject.getBaseEntityId(), locationId);
            }
        } else {
            String locationId = PmtctLibrary.getInstance().context().allSharedPreferences().getPreference(AllConstants.CURRENT_LOCATION_ID);
            presenter().startForm(pmtctHomeVisitAction.getFormName(), memberObject.getBaseEntityId(), locationId);
        }
    }

    @Override
    public void startFormActivity(JSONObject jsonForm) {
        Intent intent = new Intent(this, JsonFormActivity.class);
        intent.putExtra(Constants.JSON_FORM_EXTRA.JSON, jsonForm.toString());

        if (getFormConfig() != null) {
            intent.putExtra(JsonFormConstants.JSON_FORM_KEY.FORM, getFormConfig());
        }

        startActivityForResult(intent, Constants.REQUEST_CODE_GET_JSON);
    }

    @Override
    public void startFragment(org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction) {
        current_action = pmtctHomeVisitAction.getTitle();

        if (pmtctHomeVisitAction.getDestinationFragment() != null)
            pmtctHomeVisitAction.getDestinationFragment().show(getSupportFragmentManager(), current_action);

    }

    @Override
    public void redrawHeader(MemberObject memberObject) {
        tvTitle.setText(MessageFormat.format("{0}, {1} \u00B7 {2}", memberObject.getFullName(), memberObject.getAge(), getString(R.string.pmtct_visit)));
    }

    @Override
    public void redrawVisitUI() {
        boolean valid = actionList.size() > 0;
        for (Map.Entry<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> entry : actionList.entrySet()) {
            org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction action = entry.getValue();
            if (
                    (!action.isOptional() && (action.getActionStatus() == org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction.Status.PENDING && action.isValid()))
                            || !action.isEnabled()
            ) {
                valid = false;
                break;
            }
        }

        int res_color = valid ? R.color.white : R.color.light_grey;
        tvSubmit.setTextColor(getResources().getColor(res_color));
        tvSubmit.setOnClickListener(valid ? this : null); // update listener to null

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayProgressBar(boolean state) {
        progressBar.setVisibility(state ? View.VISIBLE : View.GONE);
    }


    @Override
    public Map<String, org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction> getPmtctHomeVisitActions() {
        return actionList;
    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public void submittedAndClose() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        close();
    }

    @Override
    public BasePmtctHomeVisitContract.Presenter getPresenter() {
        return presenter;
    }

    @Override
    public void submitVisit() {
        getPresenter().submitVisit();
    }

    @Override
    public void onDialogOptionUpdated(String jsonString) {
        org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction = actionList.get(current_action);
        if (pmtctHomeVisitAction != null) {
            pmtctHomeVisitAction.setJsonPayload(jsonString);
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            redrawVisitUI();
        }
    }

    @Override
    public Context getMyContext() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_GET_JSON) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String jsonString = data.getStringExtra(Constants.JSON_FORM_EXTRA.JSON);
                    org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction = actionList.get(current_action);
                    if (pmtctHomeVisitAction != null) {
                        pmtctHomeVisitAction.setJsonPayload(jsonString);
                    }
                } catch (Exception e) {
                    Timber.e(e);
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {

                org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction = actionList.get(current_action);
                if (pmtctHomeVisitAction != null)
                    pmtctHomeVisitAction.evaluateStatus();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        // update the adapter after every payload
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
            redrawVisitUI();
        }
    }

    @Override
    public void onBackPressed() {
        displayExitDialog(org.smartregister.chw.pmtct.activity.BasePmtctHomeVisitActivity.this::finish);
    }

    protected void displayExitDialog(final Runnable onConfirm) {
        AlertDialog dialog = new AlertDialog.Builder(this, com.vijay.jsonwizard.R.style.AppThemeAlertDialog).setTitle(confirmCloseTitle)
                .setMessage(confirmCloseMessage).setNegativeButton(com.vijay.jsonwizard.R.string.yes, (dialog1, which) -> {
                    if (onConfirm != null) {
                        onConfirm.run();
                    }
                }).setPositiveButton(com.vijay.jsonwizard.R.string.no, (dialog2, which) -> Timber.d("No button on dialog in %s", JsonFormActivity.class.getCanonicalName())).create();

        dialog.show();
    }

}