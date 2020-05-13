package org.zeus.db.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.db.DbException;
import org.zeus.db.JdbcConnectionManager;
import org.zeus.db.entity.Org;
import org.zeus.db.entity.Standard;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("standard")
@IdName("id")
@Cached
public class StandardRecord extends Model {

    public Standard toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Standard.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static StandardRecord toModel(Standard a) throws DbException {
        try {
            StandardRecord ar = new StandardRecord();
            ar.set("id", a.getId()).set("name", a.getName()).set("description", a.getDescription())
                    .set("level", a.getLevel()).set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<StandardRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return StandardRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}
