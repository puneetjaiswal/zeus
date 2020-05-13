package org.zeus.db;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.DBException;
import org.javalite.common.Util;
import org.zeus.api.config.DataStoreConfiguration;
import org.zeus.db.entity.AbstractEntity;
import org.zeus.db.entity.Artifact;
import org.zeus.db.entity.EntityType;
import org.zeus.db.entity.Tenant;
import org.zeus.db.record.ArtifactRecord;
import org.zeus.db.record.TenantRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public final class JdbcConnectionManager {
    public static final Gson GSON = new Gson();

    private final DataStoreConfiguration configuration;

    public JdbcConnectionManager(DataStoreConfiguration configuration) {
        this.configuration = configuration;
    }

    public void open() {
        Base.open(
                configuration.getDriver(),
                configuration.getJdbcUrl(),
                configuration.getUser(),
                configuration.getPassword());
        log.debug("Connection opened");
    }

    public void close() {
        Base.close();
        log.debug("Connection closed");
    }

    /**
     * insert of update
     *
     * @param entityType
     * @param jsonPayload
     * @return nil if entity could not be saved.
     * @throws DbException
     */
    public Optional<String> saveEntity(EntityType entityType, String jsonPayload) throws DbException {
        open();
        boolean success;
        String entityId;
        switch (entityType) {
            case ARTIFACT:
                Artifact a = GSON.fromJson(jsonPayload, Artifact.class);
                if (a.getId() == null) {
                    a.generateId();
                    success = ArtifactRecord.toModel(a).insert();
                } else {
                    success = ArtifactRecord.toModel(a).save();
                }
                entityId = a.getId();
                break;
            case TENANT:
                Tenant t = GSON.fromJson(jsonPayload, Tenant.class);
                if (t.getId() == null) {
                    t.generateId();
                    success = TenantRecord.toModel(t).insert();
                } else {
                    success = TenantRecord.toModel(t).save();
                }
                entityId = t.getId();
                break;
            default:
                throw new IllegalArgumentException("Unknown entity type: " + jsonPayload);
        }
        close();
        if (!success) {
            throw new DBException("Could not persist: " + jsonPayload);
        }
        return Optional.of(entityId);
    }

    public boolean deleteEntity(EntityType entityType, String id) throws DBException {
        open();
        boolean success;
        switch (entityType) {
            case ARTIFACT:
                success = ArtifactRecord.findById(id).delete();
                break;
            case TENANT:
                success = TenantRecord.findById(id).delete();
                break;
            default:
                success = false;
        }
        close();
        return success;
    }

    public List<? extends AbstractEntity> fetchEntities(EntityType entityType, Map<String, Object> filterMap)
            throws DbException {
        open();
        List<AbstractEntity> entities = new ArrayList<>();
        switch (entityType) {
            case ARTIFACT:
                List<ArtifactRecord> arList = ArtifactRecord.findByFilter(filterMap);
                for (ArtifactRecord ar : arList) {
                    entities.add(ar.toEntity());
                }
                break;
            case TENANT:
                List<TenantRecord> tList = TenantRecord.findByFilter(filterMap);
                for (TenantRecord e : tList) {
                    entities.add(e.toEntity());
                }
                break;
            default:
        }
        close();
        return entities;
    }

    public List<? extends AbstractEntity> fetchEntities(EntityType entityType, List<String> idsfilter)
            throws DbException {
        open();
        List<AbstractEntity> entities = new ArrayList<>();
        switch (entityType) {
            case ARTIFACT:
                List<ArtifactRecord> arList = ArtifactRecord.where("id in ('" +
                        Util.join(idsfilter, "','") + ")'");
                for (ArtifactRecord ar : arList) {
                    entities.add(ar.toEntity());
                }
                break;
            case TENANT:
                List<TenantRecord> tList = TenantRecord.where("id in ('" +
                        Util.join(idsfilter, "','") + ")'");
                for (TenantRecord e : tList) {
                    entities.add(e.toEntity());
                }
                break;
            default:
        }
        close();
        return entities;
    }

    public static FilterConditionArgs buildAndFilterFromMap(Map<String, Object> filterMap) {
        StringBuilder condition = new StringBuilder();
        Object[] args = new Object[filterMap.size()];
        int count = 0;
        for (Map.Entry<String, Object> keyVal : filterMap.entrySet()) {
            if (count != 0) {
                condition.append(" and ");
            }
            condition.append(keyVal.getKey()).append(" = ?");
            args[count++] = keyVal.getValue();
        }
        return new FilterConditionArgs(condition.toString(), args);
    }

    @Data
    @AllArgsConstructor
    public static class FilterConditionArgs {
        private String filter;
        private Object[] args;
    }
}
