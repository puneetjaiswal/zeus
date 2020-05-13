package org.zeus.api;

import com.google.inject.Injector;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.zeus.api.config.ZeusConfiguration;
import org.zeus.baseapp.BaseApp;

public class Launcher extends BaseApp<ZeusConfiguration> {

    Launcher(String... basePackages) {
        super(basePackages);
    }

    @Override
    public void initialize(Bootstrap<ZeusConfiguration> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(new ViewBundle<>());
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets", null, "assets"));
    }

    public void applicationAtRun(ZeusConfiguration configuration, Environment environment, Injector injector) {
        environment.jersey().register(MultiPartFeature.class);
    }

    public static void main(String[] args) throws Exception {
        /** base package is scanned for any Resource class to be loaded by default. */
        String basePackage = "org.zeus";
        new Launcher(basePackage).run(args);
    }
}
