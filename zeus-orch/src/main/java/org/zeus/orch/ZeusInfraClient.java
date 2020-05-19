package org.zeus.orch;

public interface ZeusInfraClient {
    // TODO: change return type appropriately.
    void setupVcn();

    void setupK8s();

    void setupK8sNamespace();

    void setupNodepool();

    void setupPresto();

    void tearDownVcn();

    void tearDownK8s();

    void tearDownNamespace();

    void tearDownNodepool();

    void tearDownPresto();
}
