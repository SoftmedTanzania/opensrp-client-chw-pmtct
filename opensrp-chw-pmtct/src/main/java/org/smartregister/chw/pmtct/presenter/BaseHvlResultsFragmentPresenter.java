package org.smartregister.chw.pmtct.presenter;

import org.smartregister.chw.pmtct.contract.HvlResultsFragmentContract;
import org.smartregister.chw.pmtct.util.Constants;
import org.smartregister.chw.pmtct.util.DBConstants;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.TreeSet;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseHvlResultsFragmentPresenter implements HvlResultsFragmentContract.Presenter {
    protected WeakReference<HvlResultsFragmentContract.View> viewReference;
    protected HvlResultsFragmentContract.Model model;

    protected RegisterConfiguration config;
    protected Set<View> visibleColumns = new TreeSet<>();
    protected String viewConfigurationIdentifier;


    public BaseHvlResultsFragmentPresenter(HvlResultsFragmentContract.View view, HvlResultsFragmentContract.Model model, String viewConfigurationIdentifier) {
        this.viewReference = new WeakReference<>(view);
        this.model = model;
        this.viewConfigurationIdentifier = viewConfigurationIdentifier;
        this.config = model.defaultRegisterConfiguration();
    }

    @Override
    public String getMainCondition() {
        return " " + Constants.TABLES.PMTCT_FOLLOW_UP + "." + DBConstants.KEY.HVL_SAMPLE_ID + " IS NOT NULL ";
    }

    @Override
    public String getDefaultSortQuery() {
        return "";
    }

    @Override
    public String getMainTable() {
        return Constants.TABLES.PMTCT_FOLLOW_UP;
    }

    @Override
    public String getDueFilterCondition() {
        return null;
    }


    @Override
    public void processViewConfigurations() {
        //implement
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = getMainTable();
        mainCondition = trim(getMainCondition()).equals("") ? mainCondition : getMainCondition();
        String countSelect = model.countSelect(tableName, mainCondition);
        String mainSelect = model.mainSelect(tableName, mainCondition);

        if (getView() != null) {

            getView().initializeQueryParams(tableName, countSelect, mainSelect);
            getView().initializeAdapter(visibleColumns);

            getView().countExecute();
            getView().filterandSortInInitializeQueries();
        }
    }


    protected HvlResultsFragmentContract.View getView() {
        if (viewReference != null)
            return viewReference.get();
        else
            return null;
    }

    @Override
    public void startSync() {
        //implement
    }

    @Override
    public void searchGlobally(String s) {
        //implement
    }
}
