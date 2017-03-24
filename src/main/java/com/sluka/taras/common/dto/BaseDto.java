package com.sluka.taras.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {

    private Date createdDate;

    private Date modifiedDate;
}
