package org.zeus.dal;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zeus.dal.entity.Tenant;
import org.zeus.dal.record.TenantRecord;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgRecordTest {
    private JdbcConnectionManager connectionManager;

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        File baseDir = new File(System.getProperty("java.io.tmpdir"));
        File tempH2DbDir = new File(baseDir, "h2db-" + System.currentTimeMillis());
        tempH2DbDir.deleteOnExit();
        String jdbcUrl = "jdbc:h2:" + tempH2DbDir.getAbsolutePath();
        DbTestUtil.seedRequiredData(
                new DbTestUtil.TestConfig("", tempH2DbDir.getAbsolutePath()));
        DataStoreConfiguration db = new DataStoreConfiguration(jdbcUrl, "sa", "sa", "org.h2.Driver");
        connectionManager = new JdbcConnectionManager(db);
    }

    @Test
    public void testCRUD() throws Exception {
        connectionManager.open();
        List<TenantRecord> aList = TenantRecord.findAll();
        Assert.assertEquals(aList.size(), 0);

        Tenant aIn = new Tenant();
        aIn.generateId();
        aIn.setName("artifact-0");
        aIn.setId("xyz fake tenant id");
        TenantRecord art = TenantRecord.toModel(aIn);
        Assert.assertTrue(art.insert());

        TenantRecord record = TenantRecord.findById(aIn.getId());
        Assert.assertEquals(aIn, record.toEntity());
        Assert.assertEquals(1, TenantRecord.findAll().size());

        aIn.setName("updated artifact-0");
        Assert.assertTrue(TenantRecord.toModel(aIn).save());
        record = TenantRecord.findById(aIn.getId());
        Assert.assertEquals(aIn, record.toEntity());

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("name", "updated artifact-0");
        record = TenantRecord.findByFilter(filterMap).get(0);
        Assert.assertEquals(aIn, record.toEntity());

        record.delete();
        Assert.assertEquals(0, TenantRecord.findAll().size());
        connectionManager.close();
    }
}
