package com.library.spring.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.host}")
    private String mongodbHost;

    @Value("${mongodb.port}")
    private int mongodbPort;

    @Value("${mongodb.database}")
    private String mongodbDatabase;

    @Value("${mongodb.user}")
    private String mongodbUser;

    @Value("${mongodb.password}")
    private String mongodbPassword;

    @Override
    protected String getDatabaseName() {
        return mongodbDatabase;
    }

    @Override
    public MongoClient mongoClient() {
        
        MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();
//        optionsBuilder.maxWaitTime(MAX_WAIT_TIME);
//        optionsBuilder.connectTimeout(CONNECTION_TIMEOUT);
//        optionsBuilder.socketTimeout(SOCKET_TIMEOUT);

        MongoClientOptions options = optionsBuilder.build();

        ServerAddress serverAddress = new ServerAddress(mongodbHost, mongodbPort);
        MongoCredential mongoCredential = MongoCredential.createMongoCRCredential(mongodbUser, mongodbDatabase, mongodbPassword.toCharArray());

        return new MongoClient(serverAddress, mongoCredential, options);
    }
}
