package global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:/xmlForProperties/Admin.xml"})
public class AdminXmlConfig {
}


