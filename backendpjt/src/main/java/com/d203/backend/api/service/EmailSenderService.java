package com.d203.backend.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//JavaMailSender 객체를 사용하여 이메일 보낸다.
@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String text, String toemail){
        SimpleMailMessage mss= new SimpleMailMessage();
        mss.setText(text);//내용
        mss.setFrom("admin@admin.com");//보내는 이 이메일
        mss.setTo(toemail);//받는 사람
        mss.setSubject("회원가입 인증 메일");//메일 제목
        javaMailSender.send(mss);
    }

}
