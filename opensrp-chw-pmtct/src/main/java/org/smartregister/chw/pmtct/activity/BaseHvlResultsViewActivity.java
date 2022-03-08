package org.smartregister.chw.pmtct.activity;

import org.json.JSONObject;
import org.smartregister.chw.pmtct.fragment.BaseHvlResultsFragment;
import org.smartregister.chw.pmtct.util.DBConstants;
import org.smartregister.chw.pmtct.util.NCUtils;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.pmtct.R;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.view.activity.SecuredActivity;

import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.FragmentTransaction;

import static org.smartregister.chw.pmtct.util.Constants.EVENT_TYPE.PMTCT_HVL_RESULTS_EVENT;
import static org.smartregister.chw.pmtct.util.Constants.TABLES.PMTCT_HVL_RESULTS;
import static org.smartregister.util.Utils.getAllSharedPreferences;

public class BaseHvlResultsViewActivity extends SecuredActivity {

    @Override
    protected void onCreation() {
        setContentView(R.layout.base_hvl_results_view_activity);
        loadFragment();
    }


    @Override
    protected void onResumption() {
        //overridden
    }

    public BaseHvlResultsFragment getBaseFragment() {
        return new BaseHvlResultsFragment();
    }

    private void loadFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_placeholder, getBaseFragment());
        ft.commit();
    }

    public void startFormActivity(JSONObject jsonObject) {
        //implement
    }


}
