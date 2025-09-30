plugins {
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    war
    java
}

group = "com.mju"
version = "1.0.0-BUILD-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    // Spring Boot (BOM으로 버전 일괄 관리)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-taglibs")
    implementation("jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api:3.0.0")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")

    // JSP 렌더링
    implementation("org.apache.tomcat.embed:tomcat-embed-jasper")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl")

    // MyBatis
    implementation("org.mybatis:mybatis:3.5.14")
    implementation("org.mybatis:mybatis-spring:3.0.3")

    // JDBC & DB
    implementation("org.springframework:spring-jdbc")
    runtimeOnly("mysql:mysql-connector-java:8.0.33")

    // Jackson: Boot BOM에 위임. 구버전/ASL 제거
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

    // Logging (기존 구성 유지 목적)
    implementation("org.slf4j:slf4j-api:1.6.6")
    runtimeOnly("org.slf4j:jcl-over-slf4j:1.6.6")
    runtimeOnly("org.slf4j:slf4j-log4j12:1.6.6")
    runtimeOnly("ch.qos.reload4j:reload4j:1.2.22")

    // Javax/Jakarta (기존 코드 호환용)
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("javax.mail:javax.mail-api:1.5.5")
    implementation("javax.mail:mail:1.4.7")
    implementation("javax.inject:javax.inject:1")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // Commons & 파일 업로드
    implementation("commons-io:commons-io:2.11.0")
    implementation("commons-fileupload:commons-fileupload:1.5")

    // AspectJ
    implementation("org.aspectj:aspectjrt:1.6.10")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<Copy>("processResources") {
    from("src/main/webapp/WEB-INF/spring") {
        into("spring")
    }
}

tasks.bootRun {
    systemProperty("spring.profiles.active", (project.findProperty("profile") ?: "local").toString())
}

tasks.test {
    useJUnitPlatform()
}


