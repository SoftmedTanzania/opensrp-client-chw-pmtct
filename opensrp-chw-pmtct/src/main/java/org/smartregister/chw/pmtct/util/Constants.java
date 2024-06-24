package org.smartregister.chw.pmtct.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";
    String HOME_VISIT_GROUP = "home_visit_group";


    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String PMTCT_REGISTRATION = "PMTCT Registration";
        String PMTCT_FOLLOWUP = "PMTCT Follow-up Visit";
        String PMTCT_HVL_RESULTS_EVENT = "PMTCT HVL Results";
        String VOID_EVENT = "Void Event";
    }

    interface TABLES {
        String PMTCT_REGISTRATION = "ec_pmtct_registration";
        String PMTCT_FOLLOW_UP = "ec_pmtct_followup";
        String PMTCT_HVL_RESULTS = "ec_pmtct_hvl_results";
        String PMTCT_CD4_RESULTS = "ec_pmtct_cd4_results";
        String PMTCT_EAC_VISITS = "ec_pmtct_eac_visit";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String PMTCT_FORM_NAME = "PMTCT_FORM_NAME";
        String EDIT_MODE = "editMode";
        String PMTCT_FORM = "PMTCT_FORM";
        String PARENT_FORM_ENTITY_ID = "PARENT_FORM_ENTITY_ID";
        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String PMTCT_REGISTRATION = "pmtct_registration";
    }

    public class RISK_LEVELS {
        public static final String RISK_HIGH = "high_risk";
        public static final String RISK_MEDIUM = "medium_risk";
        public static final String RISK_LOW = "low_risk";
    }
}