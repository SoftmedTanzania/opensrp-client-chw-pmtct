package org.smartregister.chw.pmtct.custom_views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.smartregister.chw.pmtct.domain.MemberObject;
import org.smartregister.chw.pmtct.fragment.BasePmtctCallDialogFragment;
import org.smartregister.pmtct.R;

public class BasePmtctFloatingMenu extends LinearLayout implements View.OnClickListener {
    private MemberObject MEMBER_OBJECT;

    public BasePmtctFloatingMenu(Context context, MemberObject MEMBER_OBJECT) {
        super(context);
        initUi();
        this.MEMBER_OBJECT = MEMBER_OBJECT;
    }

    protected void initUi() {
        inflate(getContext(), R.layout.view_pmtct_floating_menu, this);
        FloatingActionButton fab = findViewById(R.id.pmtct_fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.pmtct_fab) {
            Activity activity = (Activity) getContext();
            BasePmtctCallDialogFragment.launchDialog(activity, MEMBER_OBJECT);
        }
    }
}