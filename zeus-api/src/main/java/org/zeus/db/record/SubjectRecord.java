package org.zeus.db.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.db.DbException;
import org.zeus.db.JdbcConnectionManager;
import org.zeus.db.entity.Subject;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("subject")
@IdName("id")
@Cached
public class SubjectRecord extends Model {

    public Subject toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Subject.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static SubjectRecord toModel(Subject a) throws DbException {
        try {
            SubjectRecord ar = new SubjectRecord();
            ar.set("id", a.getId()).set("name", a.getName())
                    .set("description", a.getDescription())
                    .set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<SubjectRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return SubjectRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}
