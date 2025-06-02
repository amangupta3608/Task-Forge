package com.TaskForge.userService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestMailController {

    private final JavaMailSender mailSender;

    public String sendTestMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("amangupta0213@gmail.com");
        message.setSubject("Mail Test");
        message.setText("Hello from Springboot");
        mailSender.send(message);
        return "Mail sent successfully";
    }
}
