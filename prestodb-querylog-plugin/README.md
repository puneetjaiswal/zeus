# Presto-querylog-plugin
Query log plugin for Presto used to capture all query params.

## Build
Make sure to bump up the version so that it doesn't override current one.

Please run:

`mvn clean install`.

## Setup 
Create `$PRESTO_HOME/plugins/querylog-event-listener` dir and 
place the querylog plugin jar (with dependencies) inside.

Now place `event-listener.properties` file under `$PRESTO_HOME/etc/` dir.
Add the following content in the `event-listener.properties`
```
event-listerner.name=querylog-event-listener

```
