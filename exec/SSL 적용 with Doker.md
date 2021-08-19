# SSL 적용 with Doker

# SSL 적용

1. letsencrypt 설치

```yaml
$ sudo apt-get update
$ sudo apt-get install letsencrypt
```

1. nginx 중지
- 80포트를 사용중이면 에러가 발생할 수 있음

```yaml
sudo docker stop <FRONT_CONTAINER_NAME>
```

1. 인증서 발급

```yaml
$ sudo letsencrypt certonly --standalone -d [도메인 네임]
ex ) sudo letsencrypt certonly --standalone -d i4d101.p.ssafy.io
```

- /etc/letsencrypt/live/[도메인 네임] 밑에 키가 있음

# Docker Network

- Reverse Proxy를 위한 Docker network 생성

```yaml
sudo docker  network create <network-name>
```

# SSL 옮기기

```yaml
$ sudo cp /etc/letsencrypt/live/<도메인 네임>/fullchain.pem <볼륨 매핑에 사용될 폴더>
$ sudo cp /etc/letsencrypt/live/<도메인 네임>/privkey.pem <볼륨 매핑에 사용될 폴더>
```

- nginx 파일 수정

```
server {
        listen 443 ssl;
        listen [::]:443 ssl;

        server_name [퍼블릭IPv4주소];

        ssl_certificate /usr/share/nginx/html/homepage/cert/fullchain.pem;
        ssl_certificate_key /usr/share/nginx/html/homepage/cert/privkey.pem;

        location /{
                        alias /usr/share/nginx/html/homepage/;
                        try_files $uri $uri/ /index.html;
        }

        location /api/v1 {
                        proxy_pass https://[퍼블릭IPv4주소]:8080;

                        proxy_http_version 1.1;

                        proxy_set_header Connection "";
                        proxy_set_header Host $host;
                        proxy_set_header X-Real-IP $remote_addr;
                        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                        proxy_set_header X-Forwarded-Proto $scheme;
                        proxy_set_header X-Forwarded-Host $host;
                        proxy_set_header X-Forwarded-Port $server_port;
        }
}
```

### Spring Boot SSL 설정

```jsx
# SSL
# server.port=8443
# server.http.port=8080
server.ssl.enabled=true
server.ssl.key-store-type=PKCS12
server.ssl.key-store=classpath:[key-store-path]
server.ssl.key-store-password=[password]
server.ssl.key-alias=[alias name]
trust.store=classpath:[key-store-path]
trust.store.password=[password]
```