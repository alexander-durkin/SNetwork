package db;

import javax.inject.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PgConfigProvider implements Provider<PgConfig> {
    @Override
    public PgConfig get() {

        final Properties properties = new Properties();
        try (InputStream stream =
                     getClass()
                             .getClassLoader()
                             .getResourceAsStream("cfg/postgres.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return new PgConfig() {
            @Override
            public String getHost() {
                return properties.getProperty("postgres.host");
            }

            @Override
            public int getPort() {
                return Integer.parseInt(properties.getProperty("postgres.port")); //словить exception
            }

            @Override
            public String getDbName() {
                return properties.getProperty("postgres.dbname");
            }

            @Override
            public String getUsername() {
                return properties.getProperty("postgres.username");
            }

            @Override
            public String getPassword() {
                return properties.getProperty("postgres.password");
            }
        };
    }
}
