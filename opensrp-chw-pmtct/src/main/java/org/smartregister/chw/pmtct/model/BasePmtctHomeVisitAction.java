package org.smartregister.chw.pmtct.model;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.chw.pmtct.domain.VisitDetail;
import org.smartregister.chw.pmtct.fragment.BaseHomeVisitFragment;
import org.smartregister.chw.pmtct.util.JsonFormUtils;
import org.smartregister.util.FormUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vijay.jsonwizard.utils.NativeFormLangUtils.getTranslatedString;

/**
 * This action list allows users to either load a form or link it to a separate fragment.
 */
public class BasePmtctHomeVisitAction {

    private String baseEntityID;
    private String title;
    private String subTitle;
    private String disabledMessage;
    private Status actionStatus;
    private PayloadType payloadType;
    private String payloadDetails;
    private ScheduleStatus scheduleStatus;
    private ProcessingMode processingMode;
    private boolean optional;
    private BaseHomeVisitFragment destinationFragment;
    private String formName;
    private String jsonPayload;
    private String selectedOption;
    private PmtctHomeVisitActionHelper pmtctHomeVisitActionHelper;
    private Map<String, List<VisitDetail>> details;
    private Context context;
    private Validator validator;

    private BasePmtctHomeVisitAction(Builder builder) throws ValidationException {
        this.baseEntityID = builder.baseEntityID;
        this.title = builder.title;
        this.subTitle = builder.subTitle;
        this.disabledMessage = builder.disabledMessage;
        this.actionStatus = builder.actionStatus;
        this.payloadType = builder.payloadType;
        this.payloadDetails = builder.payloadDetails;
        this.scheduleStatus = builder.scheduleStatus;
        this.optional = builder.optional;
        this.destinationFragment = builder.destinationFragment;
        this.formName = builder.formName;
        this.pmtctHomeVisitActionHelper = builder.pmtctHomeVisitActionHelper;
        this.details = builder.details;
        this.context = builder.context;
        this.processingMode = builder.processingMode;
        this.jsonPayload = builder.jsonPayload;
        this.validator = builder.validator;

        validateMe();
        initialize();
    }

    private void initialize() {
        try {
            if (StringUtils.isBlank(jsonPayload) && StringUtils.isNotBlank(formName)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(getTranslatedString(FormUtils.getInstance(context).getFormJson(formName).toString(), context));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // update the form details
                if (details != null && details.size() > 0) {
                    JsonFormUtils.populateForm(jsonObject, details);
                }

                jsonPayload = jsonObject.toString();
            }

            if (pmtctHomeVisitActionHelper != null) {
                pmtctHomeVisitActionHelper.onJsonFormLoaded(jsonPayload, context, details);
                String pre_processed = pmtctHomeVisitActionHelper.getPreProcessed();
                if (StringUtils.isNotBlank(pre_processed)) {
                    JSONObject jsonObject = new JSONObject(pre_processed);
                    JsonFormUtils.populateForm(jsonObject, details);

                    this.jsonPayload = jsonObject.toString();
                }

                String sub_title = pmtctHomeVisitActionHelper.getPreProcessedSubTitle();
                if (StringUtils.isNotBlank(sub_title)) {
                    this.subTitle = sub_title;
                }

                ScheduleStatus status = pmtctHomeVisitActionHelper.getPreProcessedStatus();
                if (status != null) {
                    this.scheduleStatus = status;
                }
            }

            if (details != null && details.size() > 0) {
                if (destinationFragment != null) {
                    setJsonPayload(destinationFragment.getJsonObject().toString()); // force reload
                } else {
                    setJsonPayload(this.jsonPayload); // force reload
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate that action object has a proper end point destination
     */
    private void validateMe() throws ValidationException {
        if (StringUtils.isBlank(formName) && destinationFragment == null) {
            throw new ValidationException("This action object lacks a valid form or destination fragment");
        }
    }

    public boolean isValid() {
        if (validator != null)
            return validator.isValid(title);

        return true;
    }

    public boolean isEnabled() {
        if (validator != null)
            return validator.isEnabled(title);

        return true;
    }

    public String getBaseEntityID() {
        return baseEntityID;
    }

    public void setBaseEntityID(String baseEntityID) {
        this.baseEntityID = baseEntityID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDisabledMessage() {
        return disabledMessage;
    }

    public void setDisabledMessage(String disabledMessage) {
        this.disabledMessage = disabledMessage;
    }

    public Status getActionStatus() {
        return actionStatus;
    }

    public void setActionStatus(Status actionStatus) {
        this.actionStatus = actionStatus;
    }

    public PayloadType getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(PayloadType payloadType) {
        this.payloadType = payloadType;
    }

    public String getPayloadDetails() {
        return payloadDetails;
    }

    public void setPayloadDetails(String payloadDetails) {
        this.payloadDetails = payloadDetails;
    }

    public ScheduleStatus getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(ScheduleStatus scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public ProcessingMode getProcessingMode() {
        return processingMode;
    }

    public void setProcessingMode(ProcessingMode processingMode) {
        this.processingMode = processingMode;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public String getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
        if (StringUtils.isNotBlank(jsonPayload))
            this.setScheduleStatus(ScheduleStatus.DUE);

        // helper processing
        onPayloadReceivedNotifyHelper(jsonPayload);

        if (validator != null)
            validator.onChanged(title);

        evaluateStatus();
    }

    private void onPayloadReceivedNotifyHelper(String jsonPayload) {
        if (pmtctHomeVisitActionHelper == null)
            return;

        pmtctHomeVisitActionHelper.onPayloadReceived(jsonPayload);

        String sub_title = pmtctHomeVisitActionHelper.evaluateSubTitle();
        if (sub_title != null) {
            setSubTitle(sub_title);
        }

        String post_process = pmtctHomeVisitActionHelper.postProcess(jsonPayload);
        if (post_process != null) {
            this.jsonPayload = pmtctHomeVisitActionHelper.postProcess(jsonPayload);
        }

        pmtctHomeVisitActionHelper.onPayloadReceived(this);
    }

    public void setProcessedJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public BaseHomeVisitFragment getDestinationFragment() {
        return destinationFragment;
    }

    public void setDestinationFragment(BaseHomeVisitFragment destinationFragment) {
        this.destinationFragment = destinationFragment;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public PmtctHomeVisitActionHelper getPmtctHomeVisitActionHelper() {
        return pmtctHomeVisitActionHelper;
    }

    public void setPmtctHomeVisitActionHelper(PmtctHomeVisitActionHelper pmtctHomeVisitActionHelper) {
        this.pmtctHomeVisitActionHelper = pmtctHomeVisitActionHelper;
    }

    /**
     * This value will evaluate the json payload as complete if payload is preset
     * or pending if the payload is not present. Any custom execution will also be processed to get the final value
     */
    public void evaluateStatus() {
        setActionStatus(computedStatus());

        if (getPmtctHomeVisitActionHelper() != null) {
            setActionStatus(getPmtctHomeVisitActionHelper().evaluateStatusOnPayload());
        }
    }

    public org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction.Status computedStatus() {
        if (StringUtils.isNotBlank(getJsonPayload())) {
            return Status.COMPLETED;
        } else {
            return Status.PENDING;
        }
    }

    public enum Status {COMPLETED, PARTIALLY_COMPLETED, PENDING}

    public enum ScheduleStatus {DUE, OVERDUE}

    public enum PayloadType {JSON, SERVICE, VACCINE}

    /**
     * Detached processing generates separate event when form is submitted
     */
    public enum ProcessingMode {COMBINED, SEPARATE}

    public interface PmtctHomeVisitActionHelper {

        /**
         * Inject values to the json form before rendering
         * Only called once after the form has been read from the assets folder
         */
        void onJsonFormLoaded(String jsonString, Context context, Map<String, List<VisitDetail>> details);

        /**
         * executed after form is loaded.
         * Returns a string or null
         */
        String getPreProcessed();

        /**
         * Is executed immediately a json payload is received
         *
         * @param jsonPayload
         */
        void onPayloadReceived(String jsonPayload);

        /**
         * executed after form is loaded on start
         * add functionality to evaluate the state of the view immediately the form is processed
         */
        ScheduleStatus getPreProcessedStatus();

        /**
         * executed after form is loaded on start
         * add functionality to evaluate the subtitle information immediately the form is processed
         */
        String getPreProcessedSubTitle();

        /**
         * add details to process the received payload
         *
         * @param jsonPayload
         */
        String postProcess(String jsonPayload);

        /**
         * executed after the payload is received
         */
        String evaluateSubTitle();

        /**
         * Evaluated after payload is received
         *
         * @return
         */
        Status evaluateStatusOnPayload();

        /**
         * Custom processing after payload is received
         */
        void onPayloadReceived(org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction pmtctHomeVisitAction);
    }

    public static class Builder {
        private String baseEntityID;
        private String title;
        private String subTitle;
        private String disabledMessage;
        private Status actionStatus = Status.PENDING;
        private PayloadType payloadType = PayloadType.JSON;
        private String payloadDetails;
        private ScheduleStatus scheduleStatus = ScheduleStatus.DUE;
        private ProcessingMode processingMode = ProcessingMode.COMBINED;
        private boolean optional = true;
        private BaseHomeVisitFragment destinationFragment;
        private String formName;
        private PmtctHomeVisitActionHelper pmtctHomeVisitActionHelper;
        private Map<String, List<VisitDetail>> details = new HashMap<>();
        private Context context;
        private String jsonPayload;
        private Validator validator;

        public Builder(Context context, String title) {
            this.context = context;
            this.title = title;
        }

        public Builder withBaseEntityID(String baseEntityID) {
            this.baseEntityID = baseEntityID;
            return this;
        }

        public Builder withSubtitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder withDisabledMessage(String disabledMessage) {
            this.disabledMessage = disabledMessage;
            return this;
        }

        public Builder withPayloadType(PayloadType payloadType) {
            this.payloadType = payloadType;
            return this;
        }

        public Builder withPayloadDetails(String payloadDetails) {
            this.payloadDetails = payloadDetails;
            return this;
        }

        public Builder withOptional(boolean optional) {
            this.optional = optional;
            return this;
        }

        public Builder withDestinationFragment(BaseHomeVisitFragment destinationFragment) {
            this.destinationFragment = destinationFragment;
            return this;
        }

        public Builder withFormName(String formName) {
            this.formName = formName;
            return this;
        }

        public Builder withDetails(Map<String, List<VisitDetail>> details) {
            this.details = details;
            return this;
        }

        public Builder withHelper(PmtctHomeVisitActionHelper pmtctHomeVisitActionHelper) {
            this.pmtctHomeVisitActionHelper = pmtctHomeVisitActionHelper;
            return this;
        }

        public Builder withScheduleStatus(ScheduleStatus scheduleStatus) {
            this.scheduleStatus = scheduleStatus;
            return this;
        }

        public Builder withProcessingMode(ProcessingMode processingMode) {
            this.processingMode = processingMode;
            return this;
        }

        public Builder withJsonPayload(String jsonPayload) {
            this.jsonPayload = jsonPayload;
            return this;
        }

        public Builder withValidator(Validator validator) {
            this.validator = validator;
            return this;
        }

        public org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction build() throws ValidationException {
            return new org.smartregister.chw.pmtct.model.BasePmtctHomeVisitAction(this);
        }
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * provides complex logic to validate is an object should be displayed
     * and the state it should be displated
     *
     * @return
     */
    public interface Validator {
        boolean isValid(String key);

        boolean isEnabled(String key);

        /**
         * notifies the validator that a change has occurred on this object
         *
         * @param key
         * @return
         */
        void onChanged(String key);
    }
}