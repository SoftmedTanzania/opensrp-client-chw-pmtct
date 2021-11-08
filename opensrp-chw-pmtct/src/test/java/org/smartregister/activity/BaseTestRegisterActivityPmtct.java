package org.smartregister.activity;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.chw.pmtct.activity.BasePmtctRegisterActivity;

public class BaseTestRegisterActivityPmtct {
    @Mock
    public Intent data;
    @Mock
    private BasePmtctRegisterActivity basePmtctRegisterActivity = new BasePmtctRegisterActivity();

    @Test
    public void assertNotNull() {
        Assert.assertNotNull(basePmtctRegisterActivity);
    }

    @Test
    public void testFormConfig() {
        Assert.assertNull(basePmtctRegisterActivity.getFormConfig());
    }

    @Test
    public void checkIdentifier() {
        Assert.assertNotNull(basePmtctRegisterActivity.getViewIdentifiers());
    }

    @Test(expected = Exception.class)
    public void onActivityResult() throws Exception {
        Whitebox.invokeMethod(basePmtctRegisterActivity, "onActivityResult", 2244, -1, data);
        Mockito.verify(basePmtctRegisterActivity.presenter()).saveForm(null);
    }

}
