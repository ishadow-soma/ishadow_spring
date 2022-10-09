# Ishadow : 사용자 맞춤 섀도잉 영상 제작 및 학습 서비스

## 📝 Description

기존의 섀도잉 학습 서비스에서 학습 콘텐츠를 제작하기 위해서 직접 콘텐츠를 제작합니다. Ishadow는 섀도잉 학습 콘텐츠를 더 쉬운 제작 환경을 제공하고 사용자에게 더 많은 콘텐츠를 제공받을 수 있도록 도와주는 서비스입니다.

서비스 사용자는 자신의 영상과 유튜브 동영상을 콘텐츠로 사용할 수 있습니다. 콘텐츠를 업로드하면 우리는 자동으로 스크립트를 추출하고, 문장 단위로 구간을 나누어 섀도잉 콘텐츠를 제작합니다. 서비스 사용자는 원하는 영상으로 생성한 섀도잉 콘텐츠를 이용하여 어려운 구간이나 다시 학습하고 싶은 구간을 자유롭게 선택하여 즐겨찾기로 등록해 쉽게 학습할 수 있습니다.

## 📷 Preview
![ishadow](https://user-images.githubusercontent.com/47744119/194694308-6430de01-f76a-4f23-8e85-de08090b030a.gif)

## 📋 Functions

- 회원가입 및 로그인 기능
    - AWS ElastiCache(Redis)를 이용한 인증 정보 및 토큰 검증
    - AWS SES를 통한 이메일 인증
    - NAVER, KAKAO, GOOGLE OAUTH 2.0
- 영상 업로드 기능
    - 대용량 파일 업로드 및 파일 관리
- 유튜브 영상 음원 추출 기능
    - 유뷰트 URL을 활용해 FFMPEG로 음성 추출
- Spring boot와 Django간의 데이터 송수신 연결
- FFMPEG를 이용한 영상에서 음원으로 변환 기능
- Google STT와 비동기 처리를 사용한 스크립트 추출
- 섀도잉 영상 특정 구간 반복 재생
- 구두점를 활용한 스크립트 정제
- 원하는 문장 즐겨찾기
- 난이도 별 영상 필터링 및 페이징 처리
- 음성 녹음 및 재생 기능 (섀도잉 영상과 비교를 통한 피드백 목적)
- 영상 및 음성 서버 구축
    - 영상 업로드를 통한 섀도잉 영상 제작시 영상 저장 및 스트리밍 방식으로 송신해주는 서버 구축
    - 녹음된 음성을 저장 및 관리, 스트리밍으로 송신하는 서버 구축
- S3를 통한 이미지 저장 및 관리
    - 영상 썸네일 추출
- nginx에서 react, django, spring boot로 리버시 프록시 적용
- HTTPS 적용
- AWS Router 53 도메인 관리
    - https://www.ishadow.kr
    - https://subdomain.ishadow.kr
    - [https://media.ishadow.kr](https://media.ishadow.kr)
- Gitlab CI/CD를 사용한 자동화 배포 구축
- Swagger를 통한 API 명세서 제공

# Getting Started

## Local   
    Local Execution   
        gradlew build   
        cd ./build/libs   
        java - jar ishadow-0.0.1-SNAPSHOT.jar --spring.profiles.active=local   
        
    Local Swagger        
        http://localhost:9000/swagger-ui.html#/

## Prod    
    https://ishadow.kr    
    https://subdomain.ishadow.kr/swagger-ui.html#/

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/html/#build-image)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

