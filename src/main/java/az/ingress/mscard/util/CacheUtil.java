package az.ingress.mscard.util;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

import static az.ingress.mscard.model.constants.CacheConstants.ONE;
import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CacheUtil {
    RedissonClient redissonClient;

    public <T> T getBucket(String key){
        RBucket<T> bucket;
        try {
            bucket = redissonClient.getBucket(key);
            return bucket == null ? null : bucket.get();
        } catch (Exception e){
            log.error("ActionLog.getBucket error : cache stopped working");
            return null;
        }
    }

    public <T> void saveToCache(String key, T value, Long expireTime, TemporalUnit temporalUnit){
        RBucket<T> bucket;
        try {
            bucket = redissonClient.getBucket(key);
        } catch (Exception e){
            log.error("ActionLog.saveToCache error : cache stopped working");
            return;
        }

        bucket.set(value);
        if(expireTime == null) expireTime = ONE;
        if(temporalUnit == null) temporalUnit = DAYS;
        bucket.expire(Duration.of(expireTime, temporalUnit));
    }

    public boolean deleteCache(String key){
        RBucket<Object> bucket;
        try {
            bucket = redissonClient.getBucket(key);
        } catch (Exception e){
            log.error("ActionLog.deleteCache error : cache stopped working");
            return false;
        }

        if(bucket != null) return bucket.delete();
        return false;
    }
}
