package org.smartregister.provider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.chw.pmtct.fragment.BaseHvlResultsFragment;
import org.smartregister.chw.pmtct.util.DBConstants;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.pmtct.R;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;
import org.w3c.dom.Text;

import java.text.MessageFormat;
import java.util.Set;

import androidx.recyclerview.widget.RecyclerView;
import timber.log.Timber;

public class HvlResultsViewProvider implements RecyclerViewProvider<HvlResultsViewProvider.RegisterViewHolder> {

    private final LayoutInflater inflater;
    protected View.OnClickListener onClickListener;
    private View.OnClickListener paginationClickListener;
    private Context context;
    private Set<org.smartregister.configurableviews.model.View> visibleColumns;

    public HvlResultsViewProvider(Context context, View.OnClickListener paginationClickListener, View.OnClickListener onClickListener, Set visibleColumns) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.paginationClickListener = paginationClickListener;
        this.onClickListener = onClickListener;
        this.visibleColumns = visibleColumns;
        this.context = context;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient smartRegisterClient, RegisterViewHolder registerViewHolder) {
        CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        if (visibleColumns.isEmpty()) {
            populatePatientColumn(pc, registerViewHolder);
        }
    }


    @SuppressLint("SetTextI18n")
    private void populatePatientColumn(CommonPersonObjectClient pc, final RegisterViewHolder viewHolder) {
        try {

            String sampleId = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.HVL_SAMPLE_ID, false);
            String collectionDate = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.HVL_SAMPLE_COLLECTION_DATE, false);
            String hvlResult = Utils.getValue(pc.getColumnmaps(), DBConstants.KEY.HVL_RESULT, false);

            if (StringUtils.isBlank(hvlResult)) {
                viewHolder.hvlWrapper.setVisibility(View.GONE);
                viewHolder.dueWrapper.setVisibility(View.VISIBLE);
            } else {
                viewHolder.hvlResult.setText(hvlResult);
                viewHolder.hvlWrapper.setVisibility(View.VISIBLE);
                viewHolder.dueWrapper.setVisibility(View.GONE);
            }

            viewHolder.sampleId.setText(sampleId);
            viewHolder.collectionDate.setText(collectionDate);
            viewHolder.recordHvl.setTag(pc);
            viewHolder.recordHvl.setTag(R.id.VIEW_ID, BaseHvlResultsFragment.CLICK_VIEW_NORMAL);
            viewHolder.recordHvl.setOnClickListener(onClickListener);

        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void getFooterView(RecyclerView.ViewHolder viewHolder, int currentPageCount, int totalPageCount, boolean hasNext, boolean hasPrevious) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
        footerViewHolder.pageInfoView.setText(MessageFormat.format(context.getString(org.smartregister.R.string.str_page_info), currentPageCount, totalPageCount));

        footerViewHolder.nextPageView.setVisibility(hasNext ? View.VISIBLE : View.INVISIBLE);
        footerViewHolder.previousPageView.setVisibility(hasPrevious ? View.VISIBLE : View.INVISIBLE);

        footerViewHolder.nextPageView.setOnClickListener(paginationClickListener);
        footerViewHolder.previousPageView.setOnClickListener(paginationClickListener);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption filterOption, ServiceModeOption serviceModeOption, FilterOption filterOption1, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
//        implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String s, String s1, String s2) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public RegisterViewHolder createViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.hvl_result_list_row, parent, false);
        return new RegisterViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder createFooterHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.smart_register_pagination, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public boolean isFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        return viewHolder instanceof FooterViewHolder;
    }

    public class RegisterViewHolder extends RecyclerView.ViewHolder {
        public TextView sampleId;
        public TextView collectionDate;
        public TextView hvlResult;
        public RelativeLayout hvlWrapper;
        public TextView resultTitle;

        public Button recordHvl;
        public View dueWrapper;

        public RegisterViewHolder(View itemView) {
            super(itemView);

            sampleId = itemView.findViewById(R.id.sample_id);
            collectionDate = itemView.findViewById(R.id.collection_date);
            hvlResult = itemView.findViewById(R.id.result);
            hvlWrapper = itemView.findViewById(R.id.rlHvlWrapper);
            recordHvl = itemView.findViewById(R.id.record_hvl_button);
            dueWrapper = itemView.findViewById(R.id.due_button_wrapper);
            resultTitle = itemView.findViewById(R.id.hvl_result_title);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public TextView pageInfoView;
        public Button nextPageView;
        public Button previousPageView;

        public FooterViewHolder(View view) {
            super(view);

            nextPageView = view.findViewById(org.smartregister.R.id.btn_next_page);
            previousPageView = view.findViewById(org.smartregister.R.id.btn_previous_page);
            pageInfoView = view.findViewById(org.smartregister.R.id.txt_page_info);
        }
    }
}
