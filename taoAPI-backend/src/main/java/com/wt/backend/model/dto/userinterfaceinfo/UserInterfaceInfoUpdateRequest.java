package com.wt.backend.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @author xcx
 * @date
 */
@Data
public class UserInterfaceInfoUpdateRequest {
    /**
     *
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private int totalNum;

    /**
     * 剩余调用次数
     */
    private int leftNum;

    /**
     * 调用状态
     */
    private Integer status;

}
