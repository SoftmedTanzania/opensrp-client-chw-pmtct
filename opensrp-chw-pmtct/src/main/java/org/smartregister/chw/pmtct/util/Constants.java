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
        String VOID_EVENT = "Void Event";
    }

    interface TABLES {
        String PMTCT_REGISTRATION = "ec_pmtct_registration";
        String PMTCT_FOLLOW_UP = "ec_pmtct_followup";
        String PMTCT_HVL_RESULTS = "ec_pmtct_hvl_results";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String PMTCT_FORM_NAME = "PMTCT_FORM_NAME";
        String EDIT_MODE = "editMode";
        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String PMTCT_REGISTRATION = "pmtct_registration";
    }

}