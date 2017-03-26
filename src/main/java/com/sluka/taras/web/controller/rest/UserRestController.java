package com.sluka.taras.web.controller.rest;

import com.sluka.taras.common.dto.UserDto;
import com.sluka.taras.common.dto.UserResetPasswordDto;
import com.sluka.taras.common.mapper.UserMapper;
import com.sluka.taras.common.model.Role;
import com.sluka.taras.common.model.User;
import com.sluka.taras.security.SecurityUtils;
import com.sluka.taras.service.PageableService;
import com.sluka.taras.service.PhotoService;
import com.sluka.taras.service.UserService;
import com.sluka.taras.web.model.UserFilterRequest;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class UserRestController {
    private final Logger logger = LogManager.getLogger(UserRestController.class);
    private final UserService userService;
    private final UserMapper userMapper;
    private final PageableService pageableService;
    private final PhotoService photoService;

    @Autowired
    UserRestController(UserService userService, UserMapper userMapper, PageableService pageableService, PhotoService photoService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.pageableService = pageableService;
        this.photoService = photoService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<User>> getUsers(UserFilterRequest userFilterRequest) {
        Pageable pageable = pageableService.getPageable(
                userFilterRequest.getPage(),
                userFilterRequest.getSize(),
                userFilterRequest.getSortType(),
                userFilterRequest.getSortParam());
        Page<User> list = userService.filter(userFilterRequest, pageable);
        if (list.getSize() == 0)
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        return new ResponseEntity(list, HttpStatus.OK);
    }


    //        @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
//    @PreAuthorize(pepmit)
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getCurrentUser() {
        String currentPrincipalName = SecurityUtils.getCurrentLogin();
        if (currentPrincipalName == "anonymousUser")
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        UserDto userDto = userMapper.toDto(userService.findByUserName(currentPrincipalName));
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        if (user == null) return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<UserDto>(userMapper.toDto(user), HttpStatus.OK);
    }

    @PreAuthorize("isAnonymous() or hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        Map map = new HashMap();
        if (userService.findByEmail(user.getEmail()) != null)
            map.put("email", "this email is exist");
        if (userService.findByEmail(user.getUserName()) != null)
            map.put("userName", "er1");
        if (map.size() != 0)
            return new ResponseEntity(map, HttpStatus.CONFLICT);
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        if (userService.save(user) == null)
            return new ResponseEntity(HttpStatus.CONFLICT);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}/enabled", method = RequestMethod.PUT)
    public ResponseEntity<Void> enabledUser(@PathVariable("id") long id) {
        User user = userService.findById(id);
        if (user == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        user.setEnabled(!user.getEnabled());
        if (userService.changeEnabled(id) == null)
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
        if (userService.findById((userDto.getId())) == null)
            return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
        userDto = userService.update(userDto);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateCurrentUser(@RequestBody UserDto userDto) {
        String currentPrincipalName = SecurityUtils.getCurrentLogin();
        if (currentPrincipalName == "anonymousUser")
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        User user = userService.findByUserName(currentPrincipalName);
        userDto = userService.update(user.getId(), userDto);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        if (userService.findById(id) == null)
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        userService.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody UserResetPasswordDto userResetPasswordDto) {
        logger.info(userResetPasswordDto.toString());
        String response = null;
        Map<String, String> stringStringMap = new HashMap<>();
        User user = userService.getCurrentUser();
        if (!userService.isValidOldPassword(user, userResetPasswordDto.getOldPassword())) {
            stringStringMap.put("massage", "Old password is wrong");
            return new ResponseEntity<>(stringStringMap, HttpStatus.BAD_REQUEST);
        } else {
            if (!userService.isValidNewPassword(userResetPasswordDto.getNewPassword())) {
                stringStringMap.put("massage", "New password is wrong");
                return new ResponseEntity<>(stringStringMap, HttpStatus.BAD_REQUEST);
            } else if (userService.resetPassword(user, userResetPasswordDto)) {
                stringStringMap.put("massage", "success");
                return new ResponseEntity<>(stringStringMap, HttpStatus.OK);
            } else {
                stringStringMap.put("massage", "Old password is wrong");
                return new ResponseEntity<>(stringStringMap, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(value = "/ava", method = RequestMethod.POST)
    public ResponseEntity<Void> saveAva(MultipartHttpServletRequest request) throws IOException {
        MultipartFile multipartFileList = request.getFile("image");
        if (multipartFileList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        User user = userService.getCurrentUser();
        Long id = null;
        if (multipartFileList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        if (user != null) {
            id = user.getId();
            photoService.savePhotoUser(id, multipartFileList);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @RequestMapping(value = "/ava", method = RequestMethod.GET)
    public ResponseEntity<Map<String, String>> getAva(HttpServletRequest request) throws IOException {
        User user = userService.getCurrentUser();
        Long id = null;
        if (user != null)
            id = user.getId();
        String avaUrl = null;
        if (id != null)
            avaUrl = photoService.getURLToPhotoUser(id);
        else avaUrl = photoService.noImageToUser();
        Map map = new HashMap();
        map.put("avaUrl", request.getContextPath() + avaUrl);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}