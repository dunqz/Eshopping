package com.crud.crudfrontendbackend.service.user;

import com.crud.crudfrontendbackend.dto.UserDto;
import com.crud.crudfrontendbackend.dto.UserUpdateDto;
//import com.crud.crudfrontendbackend.email.EmailMessage;
import com.crud.crudfrontendbackend.email.EmailSender;
import com.crud.crudfrontendbackend.email.EmailService;
import com.crud.crudfrontendbackend.encryption.AESEncrypt;
import com.crud.crudfrontendbackend.exceptions.ApiRequestException;
import com.crud.crudfrontendbackend.model.User;
import com.crud.crudfrontendbackend.properties.EmailBuilderService;
import com.crud.crudfrontendbackend.repository.UserRepository;
import com.crud.crudfrontendbackend.email.token.ConfirmationToken;
import com.crud.crudfrontendbackend.email.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ConfirmationTokenService confirmationTokenService;

    @Autowired
    private final EmailSender emailSender;

    @Autowired
    private RabbitTemplate rabbitTemplate; // Inject RabbitMQ template

    /**
     *
     * @return
     */
    public List<User> getALlUsers(){
        return userRepository.findAll();
    }


    /**
     *
     * @param email
     * @return
     */
    public User getOneUser(String email){

        User existingUser = userRepository.findByEmailAddress(email);
        if(existingUser  == null ){
            throw new ApiRequestException("User does not exists");
        }
        return existingUser;
    }


    /**
     * Create user / register user
     *
     * @param userDto
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws Exception
     */
    @Transactional
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

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String link = "https://example.com/confirm?token=" + token; // Generate confirmation link

        // Prepare email content
        String emailContent = buildEmail(newUser.getName(), link);

        // Send message to RabbitMQ with email details
        Map<String, String> message = new HashMap<>();
        message.put("emailAddress", newUser.getEmailAddress());
        message.put("emailContent", emailContent);
//        EmailMessage emailMessage = new EmailMessage();
//        emailMessage.setEmailAddress(newUser.getEmailAddress());
//        emailMessage.setEmailContent(emailContent);

//        // Publish message to RabbitMQ
         rabbitTemplate.convertAndSend("send-email-maildev", "foo.bar.email", message);
//
//        emailSender.send(newUser.getEmailAddress(), emailContent);

        // Publish message to RabbitMQ
        //rabbitTemplate.convertAndSend("send-email-maildev","foo.bar.email", emailMessage); // Send message to RabbitMQ queue

        return newUser;
    }

    /**
     *
     * @param id
     * @return
     */
    public User deleteUser(Long id){
        Optional<User> existingUser = userRepository.findById(id);
        if(!existingUser.isPresent()){
            throw new ApiRequestException("User already deleted or does not exists");
        }
        userRepository.deleteById(id);
        return null;
    }

    /**
     *
     * @param email
     * @param userUpdateDto
     * @return
     */
    public User updateUser(String email, UserUpdateDto userUpdateDto){
        User existingUser = userRepository.findByEmailAddress(email);
        if (existingUser == null) {
            throw new ApiRequestException("User does not exist");
        }

        BeanUtils.copyProperties(userUpdateDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return updatedUser;

    }
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
//        User user = User.builder()
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .userName(userDto.getUserName())
//                .build();
//        userRepository.save(user);