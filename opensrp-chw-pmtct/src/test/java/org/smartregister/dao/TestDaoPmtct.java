package org.smartregister.dao;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.smartregister.chw.pmtct.dao.PmtctDao;
import org.smartregister.repository.Repository;

@RunWith(MockitoJUnitRunner.class)
public class TestDaoPmtct extends PmtctDao {

    @Mock
    private Repository repository;

    @Mock
    private SQLiteDatabase database;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setRepository(repository);
    }


    @Test
    public void testIsRegisteredForPmtct() {
        Mockito.doReturn(database).when(repository).getReadableDatabase();
        boolean registered = PmtctDao.isRegisteredForPmtct("12345");
        Mockito.verify(database).rawQuery(Mockito.anyString(), Mockito.any());
        Assert.assertFalse(registered);
    }
}

