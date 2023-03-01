package com.wt.project.config;

import com.wt.project.requestLimit.IpKeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */
@Configuration
public class GateConfig {
    @Bean(name = "ipKeyResolver")
    public KeyResolver keyResolver() {
        return new IpKeyResolver();
    }

    @Bean(name = "redisRateLimiter")
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(1,2);
    }
}
