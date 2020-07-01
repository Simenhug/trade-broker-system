package com.simen.tradesystem.core;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
@PropertySource("app.properties")
public class DataConfig {
    @Autowired
    Environment env;

    @Bean
    public DataSource datasource() {
        BasicDataSource ds = new BasicDataSource();

        //Driver class name
        ds.setDriverClassName(env.getProperty("tradesystem.db.driver"));
        //Driver url
        ds.setUrl(env.getProperty("tradesystem.db.url"));
        //username
        ds.setUsername(env.getProperty("tradesystem.db.username"));
        //password
        ds.setPassword(env.getProperty("tradesystem.db.password"));
        return ds;
    }
}
