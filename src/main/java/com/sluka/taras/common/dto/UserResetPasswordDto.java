package com.sluka.taras.common.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class UserResetPasswordDto {

    @Length(min = 6)
        String oldPassword;
    @Length(min = 6)
    String newPassword;

}
