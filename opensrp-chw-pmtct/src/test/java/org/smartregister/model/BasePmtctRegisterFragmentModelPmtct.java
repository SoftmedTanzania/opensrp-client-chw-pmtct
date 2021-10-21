package org.smartregister.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.pmtct.model.BasePmtctRegisterFragmentModel;
import org.smartregister.configurableviews.model.RegisterConfiguration;
import org.smartregister.configurableviews.model.View;
import org.smartregister.configurableviews.model.ViewConfiguration;

import java.util.HashSet;
import java.util.Set;

public class BasePmtctRegisterFragmentModelPmtct {

    @Mock
    private BasePmtctRegisterFragmentModel basePmtctRegisterFragmentModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDefaultRegisterConfiguration() {
        RegisterConfiguration configuration = new RegisterConfiguration();
        Mockito.when(basePmtctRegisterFragmentModel.defaultRegisterConfiguration())
                .thenReturn(configuration);
        Assert.assertEquals(configuration, basePmtctRegisterFragmentModel.defaultRegisterConfiguration());
    }

    @Test
    public void testGetViewConfiguration() {
        ViewConfiguration viewConfiguration = new ViewConfiguration();
        Mockito.when(basePmtctRegisterFragmentModel.getViewConfiguration(Mockito.anyString()))
                .thenReturn(viewConfiguration);
        Assert.assertEquals(viewConfiguration, basePmtctRegisterFragmentModel.getViewConfiguration(Mockito.anyString()));
    }

    @Test
    public void testGetRegisterActiveColumns() {
        Set<View> views = new HashSet<View>();
        Mockito.when(basePmtctRegisterFragmentModel.getRegisterActiveColumns(Mockito.anyString()))
                .thenReturn(views);
        Assert.assertEquals(views, basePmtctRegisterFragmentModel.getRegisterActiveColumns(Mockito.anyString()));
    }

    @Test
    public void testCountSelect() {
        String countString = "0";
        Mockito.when(basePmtctRegisterFragmentModel.countSelect(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(countString);
        Assert.assertEquals(countString, basePmtctRegisterFragmentModel.countSelect(Mockito.anyString(), Mockito.anyString()));
    }

    @Test
    public void testMainSelect() {
        String countString = "mainSelect";
        Mockito.when(basePmtctRegisterFragmentModel.mainSelect(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(countString);
        Assert.assertEquals(countString, basePmtctRegisterFragmentModel.mainSelect(Mockito.anyString(), Mockito.anyString()));
    }

}
