package com.wt.clientsdk;

import com.wt.clientsdk.client.TaoAPIClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xcx
 * @date
 */
@Data
//可以读取配置
@ConfigurationProperties("taoapi.client")
@Configuration
@ComponentScan
public class TaoAPIClientConfig {
    private String accessKey;
    private String secretKey;
    private Long uesrId;
    @Bean
    public TaoAPIClient taoAPIClient(){
        return new TaoAPIClient(accessKey, secretKey,uesrId);
    }
}
