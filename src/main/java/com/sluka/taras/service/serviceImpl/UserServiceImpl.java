package com.sluka.taras.service.serviceImpl;

import com.sluka.taras.service.PhotoService;
import com.sluka.taras.service.UserService;
import com.sluka.taras.common.dto.UserDto;
import com.sluka.taras.common.dto.UserResetPasswordDto;
import com.sluka.taras.common.mapper.UserMapper;
import com.sluka.taras.common.model.User;
import com.sluka.taras.repository.UserRepository;
import com.sluka.taras.security.SecurityUtils;
import com.sluka.taras.specification.UserSpecification;
import com.sluka.taras.web.model.UserFilterRequest;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final org.apache.logging.log4j.Logger Logger = LogManager.getLogger(UserServiceImpl.class);
    private final PhotoService photoService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSpecification userSpecification;
    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserSpecification userSpecification, UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder, PhotoService photoService, UserMapper userMapper) {
        this.photoService = photoService;
        this.userSpecification = userSpecification;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public final User save(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public final UserDto changeEnabled(Long id) {
        User user = findById(id);
        user.setEnabled(!user.getEnabled());
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public final UserDto update(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public final UserDto update(Long id, UserDto userDto) {
        User user = findById(id);
        if (userDto.getFirstName() != null)
            user.setFirstName(userDto.getFirstName());
        if (userDto.getLastName() != null)
            user.setLastName(userDto.getLastName());
//        if (userDto.getUserName() != null)
//            user.setUserName(userDto.getUserName());
        if (userDto.getEmail() != null)
            user.setEmail(userDto.getEmail());
        if (userDto.getPhoneNumber() != null)
            user.setPhoneNumber(userDto.getPhoneNumber());
        if (userDto.getSex() != null)
            user.setSex(userDto.getSex());
        user = userRepository.save(user);
        userDto = userMapper.toDto(user);
        return userDto;
    }

    @Override
    public boolean updatePassword(String oldPass, String newPass) {
        return false;
    }

    @Override
    public final void delete(final User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public final List<User> findAll() {
        return userRepository.findAll();
    }

    public final long count() {
        return userRepository.count();
    }

    @Override
    public final User findById(final Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public final User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public final User findByUserName(final String userName) {
        return userRepository.findByUserName(userName);
    }

//    @Override
//    public final List<User> findByEnabled(final boolean enabled) {
//        return userRepository.findByEnabled(enabled);
////    }
//
//    @Override
//    public final List<User> findByFirstName(final String firstName) {
//        return userRepository.findByFirstName(firstName);
//    }
//
//    @Override
//    public final List<User> findByLastName(final String lastName) {
//        return userRepository.findByLastName(lastName);
//    }

    @Override
    public boolean exists(Long id) {
        return userRepository.exists(id);
    }

//    @Override
//    public Page<User> listAllByPage(Pageable pageable) {
//        return userRepository.findAll(pageable);
//}

    public Page<User> filter(UserFilterRequest userFilterRequest, Pageable pageable) {
        userSpecification.setFilter(userFilterRequest);
        return userRepository.findAll(userSpecification, pageable);
    }

    @Override
    public User getCurrentUser() {
        String currentLogin = SecurityUtils.getCurrentLogin();
        return findByUserName(currentLogin);
    }

    @Override
    public boolean isValidNewPassword(String newPassword) {
        if (newPassword.length() < 6)
            return false;
        else return true;
    }

    @Override
    public boolean resetPassword(User user, UserResetPasswordDto userResetPasswordDto) {
        String oldPassword = userResetPasswordDto.getOldPassword();
        String newPassword = userResetPasswordDto.getNewPassword();
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            Logger.info("true");
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } else {
            Logger.info("bad old password");
            return false;
        }
    }

    @Override
    public boolean isValidOldPassword(User user, String oldPassword) {
        String dbPassword = user.getPassword();
        if (passwordEncoder.matches(oldPassword, user.getPassword()))
            return true;
        return false;
    }
}
