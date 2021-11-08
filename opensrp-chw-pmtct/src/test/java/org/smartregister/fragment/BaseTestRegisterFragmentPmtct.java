package org.smartregister.fragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.pmtct.activity.BasePmtctProfileActivity;
import org.smartregister.chw.pmtct.fragment.BasePmtctRegisterFragment;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import static org.mockito.Mockito.times;

public class BaseTestRegisterFragmentPmtct {
    @Mock
    public BasePmtctRegisterFragment basePmtctRegisterFragment;

    @Mock
    public CommonPersonObjectClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = Exception.class)
    public void openProfile() throws Exception {
        Whitebox.invokeMethod(basePmtctRegisterFragment, "openProfile", client);
        PowerMockito.mockStatic(BasePmtctProfileActivity.class);
        BasePmtctProfileActivity.startProfileActivity(null, null);
        PowerMockito.verifyStatic(times(1));

    }
}
