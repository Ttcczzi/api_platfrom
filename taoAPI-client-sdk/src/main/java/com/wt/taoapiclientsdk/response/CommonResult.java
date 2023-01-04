package com.wt.taoapiclientsdk.response;

import com.wt.taoapiclientsdk.constant.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xcx
 * @date
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {
    //接口调用是否成功
    int code;

    public Object data;

    public static CommonResult success(Object data){
        return new CommonResult(CodeStatus.SUCCESS,data);
    }
}
