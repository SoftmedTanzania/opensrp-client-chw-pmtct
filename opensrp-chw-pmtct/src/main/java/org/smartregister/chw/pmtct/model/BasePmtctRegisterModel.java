package org.smartregister.chw.pmtct.model;

import org.json.JSONObject;
import org.smartregister.chw.pmtct.contract.PmtctRegisterContract;
import org.smartregister.chw.pmtct.util.PmtctJsonFormUtils;

public class BasePmtctRegisterModel implements PmtctRegisterContract.Model {

    @Override
    public JSONObject getFormAsJson(String formName, String entityId, String currentLocationId) throws Exception {
        JSONObject jsonObject = PmtctJsonFormUtils.getFormAsJson(formName);
        PmtctJsonFormUtils.getRegistrationForm(jsonObject, entityId, currentLocationId);

        return jsonObject;
    }

}
