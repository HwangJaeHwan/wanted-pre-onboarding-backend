# 원티드 프리온보딩 백엔드 인턴십 - 선발 과제

## 참가자 이름 : 황재환

## 애플리케이션의 실행 방법 (엔드포인트 호출 방법 포함)

### 실행
1. ```git clone https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend.git``` 명령을 실행합니다.<br><br>
2. ```src/main/resources``` 경로의 application.yml 파일을 수정합니다.
```yaml
jwt:
  jwt-key: "c7L0+z1bI67OpGIeYN9NcKgXBsHKiR2RlYX1Vzj4ULA="

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:<사용할 포트번호>/<사용할 DB명>
    username: <사용할 계정명>
    password: <사용할 비밀번호>

  jpa:
    open-in-view: false
    show-sql: false
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```
3. 프로젝트 최상단으로 이동 후, ```./gradlew clean build```명령어로 빌드합니다.<br><br>
4. ```/build/libs``` 경로의 jar 파일을 ```java -jar``` 명령어로 실행합니다.<br><br>




### docker-compose 실행

1. ```git clone https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend.git``` 명령을 실행합니다.<br><br>
2. docker-compose.yml (수정이 필요한 점이 있으면 수정해주세요.)
```yaml
version: '3.1'

services:

  database:

     image: mysql:8.0
    container_name: database_wanted
    environment:
      MYSQL_DATABASE: wanted
      MYSQL_ROOT_PASSWORD: 1234
    command: ['--character-set-server=utf8mb4', '--collation-server=utf8mb4_unicode_ci']
    ports:
      - "3306:3306"
    volumes:
      - ./db:/var/lib/mysql

  application:
    image: zxcv0069/wanted-pre-onboarding:latest
    ports:
      - "80:8080"
    depends_on:
      - database
    container_name: app_wanted
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://database:3306/wanted?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: 1234

```
3. ```docker-compose up -d``` 명령어로 컨테이너를 실행해주세요.


## 데이터베이스 테이블 구조

## 구현한 API의 동작을 촬영한 데모 영상 링크

## 구현 방법 및 이유에 대한 간략한 설명

## API 명세(request/response 포함)
