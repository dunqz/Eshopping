//package com.crud.crudfrontendbackend.rabbitMQ;
//
//import com.crud.crudfrontendbackend.email.EmailMessage;
//import com.crud.crudfrontendbackend.email.EmailSender;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@AllArgsConstructor
//@Component
//public class EmailConsumer {
//
//    @Autowired
//    private final EmailSender emailSender;
//    @RabbitListener(queues = "maildevQ")
//    public void processEmail(EmailMessage emailMessage) {
//        String emailAddress = emailMessage.getEmailAddress();
//        String emailContent = emailMessage.getEmailContent();
//
//        // Use emailSender to send the email
//        emailSender.send(emailAddress, emailContent);
//
//        // Additional processing if needed
//    }
//
//}
//
////    public void processEmail(Map<String, String> message) {
////        String emailAddress = message.get("emailAddress");
////        String emailContent = message.get("emailContent");
////
////        // Use emailSender to send the email
////        emailSender.send(emailAddress, emailContent);
////
////    }
