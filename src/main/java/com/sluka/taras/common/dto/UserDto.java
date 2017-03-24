package com.sluka.taras.common.dto;

import com.sluka.taras.common.model.Cart;
import com.sluka.taras.common.model.Role;
import com.sluka.taras.common.model.Sex;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserDto extends BaseDto {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Boolean bane;
    private Role role;
    private Sex sex;
    private String password;
    private List<Cart> cardList = new ArrayList<>();
}
