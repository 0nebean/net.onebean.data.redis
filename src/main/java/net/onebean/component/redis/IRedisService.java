package net.onebean.component.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis封装
 * @author 0neBean
 * 2017年9月4日 下午3:02:19
 */
public interface IRedisService {

    /**
     * 缓存值并设定时效
     * @param key 键
     * @param value 值
     * @param time 失效时间
     * @return boolean
     */
    boolean set(String key, String value,long time);  
    /**
     * 缓存值
     * @param key 键
     * @param value 值
     * @return boolean
     */
    boolean set(String key, String value);
    /**
     * 删除值
     * @param key 键
     * @return boolean
     */
    boolean del(String key);
    /**
     * 根据key获取缓存中的值  
     * @param key 键
     * @return String
     */
    String get(String key);  
    /**
     * 缓存list
     * @param key 键
     * @param list list
     * @param <T> 泛型类型
     * @return boolean
     */
    <T> boolean setList(String key ,List<T> list);  
    /**
     * 根据key获取缓存中的list
     * @param key 键
     * @param clz class
     * @param <T> 泛型类型
     * @return 结果
     */
    <T> List<T> getList(String key,Class<T> clz);  
      /**
       * 在key对应list的头部添加字符串元素
       * @param key 键
       * @param obj 对象
       * @return long
       */
    long lpush(String key,Object obj);  
      /**
       * 在key对应list的尾部添加字符串元素
       * @param key 键
       * @param obj 对象
       * @return long
       */
    long rpush(String key,Object obj);  
      /**
       * 在key对应list上移除并返回list的最后一个元素
       * @param key 键
       * @return String
       */
    String lpop(String key);
    /**
     * 哈希添加
     * @param key 键 键
     * @param hashKey 哈希键
     * @param value 值 值
     */
    void hSet(String key, Object hashKey, Object value);
    /**
     * 哈希添加
     * @param key 键 键
     * @param values 值
     */
    void hSetAll(String key, Map<?,?>values);
    /**
     * 获取哈希
     * @param key 键 主键
     * @param hashKey 哈希键
     * @return obj
     */
    Object hGet(String key, Object hashKey);
    /**
     * 获取所有哈希键
     * @param key 键 主键
     * @return keys
     */
    Set<Object> hKeys(String key);
    /**
     * 删除哈希键
     * @param masterKey 主键
     * @param hashKey 哈希键
     */
    void hDel(String masterKey, Object... hashKey);
}