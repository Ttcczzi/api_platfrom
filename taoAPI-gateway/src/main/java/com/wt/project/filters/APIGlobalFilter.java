package com.wt.project.filters;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.wt.project.TaoApiGatewayApplication;
import com.wt.project.config.RedisConfig;
import com.wt.project.constant.RedisConstant;
import com.wt.project.constants.AuthStatus;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class APIGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Set<String> IP_WHITE_SET = new HashSet<>();

    static {
        IP_WHITE_SET.add("127.0.0.1");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一表示: {}",request.getId());
        log.info("请求路径: {}",request.getPath());
        log.info("请求方法: {}",request.getMethod());
        log.info("请求参数: {}",request.getQueryParams());
        log.info("请求体: {}",request.getBody());
        URI uri = request.getURI();
        log.info("请求RUL: {}",uri);
        String sourceAddress = request.getRemoteAddress().getHostString();
        log.info("请求来源: {}",sourceAddress);

        ServerHttpResponse response = exchange.getResponse();
        //2. 黑白名单
        if(!IP_WHITE_SET.contains(sourceAddress)){
            log.error("黑名单请求 {}", sourceAddress);
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //3. 用户鉴权
        HttpHeaders headers = request.getHeaders();
        long userId = Long.parseLong(headers.getFirst("userId"));
        int status = uesrCheck(headers);
        if(status == AuthStatus.NOAUTH){
            log.error("用户无权限 {}",userId);
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        //4. 发布接口就在redis中添加，下线接口就在redis中删除，redis中以key-url-interfaceId存储为hash结构
        Object obj = stringRedisTemplate
                .opsForHash().get(RedisConstant.CACHE_INTEFACE_URL, uri.toString());
        long interfaceId = 0L;
        if(ObjectUtil.isNull(obj) || (interfaceId = Long.parseLong(obj.toString())) <= 0){
            log.error("接口不存在 {}", uri);
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response.setComplete();
        }
        //5. 取数据库判断用户剩余调用次数是否大于0
        int leftNum = TaoApiGatewayApplication
                .application.userInterfaceInfoService.getLeftNum(userId, interfaceId);
        if(leftNum <= 0){
            log.error("用户调用次数已用完 userId:{} interfaceId:{}",userId, interfaceId);
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response.setComplete();
        }
        //6. 处理响应结果
       return handlerResponse(exchange,chain,interfaceId,userId);
    }

    public Mono<Void> handlerResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceId, Long uesrId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if(statusCode == HttpStatus.OK){
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                //todo 判断接口是否调用成功
                                //1. 获得返回值
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                // 构建日志
                                HttpStatus resStatus = originalResponse.getStatusCode();
                                if(resStatus == HttpStatus.OK){
                                    //2. 调用成功，剩余次数 +1 todo 远程调用backend接口
                                    TaoApiGatewayApplication
                                            .application.userInterfaceInfoService.invokeInterface(uesrId,interfaceId);
                                }
                                log.info("<--- {} {},",data,resStatus);//log.info("<-- {} {}",data, resStatus);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        }catch (Exception e){
            log.error("gateway log exception.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    //用户鉴权
    private int uesrCheck(HttpHeaders headers){
        String userId = headers.getFirst("userId");
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        log.info("{} {} {} {} {} {}", userId,accessKey, body, nonce, timestamp, sign);
        //todo 校验随机数 nonce 防止重放
        String redisKey = RedisConfig.CACHE_NONCE + nonce;
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, nonce);
        stringRedisTemplate.expire(redisKey, 5 , TimeUnit.MINUTES);
        if(!aBoolean){
            return AuthStatus.NOAUTH;
        }
        //todo 校验时间 timestamp 防止重放
        long currentTime = System.currentTimeMillis() / 1000;
        long FIVE_MINUTES = 60 * 5L;
        if(currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES){
            return AuthStatus.NOAUTH;
        }
        //todo 在数据库中查找 secretKey
        String sck = TaoApiGatewayApplication.
                application.userService.getSekByAckAndUname(accessKey, Long.parseLong(userId));
        if(sck.equals("noUser")){
            return AuthStatus.NOAUTH;
        }
        String rSign = getSign(body + nonce + timestamp, sck);
        if(!rSign.equals(sign)){
            return AuthStatus.NOAUTH;
        }
        return AuthStatus.HAVEAUTH;
    }

    public String getSign(String body, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = body + "." + secretKey;
        return md5.digestHex(content);
    }
}

