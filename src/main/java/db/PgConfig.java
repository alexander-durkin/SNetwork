package db;

public interface PgConfig {

    String getHost();

    int getPort();

    String getDbName();

    String getUsername();

    String getPassword();
}
