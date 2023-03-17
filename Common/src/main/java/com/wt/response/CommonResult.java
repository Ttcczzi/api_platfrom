package com.wt.response;

import com.wt.constant.CodeStatus;
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
    int code;

    public Object data;
    int isImg;
    String imgFormat;

    String message;

    CommonResult(int code, Object data){
        this.code = code;
        this.data = data;
    }

    CommonResult(int code, Object data, int isImg, String imgFormat){
        this.code = code;
        this.data = data;
        this.isImg = isImg;
        this.imgFormat = imgFormat;
    }

    public static CommonResult success(Object data){
        return new CommonResult(CodeStatus.SUCCESS,data);
    }

    public static CommonResult success(Object data, int isImg, String imgFormat){
        return new CommonResult(CodeStatus.SUCCESS, data, isImg, imgFormat);
    }
}
