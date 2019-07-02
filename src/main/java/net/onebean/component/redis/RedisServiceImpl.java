package net.onebean.component.redis;

import net.onebean.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;




@Service  
public class RedisServiceImpl implements IRedisService{  
  
    @Autowired  
    private RedisTemplate<String, ?> redisTemplate;  
      
	@Override
	public boolean set(final String key, final String value, final long time) {
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            if (time > 0) expire(key, time);
            return true;
        });
	}
    
    @Override  
    public boolean set(final String key, final String value) {
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            return true;
        });  
    }
  
    public String get(final String key){
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] value =  connection.get(serializer.serialize(key));
            return serializer.deserialize(value);
        });  
    }
  
    protected boolean expire(final String key, long expire) {  
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);  
    }  
  
    @Override  
    public <T> boolean setList(String key, List<T> list) {  
        String value = JSONUtil.toJson(list);  
        return set(key,value);  
    }  
  
    @Override  
    public <T> List<T> getList(String key,Class<T> clz) {  
        String json = get(key);  
        if(json!=null){  
            List<T> list = JSONUtil.toList(json, clz);
            return list;  
        }  
        return null;  
    }  
  
    @Override  
    public long lpush(final String key, Object obj) {  
        final String value = JSONUtil.toJson(obj);
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));
            return count;
        });  
    }
  
    @Override  
    public long rpush(final String key, Object obj) {  
        final String value = JSONUtil.toJson(obj);
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));
            return count;
        });  
    }
  
    @Override  
    public String lpop(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] res =  connection.lPop(serializer.serialize(key));
            return serializer.deserialize(res);
        });
    }

    @Override
    public void hSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    @Override
    public void hSetAll(String key, Map<?, ?> values) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.putAll(key,values);
    }

    @Override
    public Object hGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    @Override
    public Set<Object> hKeys(String key) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        return hash.keys(key);
    }

    @Override
    public void hDel(String masterKey, Object... hashKey) {
        HashOperations<String, Object, Object> hash = this.redisTemplate.opsForHash();
        hash.delete(masterKey, hashKey);
    }

    @Override
    public boolean del(final String key) {
        return redisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            connection.del(serializer.serialize(key));
            return true;
        });
    }
}
