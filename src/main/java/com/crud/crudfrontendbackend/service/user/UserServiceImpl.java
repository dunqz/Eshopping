package com.crud.crudfrontendbackend.service.user;

import com.crud.crudfrontendbackend.dto.UserDto;
import com.crud.crudfrontendbackend.dto.UserUpdateDto;
import com.crud.crudfrontendbackend.encryption.AESEncrypt;
import com.crud.crudfrontendbackend.exceptions.ApiRequestException;
import com.crud.crudfrontendbackend.model.User;
import com.crud.crudfrontendbackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    public User createUser(UserDto userDto) throws NoSuchAlgorithmException, InvalidKeySpecException, Exception {
        User existingEmailAddress = userRepository.findByEmailAddress(userDto.getEmailAddress());
        if (existingEmailAddress != null) {
            throw new ApiRequestException("Email address is already taken");
        }

        // Generate a secret key from a password and salt
        String password = userDto.getPassword(); // Get the plain password from the DTO
        String salt = "SaltValue"; // Replace with a securely generated salt
        SecretKey secretKey = AESEncrypt.getKeyFromPassword(password, salt);

        // Generate a random IV
        IvParameterSpec iv = AESEncrypt.generateIv();

        // Encrypt the password
        String encryptedPassword = AESEncrypt.encrypt("AES/CBC/PKCS5Padding", password, secretKey, iv);

        // Set the encrypted password in the User object
        userDto.setPassword(encryptedPassword);

        // Decrypt the stored password for logging (for testing/debugging purposes)
        String decryptedPassword = AESEncrypt.decrypt("AES/CBC/PKCS5Padding", userDto.getPassword(), secretKey, iv);
       log.info("Decrypted password: " + decryptedPassword);


        User user = new User();
        BeanUtils.copyProperties(userDto, user);
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