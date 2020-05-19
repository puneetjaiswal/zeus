package org.zeus.api.resource;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.zeus.dal.DbException;
import org.zeus.dal.JdbcConnectionManager;
import org.zeus.dal.entity.AbstractEntity;
import org.zeus.dal.entity.EntityType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Path("entity")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntityResource extends AbstractResource {
    @Inject
    private JdbcConnectionManager jdbcConnectionManager;

    @GET
    public List<EntityType> getAllEntityTypes() {
        return Arrays.asList(EntityType.values());
    }

    @POST
    public Response updateEntity(@QueryParam("entityType") String entityTypeStr, String jsonPayload) {
        if (Strings.isNullOrEmpty(entityTypeStr)) {
            throw new WebApplicationException("EntryType can not be empty");
        }
        EntityType entityType = EntityType.valueOf(entityTypeStr);
        Optional<String> updatedId;
        //todo: validations and checks
        try {
            updatedId = jdbcConnectionManager.saveEntity(entityType, jsonPayload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException(e);
        }
        if (!updatedId.isPresent()) {
            throw new WebApplicationException("Unable to update the entity: " + jsonPayload);
        }
        return Response.ok(updatedId).build();
    }

    @GET
    @Path("/{entityType}")
    public Response getAllEntitiesForType(@PathParam("entityType") String entityTypeStr) {
        EntityType entityType = EntityType.valueOf(entityTypeStr);
        List<? extends AbstractEntity> entities = null;
        try {
            entities = jdbcConnectionManager.fetchEntities(entityType, new HashMap<>());
        } catch (DbException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException(e);
        }
        return Response.ok(entities).build();
    }

    @POST
    @Path("/{entityType}")
    public Response getAllEntitiesByIdsForType(@PathParam("entityType") String entityTypeStr, List<String> idList) {
        EntityType entityType = EntityType.valueOf(entityTypeStr);
        List<? extends AbstractEntity> entities;
        try {
            entities = jdbcConnectionManager.fetchEntities(entityType, idList);
        } catch (DbException e) {
            log.error(e.getMessage(), e);
            throw new WebApplicationException(e);
        }
        return Response.ok(entities).build();
    }

    @DELETE
    @Path("/{entityType}/{id}")
    public Response deleteEntityForType(@PathParam("entityType") String entityTypeStr, @PathParam("id") String id) {
        EntityType entityType = EntityType.valueOf(entityTypeStr);
        boolean success = jdbcConnectionManager.deleteEntity(entityType, id);
        return Response.ok(success).build();
    }
}
