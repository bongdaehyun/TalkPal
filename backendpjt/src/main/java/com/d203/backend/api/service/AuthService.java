package com.d203.backend.api.service;

import com.d203.backend.db.entity.EmailToken;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.EmailRepository;
import com.d203.backend.db.repository.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private UserRepository userRepository;
    //Email토큰 생성
    public String createToken(User user){
        EmailToken emailToken=new EmailToken();
        emailToken.setToken("token" + user.getId());
        emailToken.setEmail(user.getEmail());
        emailRepository.save(emailToken);

        return emailToken.getToken();
    }
    //Email에게 이메일 인증 보내기
    public void sendVerificationMail(User user)throws NotFoundException {
        String link="http://localhost:8080/api/v1/auth/confirm/";
        if(user==null) throw new NotFoundException("사용자 조회 없음");
        String token=createToken(user);

        emailSenderService.sendEmail(link+token,user.getEmail());
    }

    //이메일 인증을 확인한다.
    public void verifyEmail(String token)throws NotFoundException{
        EmailToken emailToken=emailRepository.findByToken(token);
        if (emailToken ==null) throw new NotFoundException("토큰 없음");
        Optional<User> user=userRepository.findByEmail(emailToken.getEmail());
        user.get().setValid(true);
        userRepository.save(user.get());
    }

}
