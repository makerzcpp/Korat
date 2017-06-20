package com.korat.common.service;

import com.korat.common.utils.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * redis服务业务层
 *
 * @author solar
 * @date 2017/6/20
 */
@Service
public class RedisService {
    @Autowired
    private ShardedJedisPool shardedJedisPool;

    private <T> T execute(Function<T, ShardedJedis> function) {
        ShardedJedis shardedJedis=null;
        try{
            shardedJedis=shardedJedisPool.getResource();
            return function.callBack(shardedJedis);
        }finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }
    /**
     * 执行set操作
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
       return this.execute(shardedJedis -> shardedJedis.set(key, value)) ;
    }

    /**
     * 执行get方法
     * @param key
     * @return
     */
    public String get(String key) {
        return this.execute(shardedJedis -> shardedJedis.get(key));
    }

    /**
     * 删除指定键对应的值
     * @param key
     * @return
     */
    public Long del(String key) {
        return this.execute(shardedJedis -> shardedJedis.del(key));
    }

    /**
     * 设置存活时间
     * @param key
     * @param second
     * @return
     */
    public Long expirt(String key, Integer second) {
        return this.execute(shardedJedis -> shardedJedis.expire(key, second));
    }

    /**
     * 执行set方法并设置存活时间
     * @param key
     * @param value
     * @param second
     * @return
     */
    public String set(String key, String value, Integer second) {
        return this.execute(shardedJedis -> {
            String str = shardedJedis.set(key, value);
            shardedJedis.expire(key, second);
            return str;
        });
    }

}
