package com.sluka.taras.common.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Description extends BaseEntity {

    @NotNull
    private String type;
    @NotNull
    private String value;


}
