package org.zeus.api;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.setup.Environment;
import org.zeus.api.config.ZeusConfiguration;
import org.zeus.baseapp.AppModule;
import org.zeus.db.JdbcConnectionManager;

public class ZeusModule extends AppModule<ZeusConfiguration, Environment> {
    private ZeusConfiguration config;

    public ZeusModule(ZeusConfiguration config, Environment env) {
        super(config, env);
        this.config = config;
    }

    @Provides
    @Singleton
    public JdbcConnectionManager getJdbcConnectionManager() {
        return new JdbcConnectionManager(config.getDataStore());
    }
}
