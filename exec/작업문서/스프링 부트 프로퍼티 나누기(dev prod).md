# 스프링 부트 프로퍼티 나누기(dev/prod)

### dev/prod properties 생성 및 설정

```
# 기존 프로퍼티 설정

isProd=false
```

application-prod.properties

```
# 기존 프로퍼티 설정

isProd=true
```

### 빌드 시 dev 프로퍼티 사용을 위한 VM 옵션

`인텔리제이` 기준

> Run → Edit Configurations → VM options

```
# 다음 내용 추가

-Dspring.profiles.active=dev
```