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

## API 명세(request/response 포함) / 엔드포인트
<br>

![회원가입](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/96f59a9c-19a0-45d4-808e-c745ea6f81d4)
---

![로그인](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/aacb1dc4-7d97-47b0-88c3-b5ca599fcbad)
---

![게시글 쓰기](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/3a5bbe33-1417-404a-813c-eea17c07010c)
---

![게시글 읽기](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/9ed27c4d-2798-448d-80a8-6d502d1e917e)
---


![게시글 수정](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/0c412b06-cb54-476c-940c-79c1c3c480e4)
---

![게시글 삭제](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/d8e8002d-40c2-4733-b652-e14ca52c36a4)
---

![게시글 리스트](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/8b7f8734-79e1-4009-9683-fa6ff92bbf6f)
---

<br><br>


## 데이터베이스 테이블 구조

![db](https://github.com/HwangJaeHwan/wanted-pre-onboarding-backend/assets/58110333/59796cc1-5fa3-4985-b78b-7493e6f4debc)

<br><br><br><br>

## [구현한 API의 동작을 촬영한 데모 영상 링크](https://drive.google.com/file/d/1RrEawVqEiqJmvBXQxOE6DHZCpFqsDl2Y/view?usp=sharing)

<br><br><br>

## 구현 방법 및 이유에 대한 간략한 설명

- **과제 1. 사용자 회원가입 엔드포인트**
  
  a. json 형식으로 email와 password를 입력받아 email이 중복되는지 체크하고 중복되면 exception을 던집니다.
  
  b. spring validation으로 유효성 검사를 진행하여 통과하지 못하면 exception을 던집니다.
  
  c. 해당 email과 password로 새로운 user를 만듭니다. (이때 password는 암호화 합니다.)<br><br>

- **과제 2. 사용자 로그인 엔드포인트**
  
  a. json 형식으로 email와 password를 입력받아 검증 및 유효성 검사를 합니다.
  
  b. 해당 정보가 올바르면 토큰을 발행합니다.(JWT 이기때문에 Bearer 방식을 사용했습니다.)<br><br>

- **과제 3. 새로운 게시글을 생성하는 엔드포인트**
  
  a. json 형식으로 title과 content를 입력받습니다.
  
  b. Authorzation에 Bearer 토큰이 있는지 확인하고 해당 토큰을 파싱해 유저를 식별합니다.(토큰에 이상이 있으면 exception을 던집니다.)
  
  c. 입력받은 정보와 식별한 유저 정보를 통해 새로운 게시글을 생성합니다.<br><br>

- **과제 4. 게시글 목록을 조회하는 엔드포인트**
  
  a. query-string으로 page 값을 입력받습니다. max함수를 통해 page 값이 1보다 아래로 내려가는 것을 방지했습니다.
  
  b. 해당 page 값을 사용해 쿼리를 날려 게시글을 가져옵니다.<br><br>

- **과제 5. 특정 게시글을 조회하는 엔드포인트**

  a. 특정 게시글의 id 값을 PathVariable로 받아 해당 게시물에 접근합니다.
  
  b. entity가 아닌 새로운 dto를 만들어 entity에서 필요한 정보를 해당 dto에 담아 리턴합니다.<br><br>

- **과제 6. 특정 게시글을 수정하는 엔드포인트**

  
  a. 수정할 title와 content를 json형식으로 입력받습니다.
  
  b. 특정 게시글의 id 값을 PathVariable로 받아 해당 게시물에 접근합니다.
  
  c. Authorzation에 Bearer 토큰이 있는지 확인하고 해당 토큰을 파싱해 유저를 식별합니다.(토큰에 이상이 있으면 exception을 던집니다.)
  
  d. 식별한 유저와 해당 게시글의 작성자를 비교하여 다르면 exception을 던집니다.
  
  e. 같다면 입력받은 정보를 바탕으로 해당 게시글의 내용을 수정합니다.<br><br>

- **과제 7. 특정 게시글을 삭제하는 엔드포인트**
  
   a. 특정 게시글의 id 값을 PathVariable로 받아 해당 게시물에 접근합니다
  
   b. Authorzation에 Bearer 토큰이 있는지 확인하고 해당 토큰을 파싱해 유저를 식별합니다.(토큰에 이상이 있으면 exception을 던집니다.)
  
   c. 식별한 유저와 해당 게시글의 작성자를 비교하여 다르면 exception을 던집니다.
  
   d. 같다면 해당 게시글을 삭제합니다.<br><br>

