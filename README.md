# Getting Started

https://s3.us-west-2.amazonaws.com/secure.notion-static.com/81d835d3-1c8a-479c-94a6-85879970228d/ishadow2.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20221007%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20221007T100519Z&X-Amz-Expires=86400&X-Amz-Signature=7d509f5b879831d271663e2e21e512eac2d6ba671d267bf073942a7823907ae1&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22ishadow2.mp4%22&x-id=GetObject

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

