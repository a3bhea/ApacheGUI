package net.ovoice.apachegui;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ashraf
 */
@Configuration
public class ApacheGUIDataSourceMysql {

    private static final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/apachegui";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "mypassword";
    private static final int CONN_POOL_SIZE = 5;

    private DataSource bds = new DataSource();

    @Bean
    public DataSource DataSource() {
        //Set database driver name
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        //Set database url
        bds.setUrl(DB_URL);
        //Set database user
        bds.setUsername(DB_USER);
        //Set database password
        bds.setPassword(DB_PASSWORD);
        //Set the connection pool size
        bds.setInitialSize(CONN_POOL_SIZE);
        //Set
        return bds;
    }

    private static class DataSourceHolder {
        private static final DataSource INSTANCE = new DataSource();
    }

    public static DataSource getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    public DataSource getBds() {
        return bds;
    }

    public void setBds(DataSource bds) {
        this.bds = bds;
    }
}