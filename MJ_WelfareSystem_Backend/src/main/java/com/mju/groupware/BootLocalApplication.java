package com.mju.groupware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
@ImportResource({
        "classpath*:spring/common-context.xml",
        "classpath*:spring/root-context.xml",
        "classpath*:spring/security-context.xml"
})
public class BootLocalApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootLocalApplication.class, args);
    }

    @Bean(name = "webSecurityExpressionHandler")
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler(ApplicationContext ctx) {
        DefaultWebSecurityExpressionHandler h = new DefaultWebSecurityExpressionHandler();
        h.setApplicationContext(ctx);
        return h;
    }
}


