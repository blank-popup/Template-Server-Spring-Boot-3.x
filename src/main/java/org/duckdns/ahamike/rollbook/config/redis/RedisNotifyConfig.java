package org.duckdns.ahamike.rollbook.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisNotifyConfig {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void enableKeyspaceNotifications() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        try {
            RedisServerCommands commands = connection.serverCommands();
            String current = commands.getConfig("notify-keyspace-events").get("notify-keyspace-events").toString();
            if (current == null || !current.contains("Ex")) {
                commands.setConfig("notify-keyspace-events", "Ex");
            }
        } finally {
            connection.close();
        }
    }
}
