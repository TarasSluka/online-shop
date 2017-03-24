package com.sluka.taras.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity {
    @ManyToOne
    private Product product;
    private Integer quantity;


}
