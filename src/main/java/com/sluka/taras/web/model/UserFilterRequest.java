package com.sluka.taras.web.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserFilterRequest {
    Integer page;
    Integer size;
    String sortParam;
    String sortType;
    String global;
    Long id;
    String firstName;
    String lastName;
    String userName;
    String email;
    Date createdDate;
    String ban;
    String sex;
}
