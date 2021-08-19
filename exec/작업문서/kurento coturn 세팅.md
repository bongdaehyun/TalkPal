# kurento / coturn 세팅

### 이 문서는 프로젝트 `빌드` 및 `배포` 시 사용하는 Kurento Media Server 및 Coturn Server 를 세팅하는 방법입니다.

### 사전 준비 : docker 설치

### AWS 인바운드 규칙추가 - 프로토콜 및 포트 허용

```
3478 : UDP
3478 : TCP
49152-65535 : UDP
```

### Coturn Server 설정

```bash
# Coturn 설치 
sudo apt-get update && sudo apt-get install --no-install-recommends --yes coturn
```

```bash
# turnserver 사용 설정
vim /etc/default/coturn

# 주석제거
TURNSERVER_ENABLED=1
```

```bash
# turnserver 설정
vim /etc/turnserver.conf

# 주석 제거 및 내용 수정
listening-port=3478
tls-listening-port=5349
listening-ip=<EC2 프라이빗 IPv4 주소>
external-ip=<EC2 퍼블릭 IPv4 주소>/<EC2 프라이빗 IPv4 주소>
relay-ip=<EC2 프라이빗 IPv4 주소>
fingerprint
lt-cred-mech
user=myuser:mypassword
log-file=/var/log/turn.log
simple-log
```

```bash
# contrun 재실행
service coturn restart
```

### KMS 설정

```bash
# 컨테이너와 연결할 볼륨 생성
sudo docker volume create kurento
```

```bash
# KMS 설치
sudo docker run -d --name kms --network host -v kurento:/etc/kurento kurento/kurento-media-server:latest
```

```bash
# 볼륨 폴더 권한 가져오기
sudo chown ubuntu:ubuntu kurento -R
```

```bash
# 설정 파일 내용 수정
vi /var/lib/docker/volumes/kurento/_data/modules/kurento/WebRtcEndpoint.conf.ini

# 다음 내용 수정
stunServerAddress=<퍼블릭 아이피 주소>
stunServerPort=3478
turnURL=myuser:mypassword@<퍼블릭 아이피 주소>?transport=udp
```

```bash
# KMS 재실행
sudo docker restart kms
```

---

### 스프링 부트 KMS 연결 설정

`인텔리제이` 기준 설정

> Run → Edit Configurations → VM options

```
-Dkms.url=ws://[EC2 퍼블릭 주소]:8888/kurento
```