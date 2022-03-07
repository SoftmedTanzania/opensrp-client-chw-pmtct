package org.smartregister.chw.pmtct.contract;

import com.vijay.jsonwizard.domain.Form;

public interface BaseHvlResultsViewContract {
    interface View {
        HvlResultsFragmentContract.Presenter presenter();

        Form getFormConfig();
    }

}
