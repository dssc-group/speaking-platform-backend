package com.bupt.config;

import com.bupt.service.impl.AssignmentStatusListener;
import com.bupt.service.impl.TaskCompletionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private AssignmentStatusListener assignmentStatusListener;
    @Autowired
    private TaskCompletionListener taskCompletionListener;
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
    //redis注册监听
    @Bean
    public RedisMessageListenerContainer messageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 注册消息监听器
        container.addMessageListener(new MessageListenerAdapter(assignmentStatusListener), new PatternTopic("UploadChannel"));
        container.addMessageListener(new MessageListenerAdapter(taskCompletionListener), new PatternTopic("completeTask"));
        return container;
    }

}
