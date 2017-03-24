package com.sluka.taras.common.mapper;

import com.sluka.taras.common.dto.UserDto;
import com.sluka.taras.common.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setCreatedDate(user.getCreatedDate());
        userDto.setModifiedDate(user.getModifiedDate());
        userDto.setBane(user.getEnabled());
        userDto.setRole(user.getRole());
        userDto.setSex(user.getSex());
        userDto.setCardList(user.getCardList());
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUserName(userDto.getUserName());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCreatedDate(userDto.getCreatedDate());
        user.setModifiedDate(userDto.getModifiedDate());
        user.setPassword(userDto.getPassword());
        user.setEnabled(userDto.getBane());
        user.setRole(userDto.getRole());
        user.setSex(userDto.getSex());
        user.getCardList().addAll(userDto.getCardList());
        return user;
    }
}
