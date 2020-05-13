package org.zeus.api.config;

import lombok.Data;
import org.zeus.baseapp.AppConfiguration;

@Data
public class ZeusConfiguration extends AppConfiguration {
    private DataStoreConfiguration dataStore;
}
