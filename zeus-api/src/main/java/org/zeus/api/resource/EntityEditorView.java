package org.zeus.api.resource;

import io.dropwizard.views.View;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.nio.charset.Charset;

@Slf4j
@Path("entity-view")
public class EntityEditorView {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public EntityView entityUi() {
        return new EntityView("/template/entity-view.ftl");
    }

    public static class EntityView extends View {
        protected EntityView(String templateName) {
            super(templateName, Charset.defaultCharset());
        }
    }
}
