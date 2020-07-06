package net.onebean.component.redis;

import ch.qos.logback.classic.Logger;
import net.onebean.core.form.Parse;
import net.onebean.util.PropUtil;
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
	private static final String HOST_NAME_KEY = "spring.redis.hostName";
	private static final String PORT_KEY = "spring.redis.port";
	private static final String PASSWORD_KEY = "spring.redis.password";
	private static final String DB_SELECT_KEY = "spring.redis.database";

    @Bean(name = "getRedisConfig")
    @ConfigurationProperties(prefix="spring.redis")
    public JedisPoolConfig getRedisConfig(){
        return new JedisPoolConfig();
    }  
      
    @Bean(name = "getConnectionFactory")
    public JedisConnectionFactory getConnectionFactory(@Qualifier("getRedisConfig")JedisPoolConfig getRedisConfig){
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(getRedisConfig);
        factory.setHostName(PropUtil.getInstance().getConfig(HOST_NAME_KEY,PropUtil.PUBLIC_CONF_REDIS));
        factory.setPort(Parse.toInt(PropUtil.getInstance().getConfig(PORT_KEY,PropUtil.PUBLIC_CONF_REDIS)));
        factory.setPassword(PropUtil.getInstance().getConfig(PASSWORD_KEY,PropUtil.PUBLIC_CONF_REDIS));
        factory.setDatabase(Parse.toInt(PropUtil.getInstance().getConfig(DB_SELECT_KEY,PropUtil.PUBLIC_CONF_REDIS)));
        logger.info("JedisConnectionFactory bean init success.");
        return factory;  
    }  
      
      
    @Bean(name = "getRedisTemplate")
    public RedisTemplate<?, ?> getRedisTemplate(@Qualifier("getConnectionFactory")JedisConnectionFactory getConnectionFactory){
        return new StringRedisTemplate(getConnectionFactory);
    }  
} 