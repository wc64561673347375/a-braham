package com.coffeewx.configurer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis基础配置
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger( RedisConfig.class );

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public JedisPool redisPoolFactory() {
        logger.info( "JedisPool注入成功！！" );
        JedisPool jedisPool = new JedisPool( getJedisPoolConfig(), host, port, timeout, password );
        return jedisPool;
    }

    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle( maxIdle );
        jedisPoolConfig.setMaxWaitMillis( maxWaitMillis );
        return jedisPoolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName( host );
        jedisConnectionFactory.setPort( port );
        jedisConnectionFactory.setPassword( password );
        jedisConnectionFactory.setTimeout( timeout );
        jedisConnectionFactory.setPoolConfig( getJedisPoolConfig() );
        jedisConnectionFactory.setUsePool( true );
        logger.info( "JedisConnectionFactory注入成功！！" );
        return jedisConnectionFactory;
    }

    @Bean
    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        logger.info( "cacheManager注入成功！！" );
        RedisCacheManager redisCacheManager = new RedisCacheManager( redisTemplate );
        // 设置缓存过期时间 单位：s
        redisCacheManager.setDefaultExpiration( 1800 );
        return redisCacheManager;
    }

    @Bean
    public RedisTemplate <String, Object> getRedisTemplate() {
        logger.info( "redisTemplatet注入成功！！" );
        RedisTemplate <String, Object> redisTemplate = new RedisTemplate <>();
        redisTemplate.setKeySerializer( new StringRedisSerializer() );
        redisTemplate.setHashKeySerializer( new StringRedisSerializer() );
        redisTemplate.setHashValueSerializer( new JdkSerializationRedisSerializer() );
        redisTemplate.setValueSerializer( new JdkSerializationRedisSerializer() );
        redisTemplate.setConnectionFactory( jedisConnectionFactory() );
        return redisTemplate;
    }
}
