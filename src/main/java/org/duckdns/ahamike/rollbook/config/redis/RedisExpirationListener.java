package org.duckdns.ahamike.rollbook.config.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RedisExpirationListener implements MessageListener {
    public RedisExpirationListener(RedisMessageListenerContainer listenerContainer) {
        listenerContainer.addMessageListener(this, new ChannelTopic("__keyevent@0__:expired"));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("Key of Redis expired: " + expiredKey);
    }
}
