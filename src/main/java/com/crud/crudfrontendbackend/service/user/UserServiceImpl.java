package com.crud.crudfrontendbackend.service.user;

import com.crud.crudfrontendbackend.dto.UserDto;
import com.crud.crudfrontendbackend.dto.UserUpdateDto;
import com.crud.crudfrontendbackend.exceptions.ApiRequestException;
import com.crud.crudfrontendbackend.model.User;
import com.crud.crudfrontendbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private UserRepository userRepository;

    public List<User> getALlUsers(){
        return userRepository.findAll();
    }

    public User getOneUser(String email){

        User existingUser = userRepository.findByEmailAddress(email);
        if(existingUser  == null ){
            throw new ApiRequestException("User does not exists");
        }
        return existingUser;
    }
    public User createUser(UserDto userDto){

        User existingEmailAddress = userRepository.findByEmailAddress(userDto.getEmailAddress());
        if(existingEmailAddress != null ){
            throw new ApiRequestException("Email address is already taken");
        }

        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        User newUser = userRepository.save(user);
        return newUser;
    }

    public User deleteUser(Long id){
        Optional<User> existingUser = userRepository.findById(id);
        if(!existingUser.isPresent()){
            throw new ApiRequestException("User already deleted or does not exists");
        }
        userRepository.deleteById(id);
        return null;
    }

    public User updateUser(String email, UserUpdateDto userUpdateDto){
        User existingUser = userRepository.findByEmailAddress(email);
        if (existingUser == null) {
            throw new ApiRequestException("User does not exist");
        }

        BeanUtils.copyProperties(userUpdateDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;

    }

}
//        User user = User.builder()
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .userName(userDto.getUserName())
//                .build();
//        userRepository.save(user);