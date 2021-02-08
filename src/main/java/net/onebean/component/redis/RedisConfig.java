package net.onebean.component.redis;

import ch.qos.logback.classic.Logger;
import net.onebean.core.form.Parse;
import net.onebean.util.PropUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisConfig {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(RedisConfig.class);
    private static final String HOST_NAME_KEY = "spring.redis.hostName";
    private static final String PORT_KEY = "spring.redis.port";
    private static final String PASSWORD_KEY = "spring.redis.password";
    private static final String DB_SELECT_KEY = "spring.redis.database";

    @Bean(name = "lettuceConnectionFactory")
    LettuceConnectionFactory initLettuceConnectionFactory() {
        String hostName = PropUtil.getInstance().getConfig(HOST_NAME_KEY, PropUtil.PUBLIC_CONF_REDIS);
        int port = Parse.toInt(PropUtil.getInstance().getConfig(PORT_KEY, PropUtil.PUBLIC_CONF_REDIS));
        String password = PropUtil.getInstance().getConfig(PASSWORD_KEY, PropUtil.PUBLIC_CONF_REDIS);
        int database = Parse.toInt(PropUtil.getInstance().getConfig(DB_SELECT_KEY, PropUtil.PUBLIC_CONF_REDIS));
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(hostName);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        redisStandaloneConfiguration.setDatabase(database);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        logger.info("LettuceConnectionFactory bean init success , redis host = {} , redis port ={}", lettuceConnectionFactory.getHostName(), lettuceConnectionFactory.getPort());
        return lettuceConnectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> stringRedisTemplate(@Qualifier("lettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }
} 