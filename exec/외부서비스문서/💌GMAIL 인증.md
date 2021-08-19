# 💌GMAIL 인증

## Gmail STMP 사용

[https://www.siteground.com/kb/gmail-smtp-server/](https://www.siteground.com/kb/gmail-smtp-server/)

## 관리자 메일 보안 변경

- 보안 단계 허용 [https://myaccount.google.com/lesssecureapps](https://myaccount.google.com/lesssecureapps)
- 앱비밀번호 설정 [https://myaccount.google.com/security](https://myaccount.google.com/security)
- [참고](https://velog.io/@max9106/Spring-Boot-Gmail-SMTP-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0%EB%A9%94%EC%9D%BC%EB%B3%B4%EB%82%B4%EA%B8%B0)

## application-prod.properties 파일 변경

```
#gmail 사용
spring.mail.host=smtp.gmail.com
spring.mail.port=587
#gmail ID
spring.mail.username=<gmail_id>
#gmail pwd
spring.mail.password=<gmail_app_pwd)

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000 
#서버 url
mailurl=https://[퍼블릭 IPv4 주소]:8080
```