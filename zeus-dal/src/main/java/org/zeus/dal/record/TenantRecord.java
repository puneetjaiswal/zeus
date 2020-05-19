package org.zeus.dal.record;

import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Cached;
import org.javalite.activejdbc.annotations.IdName;
import org.javalite.activejdbc.annotations.Table;
import org.zeus.dal.DbException;
import org.zeus.dal.JdbcConnectionManager;
import org.zeus.dal.entity.Tenant;

import java.util.List;
import java.util.Map;

@Slf4j
@Table("tenant")
@IdName("id")
@Cached
public class TenantRecord extends Model {

    public Tenant toEntity() throws DbException {
        try {
            return JdbcConnectionManager.GSON.fromJson(this.getString("json_entity"), Tenant.class);
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static TenantRecord toModel(Tenant a) throws DbException {
        try {
            TenantRecord ar = new TenantRecord();
            ar.set("id", a.getId()).set("name", a.getName())
                    .set("json_entity", JdbcConnectionManager.GSON.toJson(a));
            return ar;
        } catch (Exception e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public static List<TenantRecord> findByFilter(Map<String, Object> filterMap) {
        if (filterMap == null || filterMap.isEmpty()) {
            return findAll();
        }
        JdbcConnectionManager.FilterConditionArgs filterConditionArgs = JdbcConnectionManager.buildAndFilterFromMap(filterMap);
        return TenantRecord.where(filterConditionArgs.getFilter(), filterConditionArgs.getArgs());
    }
}
