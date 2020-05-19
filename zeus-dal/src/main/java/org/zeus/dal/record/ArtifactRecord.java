package org.zeus.dal.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.dal.DbException;
import org.zeus.dal.JdbcConnectionManager;
import org.zeus.dal.entity.Artifact;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("artifact")
@IdName("id")
@Cached
public class ArtifactRecord extends Model {

    public Artifact toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Artifact.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static ArtifactRecord toModel(Artifact a) throws DbException {
        try {
            ArtifactRecord ar = new ArtifactRecord();
            ar.set("id", a.getId()).set("name", a.getName()).set("path", a.getPath())
                    .set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<ArtifactRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return ArtifactRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}