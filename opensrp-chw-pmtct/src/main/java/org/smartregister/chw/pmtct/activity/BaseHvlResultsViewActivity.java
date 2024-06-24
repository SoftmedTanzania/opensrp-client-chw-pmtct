package org.smartregister.chw.pmtct.activity;

import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;
import org.smartregister.chw.pmtct.fragment.BaseHvlResultsFragment;
import org.smartregister.pmtct.R;
import org.smartregister.view.activity.SecuredActivity;

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
