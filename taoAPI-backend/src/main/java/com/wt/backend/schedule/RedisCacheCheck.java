package com.wt.backend.schedule;

import com.wt.backend.service.api.InterfaceInfoService;
import com.wt.constant.RedisConstant;
import com.wt.mysqlmodel.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xcx
 * @date
 */
@Component
@Slf4j
public class RedisCacheCheck {

    private static final DefaultRedisScript<Object> CHECK_SCRIPT;

    static {
        CHECK_SCRIPT = new DefaultRedisScript<>();
        CHECK_SCRIPT.setLocation(new ClassPathResource("check.lua"));
        CHECK_SCRIPT.setResultType(Object.class);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Autowired
    private InterfaceInfoService interfaceInfoService;

    /**
     * 1天
     */
    final long time = 3 * 24 * 60 * 60 * 1000;

    @Scheduled(fixedDelay = time)
    public void doHotStoreCheck() {
        log.info("start check");

        List<InterfaceInfo> list = interfaceInfoService.query().select("url", "id").eq("status", 1).list();

        List<String> fileds = list.stream().map(interfaceInfo -> {
            return interfaceInfo.getUrl();
        }).collect(Collectors.toList());

        String[] values = list.stream().map(interfaceInfo -> {
            return String.valueOf(interfaceInfo.getId().longValue());
        }).toArray(String[]::new);

        Object execute = stringRedisTemplate.execute(
                CHECK_SCRIPT,
                fileds,
                values
        );
        log.info(execute.toString());

    }
}
