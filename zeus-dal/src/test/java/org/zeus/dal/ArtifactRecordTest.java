package org.zeus.dal;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.zeus.dal.entity.Artifact;
import org.zeus.dal.record.ArtifactRecord;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Test
public class ArtifactRecordTest {
    JdbcConnectionManager connectionManager;

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
        List<ArtifactRecord> aList = ArtifactRecord.findAll();
        Assert.assertEquals(aList.size(), 0);

        Artifact aIn = new Artifact();
        aIn.generateId();
        aIn.setName("artifact-0");
        aIn.setPath("/x/y/z.txt");
        ArtifactRecord art = ArtifactRecord.toModel(aIn);
        Assert.assertTrue(art.insert());

        ArtifactRecord record = ArtifactRecord.findById(aIn.getId());
        Assert.assertEquals(aIn, record.toEntity());
        Assert.assertEquals(1, ArtifactRecord.findAll().size());

        aIn.setName("updated artifact-0");
        Assert.assertTrue(ArtifactRecord.toModel(aIn).save());
        record = ArtifactRecord.findById(aIn.getId());
        Assert.assertEquals(aIn, record.toEntity());

        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("path", "/x/y/z.txt");
        record = ArtifactRecord.findByFilter(filterMap).get(0);
        Assert.assertEquals(aIn, record.toEntity());

        record.delete();
        Assert.assertEquals(0, ArtifactRecord.findAll().size());
        connectionManager.close();
    }
}
