package com.wt.taoapiclientsdk.request;

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
public class CommonRequest {
    String url;
    Map requestHeaders;
    Map requestParams;
}
