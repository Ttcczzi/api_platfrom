package com.wt.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author xcx
 * @date
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestfulRequest {
    String url;
    Map requestHeaders;
    /**
     * json格式，调用者需要将对象转换为json
     */
    String requestParams;
}
