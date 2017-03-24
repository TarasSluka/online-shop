package com.sluka.taras.service;

import com.sluka.taras.common.dto.UserDto;
import com.sluka.taras.common.dto.UserResetPasswordDto;
import com.sluka.taras.common.model.User;
import com.sluka.taras.web.model.UserFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User save( User user);

    UserDto update(Long id, UserDto userDto);

    UserDto changeEnabled(Long id);

    UserDto update(UserDto userDto);

    boolean updatePassword(String oldPass, String newPass);

    void delete( User user);

    void deleteById( Long id);

    List<User> findAll();

    User findById( Long id);

    User findByEmail( String email);

    User findByUserName( String userName);

    boolean exists(Long id);

    Page<User> filter(UserFilterRequest userFilterRequest, Pageable pageable);

    User getCurrentUser();

    boolean resetPassword(User user, UserResetPasswordDto userResetPasswordDto);

    boolean isValidOldPassword(User user, String oldPassword);

    boolean isValidNewPassword(String newPassword);

}

