package com.wt.mysqlmodel.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    Long id;
}
