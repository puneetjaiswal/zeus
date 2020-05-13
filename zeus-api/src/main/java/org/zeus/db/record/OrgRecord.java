package org.zeus.db.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.db.DbException;
import org.zeus.db.JdbcConnectionManager;
import org.zeus.db.entity.Org;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("org")
@IdName("id")
@Cached
public class OrgRecord extends Model {

    public Org toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Org.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static OrgRecord toModel(Org a) throws DbException {
        try {
            OrgRecord ar = new OrgRecord();
            ar.set("id", a.getId()).set("name", a.getName()).set("address", a.getAddress())
                    .set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<OrgRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return OrgRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}
