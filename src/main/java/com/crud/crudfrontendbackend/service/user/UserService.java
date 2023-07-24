package com.crud.crudfrontendbackend.service.user;

import com.crud.crudfrontendbackend.dto.UserDto;
import com.crud.crudfrontendbackend.dto.UserUpdateDto;
import com.crud.crudfrontendbackend.model.User;

import java.util.List;

public interface UserService {

    User createUser(UserDto userDto);

    List<User> getALlUsers();

    User getOneUser(String email);

    User deleteUser(Long id);

    User updateUser(String email, UserUpdateDto userUpdateDto);
}