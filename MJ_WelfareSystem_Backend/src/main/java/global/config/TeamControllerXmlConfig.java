package global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:/xmlForProperties/TeamController.xml"})
public class TeamControllerXmlConfig {
}


