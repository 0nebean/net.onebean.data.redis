package net.onebean.component.redis;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisConfig {
  
	private static final Logger logger = (Logger) LoggerFactory.getLogger(RedisConfig.class);
      
    @Bean(name = "getRedisConfig")
    @ConfigurationProperties(prefix="spring.redis")
    public JedisPoolConfig getRedisConfig(){
        return new JedisPoolConfig();
    }  
      
    @Bean(name = "getConnectionFactory")
    public JedisConnectionFactory getConnectionFactory(@Qualifier("getRedisConfig")JedisPoolConfig getRedisConfig){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(getRedisConfig);
        logger.info("JedisConnectionFactory bean init success.");  
        return factory;  
    }  
      
      
    @Bean(name = "getRedisTemplate")
    public RedisTemplate<?, ?> getRedisTemplate(@Qualifier("getConnectionFactory")JedisConnectionFactory getConnectionFactory){
        return new StringRedisTemplate(getConnectionFactory);
    }  
} 