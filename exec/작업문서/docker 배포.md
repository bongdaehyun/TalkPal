# docker 배포

## Frontend

docker file

```docker
FROM node:lts-alpine as build-stage
WORKDIR /homepage
COPY package*.json ./

RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine as production-stage
RUN rm /etc/nginx/conf.d/default.conf
COPY ./nginx/homepage.conf /etc/nginx/conf.d/homepage.conf

COPY --from=build-stage ./homepage/dist /usr/share/nginx/html/homepage
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

이미지 생성

```bash
docker build <dockerfile_path> <image_name>
ex) docker build ./frontend front
```

이미지 실행

```bash
docker run [옵션] [이미지 이름] [실행할 파일]
ex) docker run -d --name front -p 80:80 
```

## Backend

docker file

```docker
FROM adoptopenjdk:8-jdk-hotspot AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootjar

FROM adoptopenjdk:8-jdk-hotspot
COPY --from=builder build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-Dkms.url=ws://[EC2 퍼블릭 IPv4 주소]:8888/kurento", "-Dspring.profiles.active=prod","-jar","/app.jar"]
```

이미지 생성

```bash
docker build <dockerfile_path> <image_name>
ex) docker build ./backend back
```

이미지 실행

```bash
docker run [옵션] [이미지 이름] [실행할 파일]
ex) docker run -d --name back -p 8080:8080
```