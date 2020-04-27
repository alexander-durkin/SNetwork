package db;

import org.postgresql.ds.PGPoolingDataSource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class PgDataSourceProvider implements Provider<DataSource> {

    private final PgConfig pgConfig;

    @Inject
    public PgDataSourceProvider(PgConfig pgConfig) {
        this.pgConfig = pgConfig;
    }

    @Override
    public DataSource get() {
        final PGPoolingDataSource ds = new PGPoolingDataSource();

        ds.setServerName(pgConfig.getHost());
        ds.setPortNumber(pgConfig.getPort());
        ds.setDatabaseName(pgConfig.getDbName());
        ds.setUser(pgConfig.getUsername());
        ds.setPassword(pgConfig.getPassword());
        ds.setMaxConnections(4);
        ds.setInitialConnections(2);

        Instant instant = Instant.parse("2016-02-29T00:00:00.00Z"); //throws DateTimeParseException
        //System.out.println(instant);
        //System.out.println(Timestamp.from(instant));
        //System.out.println(LocalDateTime.now().minus(30, ChronoUnit.YEARS).toInstant(ZoneOffset.UTC));

        return ds;
    }
}
