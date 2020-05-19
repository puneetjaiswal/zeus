package org.zeus.api.config;

import lombok.Data;
import org.zeus.baseapp.AppConfiguration;
import org.zeus.dal.DataStoreConfiguration;

@Data
public class ZeusConfiguration extends AppConfiguration {
    private DataStoreConfiguration dataStore;
}
