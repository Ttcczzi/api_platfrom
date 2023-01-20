package com.wt.taoapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.wt.request.CommonRequest;
import com.wt.request.RestfulRequest;
import com.wt.response.CommonResult;
import com.wt.taoapiclientsdk.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xcx
 * @date
 * 调用第三方接口的API
 */
@Slf4j
public class TaoAPIClient {

    private String accessKey;
    private String secretKey;
    private Long userId;

    public TaoAPIClient(String accessKey, String secretKey, Long userId) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.userId = userId;
    }

    public CommonResult get(CommonRequest request){
        Map requestParams = request.getRequestParams();
        String url = request.getUrl();
        HttpResponse response = HttpRequest.get(url)
                .charset(StandardCharsets.UTF_8)
                .form(request.getRequestParams())
                .addHeaders(getHeaderMap(requestParams.toString()))
                .addHeaders(request.getRequestHeaders())
                .execute();
        return getResult(response);
    }

    public CommonResult post(CommonRequest request){
        Map requestParams = request.getRequestParams();
        String url = request.getUrl();
        HttpResponse response = HttpRequest.post(url)
                .charset(StandardCharsets.UTF_8)
                .form(request.getRequestParams())
                .addHeaders(getHeaderMap(requestParams.toString()))
                .addHeaders(request.getRequestHeaders())
                .execute();
        return getResult(response);
    }

    public CommonResult restfulPost(RestfulRequest request){
        String requestParams = request.getRequestParams();
        String url = request.getUrl();
        HttpResponse httpResponse = HttpRequest.post( url)
                .charset(StandardCharsets.UTF_8)
                .addHeaders(getHeaderMap(requestParams))
                .addHeaders(request.getRequestHeaders())
                .body(requestParams)
                .execute();
        return getResult(httpResponse);
    }

    public CommonResult getResult(HttpResponse response){
        if(response.getStatus() != HttpStatus.HTTP_OK){
            String body = response.body();
            CommonResult result = new CommonResult();
            result.setMessage(body);
            return result;
        }
        String body = response.body();
        CommonResult result = JSONUtil.toBean(body, CommonResult.class);
        return result;
    }




    private Map<String,String> getHeaderMap(String body){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", String.valueOf(userId));
        hashMap.put("accessKey", accessKey);
        String nonce = RandomUtil.randomNumbers(4) + System.currentTimeMillis() % 1000000;
        hashMap.put("nonce", nonce);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        hashMap.put("timestamp", timestamp);
        hashMap.put("body", body);
        hashMap.put("sign", SignUtil.getSign(body + nonce + timestamp, secretKey));
        return hashMap;
    }
}
