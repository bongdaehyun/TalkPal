package com.d203.backend.api.service.Email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//JavaMailSender 객체를 사용하여 이메일 보낸다.
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String text, String toemail) throws MessagingException {
        //html을 이용하여 인증메일 텍스트 구성
        MimeMessage mss= javaMailSender.createMimeMessage();
        String tag="<a href=\""+text+"\">회원 가입 인증 !</a>";
        mss.setText(tag,"utf-8","html");
        mss.setFrom(new InternetAddress("admin@gmail.com"));
        mss.setRecipient(Message.RecipientType.TO,new InternetAddress(toemail));
        //mss.setTo(toemail);//받는 사람
        mss.setSubject("회원가입 인증 메일");//메일 제목
        javaMailSender.send(mss);
        //System.out.println(text+" "+toemail);
    }

}
